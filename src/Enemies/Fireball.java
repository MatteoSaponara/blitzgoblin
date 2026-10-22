package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
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
    private int health = 1;

    public Fireball(Point location, float movementSpeed, int existenceFrames) {
        this(location, movementSpeed, existenceFrames, true);
    }

    public Fireball(Point location, float movementSpeed) {
        this(location, movementSpeed, 0, false);
    }

    private Fireball(Point location, float movementSpeed, int existenceFrames, boolean hasExistenceLimit) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("Fireball.png"), 7, 7), "DEFAULT");
        this.movementSpeed = movementSpeed;
        this.existenceFrames = existenceFrames;
        this.hasExistenceLimit = hasExistenceLimit;

        initialize();
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
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build()
            });
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
