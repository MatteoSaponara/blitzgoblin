package Enemies;
import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.MapEntity;
import Level.MapEntityStatus;
import Level.Player;
import Utils.Direction;
import Utils.Point;
import Level.Enemy;
import java.util.ArrayList;
import java.util.HashMap;
import EnhancedMapTiles.DoorBlock;

import Level.EnhancedMapTile;
import Interfaces.IDamagable;

public class Lever extends Enemy implements IDamagable{
    protected Point startLocation;
    
    //private int health;

    private ArrayList<EnhancedMapTile> enhancedMapTiles;

    public Lever(Point startLocation) {
        // sprite is a single row of 2 frames, each 31x33 with 1px of padding between them
        // the art faces left by default, so the right-facing animations flip it
        super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("Turret.png"), 31, 33), "TURRET_LEFT");
        this.startLocation = startLocation;
        this.initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    public void addDoorBlock(EnhancedMapTile tile) {
        if (tile instanceof DoorBlock) {
            enhancedMapTiles.add(tile);
        }
    }

    public int getHealth() {
        return 0;
    }

    public void takeDamage(int damage) {
        System.out.println("Lever has been hit");
        for (EnhancedMapTile tile : enhancedMapTiles) {
            if (tile instanceof DoorBlock) {
                ((DoorBlock) tile).switchDoorState();
            }
        }
    }

    public void die() {
        //Fuck you
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build()
            });
        }};
    }
    
}
