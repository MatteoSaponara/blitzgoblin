package Screens;

import Engine.GamePanel;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Level.Player;
import Level.PlayerListener;
import Maps.GamblingRoom;
import Players.Cat;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;


// This class is for when the platformer game is actually being played
public class PlayLevelScreen extends Screen implements PlayerListener {
    private static final int HEART_FRAME_SIZE = 30;
    private static final int HEART_FRAME_COUNT = 3;
    private static final float HEART_SCALE = 1.4f;
    private static final int HEART_DISPLAY_SIZE = Math.round(HEART_FRAME_SIZE * HEART_SCALE);
    private static final int HEART_FRAME_DURATION = 8;
    private static final int HEART_LAST_FRAME_PAUSE = 30;
    private static final int HEART_STAGGER_DELAY = 4;
    private static final int COIN_FRAME_SIZE = 26;
    private static final int COIN_FRAME_HEIGHT = 30;
    private static final int COIN_FRAME_COUNT = 8;
    private static final int COIN_FRAME_DURATION = 11;
    private static final int COIN_DISPLAY_WIDTH = Math.round(COIN_FRAME_SIZE * HEART_SCALE);
    private static final int COIN_DISPLAY_HEIGHT = Math.round(COIN_FRAME_HEIGHT * HEART_SCALE);

    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected BufferedImage[] heartFrames;
    private BufferedImage[] coinFrames;
    private long heartAnimationTicks;
    private long coinAnimationTicks;
    protected BufferedImage testImage;
    protected PlayLevelScreenState playLevelScreenState;
    protected int screenTimer;
    protected LevelClearedScreen levelClearedScreen;
    protected LevelLoseScreen levelLoseScreen;
    protected boolean levelCompletedStateChangeStart;

   public PlayLevelScreen(ScreenCoordinator screenCoordinator, Map map) {
	   this.screenCoordinator = screenCoordinator;
	   this.map = map;
	   }

    public void initialize() {
        // define/setup map
        //this.map = new TestMap();

        // setup player
        this.heartFrames = new BufferedImage[HEART_FRAME_COUNT];
        for (int frame = 0; frame < HEART_FRAME_COUNT; frame++) {
            this.heartFrames[frame] = ImageLoader.loadSubImage(
                    "heart.png", frame * (HEART_FRAME_SIZE + 1), 0, HEART_FRAME_SIZE, HEART_FRAME_SIZE);
        }
        this.coinFrames = new BufferedImage[COIN_FRAME_COUNT];
        for (int frame = 0; frame < COIN_FRAME_COUNT; frame++) {
            this.coinFrames[frame] = ImageLoader.loadSubImage(
                    "hudcoin.png", frame * (COIN_FRAME_SIZE + 1), 0, COIN_FRAME_SIZE, COIN_FRAME_HEIGHT);
        }
        this.heartAnimationTicks = 0;
        this.coinAnimationTicks = 0;
        this.player = new Cat(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y, false);
        int maxHealth = Player.DEFAULT_MAX_HEALTH + screenCoordinator.getBonusHearts();
        player.setMaxHealth(maxHealth);
        player.setPlayerHealth(maxHealth);
        if (map instanceof GamblingRoom) {
            player.pickupShotgun();
            player.unlockSingleAmmo();
        }
        this.player.setCoins(screenCoordinator.getCoinsCollected());
        this.player.setMap(map);
        map.centerCameraOnPlayer(player);
        this.player.addListener(this);

        levelClearedScreen = new LevelClearedScreen();
        levelLoseScreen = new LevelLoseScreen(this);

        this.playLevelScreenState = PlayLevelScreenState.RUNNING;
    }

    public void update() {
        // based on screen state, perform specific actions
        switch (playLevelScreenState) {
            // if level is "running" update player and map to keep game logic for the platformer level going
            case RUNNING:
                updateHudAnimations();
                player.update();
                map.update(player);
                if(player.hasShotgun())
                    if (player.getShotgun() != null) {
                        player.getShotgun().update();
                    }
                break;
            // if level has been completed, bring up the level cleared screen
            case LEVEL_COMPLETED:
                if (levelCompletedStateChangeStart) {
                    screenTimer = 130;
                    levelCompletedStateChangeStart = false;
                } else {
                    levelClearedScreen.update();
                    screenTimer--;
                    if (screenTimer <= 0) {
                        screenCoordinator.advanceLevel(player.getCoins(), player.getMaxHealth());
					}
				}
                    
                    break;
            // wait on level lose screen to make a decision (either resets level or sends player back to main menu)
            case LEVEL_LOSE:
                levelLoseScreen.update();
                break;
        }
    }

    private void updateHudAnimations() {
        heartAnimationTicks++;
        coinAnimationTicks++;
    }

    private int getHeartAnimationFrame(int heartIndex) {
        long heartAge = Math.max(0L, heartAnimationTicks - (long) heartIndex * HEART_STAGGER_DELAY);
        int cycleDuration = HEART_FRAME_DURATION * (HEART_FRAME_COUNT - 1) + HEART_LAST_FRAME_PAUSE
                + HEART_FRAME_DURATION;
        int cyclePosition = (int) (heartAge % cycleDuration);

        if (cyclePosition < HEART_FRAME_DURATION) {
            return 0;
        }
        if (cyclePosition < HEART_FRAME_DURATION * 2) {
            return 1;
        }
        return HEART_FRAME_COUNT - 1;
    }

    private int getCoinAnimationFrame() {
        return (int) ((coinAnimationTicks / COIN_FRAME_DURATION) % COIN_FRAME_COUNT);
    }

    public void draw(GraphicsHandler graphicsHandler) {
        // based on screen state, draw appropriate graphics
        switch (playLevelScreenState) {
            case RUNNING:
                map.draw(graphicsHandler);
                player.draw(graphicsHandler);
                if(player.getShotgun() != null) { 
                    player.getShotgun().draw(graphicsHandler);
                    if(player.playerCanShootShotgun())
                        graphicsHandler.drawImage(ImageLoader.load("gunui.png"), 27, 100, 55, 25);
                    else
                        graphicsHandler.drawImage(ImageLoader.load("gunuireload.png"), 27, 100, 55, 25);
                    String ammoMode = player.getAmmoMode();
                    if (ammoMode.equals("single")) {
                        graphicsHandler.drawString("Single", 90, 120, new Font(Engine.Config.DEFAULT_FONT, Font.PLAIN, 20), Color.RED);
                    } else if (ammoMode.equals("twelve")) {
                        graphicsHandler.drawString("Twelve", 90, 120, new Font(Engine.Config.DEFAULT_FONT, Font.PLAIN, 20), Color.GREEN);
                    } else if (ammoMode.equals("cannon")) {
                        graphicsHandler.drawString("Cannon", 90, 120, new Font(Engine.Config.DEFAULT_FONT, Font.PLAIN, 20), Color.BLUE);
                    }
                }

                if (GamePanel.isHitboxesEnabled()) {
                    map.drawDebugHitboxes(graphicsHandler, player);
                }

                for (int i = 0; i < player.getHealth(); i++) {
                    graphicsHandler.drawImage(heartFrames[getHeartAnimationFrame(i)], 10 + (i * 40), 10,
                            HEART_DISPLAY_SIZE, HEART_DISPLAY_SIZE);
                }

                graphicsHandler.drawImage(coinFrames[getCoinAnimationFrame()], 10, 56,
                        COIN_DISPLAY_WIDTH, COIN_DISPLAY_HEIGHT);
                graphicsHandler.drawString(Integer.toString(player.getCoins()), 52, 83,
                        new Font(Engine.Config.DEFAULT_FONT, Font.PLAIN, 32), Color.WHITE);

                break;
            case LEVEL_COMPLETED:
                levelClearedScreen.draw(graphicsHandler);
                break;
            case LEVEL_LOSE:
                levelLoseScreen.draw(graphicsHandler);
                break;
        }
    }



    public PlayLevelScreenState getPlayLevelScreenState() {
        return playLevelScreenState;
    }

    @Override
    public void onLevelCompleted() {
        if (playLevelScreenState != PlayLevelScreenState.LEVEL_COMPLETED) {
            playLevelScreenState = PlayLevelScreenState.LEVEL_COMPLETED;
            levelCompletedStateChangeStart = true;
        }
    }

    @Override
    public void onDeath() {
        if (playLevelScreenState != PlayLevelScreenState.LEVEL_LOSE) {
            playLevelScreenState = PlayLevelScreenState.LEVEL_LOSE;
        }
    }

    public void resetLevel() {
		map.reset();
        initialize();
    }

    public void goBackToMenu() {
        screenCoordinator.setGameState(GameState.MENU);
    }

    // This enum represents the different states this screen can be in
    private enum PlayLevelScreenState {
        RUNNING, LEVEL_COMPLETED, LEVEL_LOSE
    }
}
