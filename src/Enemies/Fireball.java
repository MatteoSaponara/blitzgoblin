package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.PhysicsParticle;
import GameObject.SpriteSheet;
import Level.Enemy;
import Level.MapEntity;
import Level.MapEntityStatus;
import Level.Player;
import Utils.Direction;
import Utils.Point;
import Interfaces.IDamagable;

import java.awt.Color;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

// This class is for fireballs shot by dinosaur and turret enemies
// it travels in a straight line (x axis) and disappears on a solid tile collision
public class Fireball extends Enemy implements IDamagable {
    private static final int EMBER_TRAIL_INTERVAL = 3;
    private float movementSpeed;
    private int existenceFrames;
    private final boolean hasExistenceLimit;
    private final Direction direction;
    private int health = 1;
    private int emberTrailFrame;

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
            if (mapEntityStatus == MapEntityStatus.ACTIVE
                    && ++emberTrailFrame >= EMBER_TRAIL_INTERVAL) {
                emberTrailFrame = 0;
                spawnEmber();
            }
        }
    }

    private void spawnEmber() {
        if (map == null) {
            return;
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();
        float directionX = direction == Direction.RIGHT ? 1 : -1;
        PhysicsParticle ember = new PhysicsParticle(
                getX() + getWidth() / 2f - directionX * (float) random.nextDouble(5, 9),
                getY() + getHeight() / 2f + (float) random.nextDouble(-3, 3),
                (float) random.nextDouble(5, 7),
                new Color(255, random.nextInt(90, 150), random.nextInt(10, 40), 230));
        ember.setVelocity(
                -directionX * (float) random.nextDouble(0.1, 0.4),
                (float) random.nextDouble(-0.5, 0.1));
        ember.setGravity(0, -0.01f);
        ember.setDrag(0.05f);
        ember.setLifetimeFrames(random.nextInt(28, 41));
        ember.setFadesOut(true);
        ember.setShrinksToNothing(true);
        map.addParticle(ember);
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
