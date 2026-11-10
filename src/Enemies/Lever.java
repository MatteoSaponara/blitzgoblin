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
    protected float leverCooldown = 1f;
    protected float leverCooldownTimer = 0;

    //private int health;

    private ArrayList<EnhancedMapTile> enhancedMapTiles;
    public ArrayList<EnhancedMapTile> getEnhancedMapTiles() {return enhancedMapTiles;}

    public Lever(Point startLocation) {
        // sprite is a single row of 2 frames, each 31x33 with 1px of padding between them
        // the art faces left by default, so the right-facing animations flip it
        super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("Turret.png"), 31, 33), "DEFAULT");
        this.startLocation = startLocation;
        this.setIsUpdateOffScreen(true);
        this.initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        lastTime = System.nanoTime();
        currentTime = System.nanoTime();
        enhancedMapTiles = new ArrayList<EnhancedMapTile>();
    }
    long lastTime, currentTime;
    

    public void addDoorBlock(EnhancedMapTile tile) {
        if (tile instanceof DoorBlock) {
            enhancedMapTiles.add(tile);
        }
    }

    public int getHealth() {
        return 0;
    }
    private boolean isLeverOnCooldown() {
        return ((currentTime-lastTime)/1000000000.0) < leverCooldown;
    }
    public void takeDamage(int damage) {
        System.out.println("Lever has been hit");
        currentTime = System.nanoTime();
        if(isLeverOnCooldown())
            return;
        else lastTime = currentTime;
        System.out.println("flipping door");
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
                            .withScale(1)
                            .withBounds(1, 1, 27, 27)
                            .build()
            });
        }};
    }
    
}
