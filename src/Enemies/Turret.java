package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.MapEntity;
import Level.Player;
import Utils.Direction;
import Utils.Point;
import Level.Enemy;

import java.util.HashMap;

public class Turret extends Enemy{

    // start and end location defines the two points that it walks between
    // is only made to walk along the x axis and has no air ground state logic, so make sure both points have the same Y value
    protected Point startLocation;

    private Direction startFacingDirection;
    protected Direction facingDirection;

    // timer is used to determine when a fireball is to be shot out
    protected int shootTimer;

    public Turret(Point startLocation, Direction facingDirection) {
        super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("DinosaurEnemy.png"), 14, 17), "TURRET");
        this.startLocation = startLocation;
        this.startFacingDirection = facingDirection;
        this.initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        facingDirection = startFacingDirection;

        // every certain number of frames, the fireball will be shot out
        shootTimer = 130;
    }

    @Override
    public void update(Player player) {

        // this is for actually having the dinosaur spit out the fireball
        if (shootTimer == 0) {
            // define where fireball will spawn on map (x location) relative to dinosaur enemy's location
            // and define its movement speed
            int fireballX;
            float movementSpeed;
            if (facingDirection == Direction.RIGHT) {
                fireballX = Math.round(getX()) + getWidth();
                movementSpeed = 1.5f;
            } else {
                fireballX = Math.round(getX() - 21);
                movementSpeed = -1.5f;
            }

            // define where fireball will spawn on the map (y location) relative to dinosaur enemy's location
            int fireballY = Math.round(getY()) + 4;

            // create Fireball enemy
            Fireball fireball = new Fireball(new Point(fireballX, fireballY), movementSpeed, 60);

            // add fireball enemy to the map for it to spawn in the level
            map.addEnemy(fireball);

            // reset shoot wait timer so the process can happen again (dino walks around, then waits, then shoots)
            shootTimer = 130;
        }
        else {
            shootTimer--;
        }
        super.update(player);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
    
        put("TURRET", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                            .withScale(3)
                            .withBounds(4, 2, 5, 13)
                            .build(),
        });
        }};
    }

    public enum TurretState {
        TURRET
    }
}
