package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.Enemy;
import Level.MapEntityStatus;
import Level.Player;
import Utils.Point;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class DeathAnimEnemies extends Enemy {

    private static final int FRAME_WIDTH = 32;
    private static final int FRAME_HEIGHT = 32;
    private static final int FRAME_COUNT = 4;
    private static final int FRAME_DELAY = 8;
    private static final float SCALE = 1.4f;

    private int animationTimer = 0;

    public DeathAnimEnemies(Point location) {
		
		super(location.x, location.y, new SpriteSheet(ImageLoader.load("DeathAnimationEnemies.png"), FRAME_WIDTH, FRAME_HEIGHT),"DEATH");

        initialize();
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(
        SpriteSheet spriteSheet
    ) {
        BufferedImage image = spriteSheet.getImage();
        Frame[] frames = new Frame[FRAME_COUNT];

        for (int i = 0; i < FRAME_COUNT; i++) {
            frames[i] = new FrameBuilder(
                image.getSubimage(
                    i * FRAME_WIDTH,
                    0,
                    FRAME_WIDTH,
                    FRAME_HEIGHT
                ),
                FRAME_DELAY
            )
                .withScale(SCALE)
                .withImageEffect(ImageEffect.NONE)
                .withBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT)
                .build();
        }

        HashMap<String, Frame[]> animations = new HashMap<>();
        animations.put("DEATH", frames);

        return animations;
    }

    @Override
    public void update(Player player) {
        super.update(player);

        animationTimer++;

        if (animationTimer >= FRAME_DELAY * FRAME_COUNT) {
            mapEntityStatus = MapEntityStatus.REMOVED;
        }
    }
}
