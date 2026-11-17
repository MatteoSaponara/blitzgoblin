package EnhancedMapTiles;

import Engine.Config;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.ImageEffect;
import Level.Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

final class SlotMachineDisplay {
    private static final int COIN_FRAME_SIZE = 26;
    private static final int COIN_FRAME_HEIGHT = 30;
    private static final int SPIN_DURATION = Config.TARGET_FPS;
    private static final int RESULT_DISPLAY_DURATION = Config.TARGET_FPS * 2;
    private static final int FLICKER_INTERVAL = 3;
    private static final int COIN_FRAME_DURATION = 12;
    private static final int COIN_X = 31;
    private static final int COIN_Y = 31;
    private static final int HEART_X = 42;
    private static final int HEART_Y = 33;
    private static final int HEART_SIZE = 28;
    private static final int TEXT_X = 60;
    private static final int TEXT_Y = 53;
    private static final BufferedImage[] COIN_FRAMES = loadCoinFrames();
    private static final BufferedImage HEART_ICON =
            ImageLoader.loadSubImage("heart.png", 0, 0, 30, 30);
    private static final Outcome[] OUTCOMES = Outcome.values();

    enum Outcome {
        NOTHING(null, 9) {
            @Override
            void award(Player player) {
            }
        },
        EXTRA_HEART(HEART_ICON, 1) {
            @Override
            void award(Player player) {
                player.setMaxHealth(player.getMaxHealth() + 1);
                player.setPlayerHealth(player.getHealth() + 1);
            }
        };

        private final BufferedImage icon;
        private final int weight;

        Outcome(BufferedImage icon, int weight) {
            this.icon = icon;
            this.weight = weight;
        }

        abstract void award(Player player);
    }

    private enum State {
        COST,
        SPINNING,
        RESULT
    }

    private State state = State.COST;
    private int coinAnimationTicks;
    private int spinTicks;
    private int resultTicks;
    private boolean resultFramePending;
    private Outcome selectedOutcome;

    void startSpin() {
        beginSpin(chooseOutcome());
    }

    boolean canGamble() {
        return state != State.SPINNING && !resultFramePending;
    }

    private void beginSpin(Outcome outcome) {
        selectedOutcome = outcome;
        this.spinTicks = 0;
        this.resultTicks = 0;
        this.state = State.SPINNING;
    }

    Outcome update() {
        coinAnimationTicks++;
        if (state == State.SPINNING && ++spinTicks >= SPIN_DURATION) {
            state = State.RESULT;
            resultFramePending = true;
            return selectedOutcome;
        }
        if (state == State.RESULT) {
            if (resultFramePending) {
                resultFramePending = false;
            }
            if (++resultTicks >= RESULT_DISPLAY_DURATION) {
                state = State.COST;
            }
        }
        return null;
    }

    void draw(GraphicsHandler graphicsHandler, int machineX, int machineY) {
        switch (state) {
            case COST:
                drawCost(graphicsHandler, machineX, machineY);
                break;
            case SPINNING:
                drawOutcome(graphicsHandler, machineX, machineY,
                        OUTCOMES[(spinTicks / FLICKER_INTERVAL) % OUTCOMES.length]);
                break;
            case RESULT:
                drawOutcome(graphicsHandler, machineX, machineY, selectedOutcome);
                break;
            default:
                throw new IllegalStateException("Unknown slot machine display state: " + state);
        }
    }

    private void drawCost(GraphicsHandler graphicsHandler, int machineX, int machineY) {
        BufferedImage coin = COIN_FRAMES[(coinAnimationTicks / COIN_FRAME_DURATION) % COIN_FRAMES.length];
        drawImage(graphicsHandler, coin, machineX + COIN_X, machineY + COIN_Y,
                COIN_FRAME_SIZE, COIN_FRAME_HEIGHT);
        graphicsHandler.drawString("x1", toWorld(machineX + TEXT_X), toWorld(machineY + TEXT_Y),
                new Font(Config.DEFAULT_FONT, Font.PLAIN, 24), Color.BLACK);
    }

    private void drawOutcome(GraphicsHandler graphicsHandler, int machineX, int machineY, Outcome outcome) {
        if (outcome.icon != null) {
            drawImage(graphicsHandler, outcome.icon,
                    machineX + HEART_X, machineY + HEART_Y, HEART_SIZE, HEART_SIZE);
        }
    }

    private void drawImage(GraphicsHandler graphicsHandler, BufferedImage image,
                           int x, int y, int width, int height) {
        if (graphicsHandler.isPixelGridEnabled()) {
            graphicsHandler.drawPixelImage(image, x, y, x + width, y + height,
                    ImageEffect.NONE);
        } else {
            graphicsHandler.drawImage(image,
                    toWorld(x), toWorld(y), toWorld(width), toWorld(height));
        }
    }

    private int toWorld(int pixelGridCoordinate) {
        return Math.round(pixelGridCoordinate * Config.PIXEL_SCALE);
    }

    private Outcome chooseOutcome() {
        int totalWeight = 0;
        for (Outcome outcome : OUTCOMES) {
            totalWeight += outcome.weight;
        }

        int roll = ThreadLocalRandom.current().nextInt(totalWeight);
        for (Outcome outcome : OUTCOMES) {
            if (roll < outcome.weight) {
                return outcome;
            }
            roll -= outcome.weight;
        }
        throw new IllegalStateException("No slot machine outcome selected");
    }

    private static BufferedImage[] loadCoinFrames() {
        BufferedImage[] frames = new BufferedImage[8];
        for (int frame = 0; frame < frames.length; frame++) {
            frames[frame] = ImageLoader.loadSubImage("hudcoin.png",
                    frame * (COIN_FRAME_SIZE + 1), 0, COIN_FRAME_SIZE, COIN_FRAME_HEIGHT);
        }
        return frames;
    }
}
