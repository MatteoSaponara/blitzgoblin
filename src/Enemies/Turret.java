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

    // how many frames before firing that the turret switches to its charging pose
    protected final int CHARGE_DURATION = 20;

    public Turret(Point startLocation, Direction facingDirection) {
        // sprite is a single row of 2 frames, each 31x33 with 1px of padding between them
        // the art faces left by default, so the right-facing animations flip it
        super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("Turret.png"), 31, 33), "TURRET_LEFT");
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

        // switch to the charging pose as the shoot timer counts down towards firing, otherwise show the normal/shooting pose
        boolean isChargingUp = shootTimer <= CHARGE_DURATION;
        if (facingDirection == Direction.RIGHT) {
            currentAnimationName = isChargingUp ? "TURRET_CHARGE_RIGHT" : "TURRET_RIGHT";
        } else {
            currentAnimationName = isChargingUp ? "TURRET_CHARGE_LEFT" : "TURRET_LEFT";
        }

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
                fireballX = Math.round(getX());
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

            // frame 1 (column 0): normal/shooting pose
            put("TURRET_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                            .withScale(1.4f)
                            .withBounds(2, 2, 27, 29)
                            .build(),
            });

            put("TURRET_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                            .withScale(1.4f)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(2, 2, 27, 29)
                            .build(),
            });

            // frame 2 (column 1): charging pose
            put("TURRET_CHARGE_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                            .withScale(1.4f)
                            .withBounds(2, 2, 27, 29)
                            .build(),
            });

            put("TURRET_CHARGE_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                            .withScale(1.4f)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(2, 2, 27, 29)
                            .build(),
            });
        }};
    }

    public enum TurretState {
        TURRET
    }
}