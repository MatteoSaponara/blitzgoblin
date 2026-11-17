package EnhancedMapTiles;

import Builders.FrameBuilder;
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
import java.util.concurrent.ThreadLocalRandom;

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
    private static final int COIN_CHARGE_COOLDOWN_FRAMES = 25;
    private static final int HEART_REWARD_CHANCE_DENOMINATOR = 10;
    private int coinChargeCooldown;

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

        if (coinChargeCooldown > 0) {
            coinChargeCooldown--;
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

        if (hitByShot && coinChargeCooldown == 0 && player.getCoins() > 0) {
            player.setCoins(player.getCoins() - 1);
            coinChargeCooldown = COIN_CHARGE_COOLDOWN_FRAMES;
            if (ThreadLocalRandom.current().nextInt(HEART_REWARD_CHANCE_DENOMINATOR) == 0) {
                player.setMaxHealth(player.getMaxHealth() + 1);
                player.setPlayerHealth(player.getHealth() + 1);
            }
        }

        if (SHOT_ANIMATION.equals(getCurrentAnimationName()) && hasAnimationLooped) {
            setCurrentAnimationName(IDLE_ANIMATION);
        }
    }

    private boolean isPlayerProjectile(Enemy enemy) {
        return enemy instanceof GunPOne
                || enemy instanceof GunPTwelveShot
                || enemy instanceof GunPCannonBall;
    }
}
