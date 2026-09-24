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

    public Cat(float x, float y) {
        super(new SpriteSheet(ImageLoader.load("Goblin.png"), 48, 52), x, y, "STAND_RIGHT");
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
            // first two frames of the sheet are used as a simple two-frame idle animation
            put("STAND_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 30)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 30)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build()
            });

            put("STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 30)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 30)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build()
            });

            // last four frames of the sheet are used as the walk cycle
            put("WALK_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build()
            });

            put("WALK_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build()
            });

            // everything below is just using the walk cycle as a placeholder for now

            put("JUMP_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build()
            });

            put("JUMP_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build()
            });

            put("FALL_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build()
            });

            put("FALL_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build()
            });

            put("CROUCH_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build()
            });

            put("CROUCH_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build()
            });

            put("DEATH_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build()
            });

            put("DEATH_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build()
            });

            put("SWIM_STAND_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 8)
                            .withScale(SPRITE_SCALE)
                            .withBounds(8, 6, 22, 42)
                            .build()
            });

            put("SWIM_STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 8)
                            .withScale(SPRITE_SCALE)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 6, 22, 42)
                            .build()
            });
        }};
    }
}