package Engine;

import java.util.concurrent.locks.LockSupport;

public class GameLoop {
    private GamePanel gamePanel;

    public GameLoop(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public Runnable getGameLoopProcess() {
        if (Config.GAME_LOOP_TYPE == GameLoopType.MAX_PERFORMANCE) {
            return getMaxPerformanceModeGameLoop();
        }
        else if (Config.GAME_LOOP_TYPE == GameLoopType.POWER_SAVER){
            return getPowerSaverGameLoop();
        }
        else {
            throw new IllegalStateException("Invalid Game Loop Type");
        }
    }

    // a slightly modified version of the Minecraft game loop
    // designed to SEND IT so that the game runs as best as it can
    private Runnable getMaxPerformanceModeGameLoop() {
        return new Runnable() {
            @Override
            public void run() {
                // FPS and UPS are tied together for engine simplicity
                double targetTickRate = 1000000000 / (float) Config.TARGET_FPS;

                long previousTime = System.nanoTime();
                double delta = 0;
                boolean running = true;

                while (running) {
                    long currentTime = System.nanoTime();
                    delta += (currentTime - previousTime) / targetTickRate;
                    previousTime = currentTime;

                    if (delta >= 1) {
                        gamePanel.update();
                        gamePanel.setDoPaint(true);
                        gamePanel.repaint();
                        delta--;
                    }
                }
            }
        };
    }

    // a slightly modified version of the game loop found here: https://gist.github.com/keychera/c88794e953b8fb3f3334df93be7a99a6
    // designed to be less CPU intensive
    private Runnable getPowerSaverGameLoop() {
        return new Runnable() {
            @Override
            public void run() {
                // UPS and FPS are tied together for engine simplicity
                final long TIME_BETWEEN_UPDATES = 1000000000L / Config.TARGET_FPS;

                final int MAX_UPDATES_BEFORE_RENDER = 5;
                long nextUpdateTime = System.nanoTime();

                while (!Thread.currentThread().isInterrupted())
                {
                    long now = System.nanoTime();
                    int updateCount = 0;

                    //Do as many game updates as we need to, potentially playing catchup.
                    while(now >= nextUpdateTime && updateCount < MAX_UPDATES_BEFORE_RENDER) {
                        gamePanel.update();
                        gamePanel.setDoPaint(true);
                        nextUpdateTime += TIME_BETWEEN_UPDATES;
                        updateCount++;
                        now = System.nanoTime();
                    }

                    // If for some reason an update takes forever, this prevents it from running insane number of catch-ups.
                    if (now >= nextUpdateTime) {
                        nextUpdateTime = now;
                    }

                    gamePanel.repaint();

                    // Sleep to the next tick deadline without repeated 1 ms sleeps adding frame-time jitter.
                    long timeUntilNextUpdate;
                    while (!Thread.currentThread().isInterrupted()
                            && (timeUntilNextUpdate = nextUpdateTime - System.nanoTime()) > 0) {
                        LockSupport.parkNanos(timeUntilNextUpdate);
                    }
                }
            }
        };
    }
}
