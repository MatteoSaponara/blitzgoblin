package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import Level.Enemy;
import Level.MapEntityStatus;
import Level.Player;
import Utils.Point;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class DeathAnimEnemies extends Enemy {
    private final int frameWidth;
    private final int frameHeight;
    private final int frameCount;
    private final int frameDelay;
    private final float scale;

    private int animationTimer = 0;

    public DeathAnimEnemies(Point location, String imageName, int frameWidth, int frameHeight, int frameCount, int frameDelay, float scale) {
        super(location.x, location.y, createAnimations(imageName, frameWidth, frameHeight, frameCount, frameDelay, scale), "DEATH");

        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frameCount = frameCount;
        this.frameDelay = frameDelay;
        this.scale = scale;

        initialize();
    }

    private static HashMap<String, Frame[]> createAnimations(String imageName, int frameWidth, int frameHeight, int frameCount, int frameDelay, float scale) {
        BufferedImage image = ImageLoader.load(imageName);
        Frame[] frames = new Frame[frameCount];

        for (int i = 0; i < frameCount; i++) {
            frames[i] = new FrameBuilder(image.getSubimage(i * frameWidth, 0, frameWidth, frameHeight), frameDelay)
                .withScale(scale)
                .withImageEffect(ImageEffect.NONE)
                .withBounds(0, 0, frameWidth, frameHeight)
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

        if (animationTimer >= frameDelay * frameCount) {
            mapEntityStatus = MapEntityStatus.REMOVED;
        }
    }
}
