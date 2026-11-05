package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.Enemy;
import Level.MapEntity;
import Level.MapEntityStatus;
import Level.Player;
import Utils.Direction;
import Utils.Point;
import Interfaces.IDamagable;

import java.util.HashMap;

// This class is for fireballs shot by dinosaur and turret enemies
// it travels in a straight line (x axis) and disappears on a solid tile collision
public class Fireball extends Enemy implements IDamagable {
    private float movementSpeed;
    private int existenceFrames;
    private final boolean hasExistenceLimit;
    private final Direction direction;
    private int health = 1;

    public Fireball(Point location, float movementSpeed, int existenceFrames) {
        this(location, movementSpeed, existenceFrames, true, getDirection(movementSpeed));
    }

    public Fireball(Point location, float movementSpeed) {
        this(location, movementSpeed, 0, false, getDirection(movementSpeed));
    }

    public Fireball(Point location, Direction direction, float movementSpeed) {
        this(location, movementSpeed, 0, false, direction);
    }

    private Fireball(Point location, float movementSpeed, int existenceFrames, boolean hasExistenceLimit, Direction direction) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("Fireball.png"), 30, 30), "DEFAULT");
        this.direction = direction;
        this.movementSpeed = direction == Direction.RIGHT ? Math.abs(movementSpeed) : -Math.abs(movementSpeed);
        this.existenceFrames = existenceFrames;
        this.hasExistenceLimit = hasExistenceLimit;
        ImageEffect imageEffect = direction == Direction.RIGHT ? ImageEffect.FLIP_HORIZONTAL : ImageEffect.NONE;
        for (Frame frame : animations.get("DEFAULT")) {
            frame.setImageEffect(imageEffect);
        }

        initialize();
    }

    private static Direction getDirection(float movementSpeed) {
        return movementSpeed < 0 ? Direction.LEFT : Direction.RIGHT;
    }

    @Override
    public void update(Player player) {
        if (hasExistenceLimit && existenceFrames == 0) {
            this.mapEntityStatus = MapEntityStatus.REMOVED;
        } else {
            // move fireball forward
            moveXHandleCollision(movementSpeed);
            super.update(player);
            if (hasExistenceLimit) {
                existenceFrames--;
            }
        }
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // if fireball collides with anything solid on the x axis, it is removed
        if (hasCollided) {
            this.mapEntityStatus = MapEntityStatus.REMOVED;
        }
    }

    @Override
    public void touchedPlayer(Player player) {
        // if fireball touches player, it disappears
        super.touchedPlayer(player);
        this.mapEntityStatus = MapEntityStatus.REMOVED;
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        Frame[] frames = new Frame[3];
        for (int frameIndex = 0; frameIndex < frames.length; frameIndex++) {
            frames[frameIndex] = new FrameBuilder(
                    spriteSheet.getImage().getSubimage(
                            frameIndex * spriteSheet.getSpriteWidth(),
                            0,
                            spriteSheet.getSpriteWidth(),
                            spriteSheet.getSpriteHeight()),
                            10)
                            .withScale(1.4f)
                            .withBounds(10, 10, 10, 10)
                            .build();
        }

        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", frames);
        }};
    }
    
    public int getHealth() {
        return health;
    }
    
    public void takeDamage(int damage) {
        if (damage > 0) {
            startDamageFlash();
        }
        health -= damage;
        if (health < 1) {
            die();
     
        }
    }
    public void die() {
        this.mapEntityStatus = MapEntityStatus.REMOVED;
        getMap().addEnemy(new DeathAnimEnemies(new Point(getX(), getY()), "DeathAnimationEnemies2.png", 32, 32, 4, 6, 1f));
    }
    
    
}
