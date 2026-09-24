// This is Goblin.java now.

package Players;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.Player;

import java.util.HashMap;

// This is the class for the Cat player character
// basically just sets some values for physics and then defines animations
public class Cat extends Player {

    // controls the display scale of all the goblin's animation frames -- change this to resize the goblin globally
    // must be static since it's read while this object's animations are being loaded in, before its instance fields are set up
    public static float SPRITE_SCALE = 1.4f;

    // hurtbox used for every animation frame, in unscaled spritesheet pixel coordinates
    private static final int BOUNDS_X = 12;
    private static final int BOUNDS_Y = 6;
    private static final int BOUNDS_WIDTH = 33;
    private static final int BOUNDS_HEIGHT = 44;

    public Cat(float x, float y) {
        // updated spritesheet is a single row of 12 frames, each 73x54 with 1px of padding between them
        super(new SpriteSheet(ImageLoader.load("Goblin.png"), 73, 54), x, y, "STAND_RIGHT");
        gravity = .5f;
        terminalVelocityY = 6f;
        jumpHeight = 14.5f;
        jumpDegrade = .5f;
        walkSpeed = 2.3f;
        momentumYIncrease = .5f;
    }

    public void update() {
        super.update();
    }

    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
        // drawBounds(graphicsHandler, new Color(255, 0, 0, 170));
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            // frames 1-2 (columns 0-1): simple two-frame idle animation
            put("STAND_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 30)
                            .withScale(SPRITE_SCALE)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 30)
                            .withScale(SPRITE_SCALE)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build()
            });

            put("STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 30)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 30)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build()
            });

            // frame 3 (column 2): single-frame crouch pose
            put("CROUCH_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 0)
                            .withScale(SPRITE_SCALE)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build()
            });

            put("CROUCH_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 0)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build()
            });

            // frames 4-5 (columns 3-4): mouse-triggered melee attack animation
            put("ATTACK_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 7)
                            .withScale(SPRITE_SCALE)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 7)
                            .withScale(SPRITE_SCALE)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build()
            });

            put("ATTACK_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 7)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 7)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build()
            });

            // frames 6-9 (columns 5-8): four-frame walk cycle
            put("WALK_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 6), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 7), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 8), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build()
            });

            put("WALK_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 6), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 7), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 8), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build()
            });

            // frame 10 (column 9): single-frame jump pose
            put("JUMP_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 9), 0)
                            .withScale(SPRITE_SCALE)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build()
            });

            put("JUMP_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 9), 0)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build()
            });

            // frame 11 (column 10): single-frame fall pose
            put("FALL_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 10), 0)
                            .withScale(SPRITE_SCALE)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build()
            });

            put("FALL_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 10), 0)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build()
            });

            // frame 12 (column 11): single-frame death pose
            put("DEATH_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 11), 0)
                            .withScale(SPRITE_SCALE)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build()
            });

            put("DEATH_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 11), 0)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build()
            });

            // no dedicated swimming frames on the new spritesheet yet, so fall back to the idle pose for now
            put("SWIM_STAND_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 30)
                            .withScale(SPRITE_SCALE)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 30)
                            .withScale(SPRITE_SCALE)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build()
            });

            put("SWIM_STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 30)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 30)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(BOUNDS_X, BOUNDS_Y, BOUNDS_WIDTH, BOUNDS_HEIGHT)
                            .build()
            });
        }};
    }
}