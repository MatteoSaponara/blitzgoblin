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
    private static final int FRAME_DELAY = 16;
    private static final int COIN_CHARGE_COOLDOWN_FRAMES = 25;
    private static final int HEART_REWARD_CHANCE_DENOMINATOR = 10;
    private int coinChargeCooldown;

    public SlotMachine(Point location) {
        super(location.x, location.y,
                new SpriteSheet(ImageLoader.load("slotMachine.png"), FRAME_WIDTH, FRAME_HEIGHT),
                TileType.PASSABLE);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        Frame[] frames = new Frame[3];
        for (int frameIndex = 0; frameIndex < frames.length; frameIndex++) {
            frames[frameIndex] = new FrameBuilder(
                    spriteSheet.getImage().getSubimage(frameIndex * FRAME_WIDTH, 0, FRAME_WIDTH, FRAME_HEIGHT),
                    FRAME_DELAY)
                    .withScale(1.4f)
                    .build();
        }

        HashMap<String, Frame[]> animations = new HashMap<>();
        animations.put("DEFAULT", frames);
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

        if (hitByShot && coinChargeCooldown == 0 && player.getCoins() > 0) {
            player.setCoins(player.getCoins() - 1);
            coinChargeCooldown = COIN_CHARGE_COOLDOWN_FRAMES;
            if (ThreadLocalRandom.current().nextInt(HEART_REWARD_CHANCE_DENOMINATOR) == 0) {
                player.setMaxHealth(player.getMaxHealth() + 1);
                player.setPlayerHealth(player.getHealth() + 1);
            }
        }
    }

    private boolean isPlayerProjectile(Enemy enemy) {
        return enemy instanceof GunPOne
                || enemy instanceof GunPTwelveShot
                || enemy instanceof GunPCannonBall;
    }
}
