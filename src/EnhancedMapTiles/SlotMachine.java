package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Enemies.GunPCannonBall;
import Enemies.GunPOne;
import Enemies.GunPTwelveShot;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.Enemy;
import Level.MapEntityStatus;
import Level.Player;
import Level.TileType;
import Utils.Point;
import java.util.HashMap;

public class SlotMachine extends EnhancedMapTile {
    private static final int FRAME_WIDTH = 134;
    private static final int FRAME_HEIGHT = 150;
    private static final int IDLE_FRAME_DELAY = 12;
    private static final int SHOT_FRAME_DELAY = 8;
    private static final float TARGET_BOUNDS_X = 100;
    private static final float TARGET_BOUNDS_Y = 104;
    private static final int TARGET_BOUNDS_WIDTH = 34;
    private static final int TARGET_BOUNDS_HEIGHT = 34;
    private static final String IDLE_ANIMATION = "DEFAULT";
    private static final String SHOT_ANIMATION = "SHOT";
    private final SlotMachineDisplay display = new SlotMachineDisplay();

    public SlotMachine(Point location) {
        super(location.x, location.y,
                new SpriteSheet(ImageLoader.load("slotMachine.png"), FRAME_WIDTH, FRAME_HEIGHT),
                TileType.PASSABLE);
        setDrawsBehindEnemies(true);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        Frame[] idleFrames = new Frame[3];
        for (int frameIndex = 0; frameIndex < idleFrames.length; frameIndex++) {
            idleFrames[frameIndex] = new FrameBuilder(
                    spriteSheet.getImage().getSubimage(frameIndex * FRAME_WIDTH, 0, FRAME_WIDTH, FRAME_HEIGHT),
                    IDLE_FRAME_DELAY)
                    .withScale(1.4f)
                    .withBounds(TARGET_BOUNDS_X, TARGET_BOUNDS_Y, TARGET_BOUNDS_WIDTH, TARGET_BOUNDS_HEIGHT)
                    .build();
        }

        Frame[] shotFrames = new Frame[3];
        for (int frameIndex = 0; frameIndex < shotFrames.length; frameIndex++) {
            shotFrames[frameIndex] = new FrameBuilder(
                    spriteSheet.getImage().getSubimage((frameIndex + 3) * FRAME_WIDTH, 0, FRAME_WIDTH, FRAME_HEIGHT),
                    SHOT_FRAME_DELAY)
                    .withScale(1.4f)
                    .withBounds(TARGET_BOUNDS_X, TARGET_BOUNDS_Y, TARGET_BOUNDS_WIDTH, TARGET_BOUNDS_HEIGHT)
                    .build();
        }

        HashMap<String, Frame[]> animations = new HashMap<>();
        animations.put(IDLE_ANIMATION, idleFrames);
        animations.put(SHOT_ANIMATION, shotFrames);
        return animations;
    }

    @Override
    public void update(Player player) {
        super.update(player);
        SlotMachineDisplay.Outcome completedOutcome = display.update();
        if (completedOutcome != null) {
            completedOutcome.award(player);
        }

        boolean hitByShot = false;
        for (Enemy enemy : map.getEnemies()) {
            if (isPlayerProjectile(enemy)
                    && enemy.getMapEntityStatus() == MapEntityStatus.ACTIVE
                    && intersects(enemy)) {
                enemy.setMapEntityStatus(MapEntityStatus.REMOVED);
                hitByShot = true;
            }
        }

        if (hitByShot) {
            setCurrentAnimationName(SHOT_ANIMATION);
        }

        if (hitByShot && display.canGamble() && player.getCoins() > 0) {
            player.setCoins(player.getCoins() - 1);
            display.startSpin();
        }

        if (SHOT_ANIMATION.equals(getCurrentAnimationName()) && hasAnimationLooped) {
            setCurrentAnimationName(IDLE_ANIMATION);
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
        float cameraX = map == null ? 0 : map.getCamera().getX();
        float cameraY = map == null ? 0 : map.getCamera().getY();
        int screenX;
        int screenY;
        if (graphicsHandler.isPixelGridEnabled() && map != null) {
            screenX = GraphicsHandler.toPixelGrid(getX())
                    - GraphicsHandler.toPixelGrid(cameraX);
            screenY = GraphicsHandler.toPixelGrid(getY())
                    - GraphicsHandler.toPixelGrid(cameraY);
        } else {
            screenX = GraphicsHandler.toPixelGrid(Math.round(getX() - cameraX));
            screenY = GraphicsHandler.toPixelGrid(Math.round(getY() - cameraY));
        }
        display.draw(graphicsHandler, screenX, screenY);
    }

    private boolean isPlayerProjectile(Enemy enemy) {
        return enemy instanceof GunPOne
                || enemy instanceof GunPTwelveShot
                || enemy instanceof GunPCannonBall;
    }
}
