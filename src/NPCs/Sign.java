package NPCs;

import Builders.FrameBuilder;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Engine.ImageLoader;
import Level.NPC;
import Utils.Point;

import java.util.HashMap;

// This class is for signs NPCs
public class Sign extends NPC {

    public Sign(Point location, String imageName, String message) {
        super(location.x, location.y,
                new SpriteSheet(ImageLoader.load(imageName), 62, 62), "Sign");

        isInteractable = true;
        talkedToTime = 200;
        textbox.setText(message);
        textboxOffsetX = -60;
        textboxOffsetY = -34;
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("Sign", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(1)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .build()
            });
        }};
    }
}
