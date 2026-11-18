package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Interfaces.IDamagable;
import Interfaces.IArmored;
import Level.Enemy;
import Level.MapEntity;
import Level.MapEntityStatus;
import Level.Player;
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;
import java.util.HashMap;

public class FinalBossHand extends Enemy{
    public FinalBossHand(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("FinalBossHand.png"), 64, 64), "IDLE");
        this.initialize();
    }

    @Override 
    public void initialize() {
        super.initialize();
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            
        }};
    }
}
