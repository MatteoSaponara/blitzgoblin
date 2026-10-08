package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import Engine.Mouse;
import GameObject.Frame;
import GameObject.SpriteSheet;
//import Interfaces.IDamagable;
import Level.Enemy;
import Level.MapEntity;
import Level.MapEntityStatus;
import Level.Player;
import Utils.Direction;
import Utils.Point;

import java.util.HashMap;

public class GunPOne extends Enemy {
    private float movementSpeed;
    private float distanceTraveled;
    private float maxDistance;

    private float moveX;
    private float moveY;

    public GunPOne(Point spawnPoint, float targetX, float targetY) {
        super(spawnPoint.x-(3*4), spawnPoint.y-(3*4),
                new SpriteSheet(ImageLoader.load("Bullet1.png"), 7, 7),"DEFAULT");

        movementSpeed = 3f;
        maxDistance = 3000f;
        distanceTraveled = 0;

        float dx = targetX - spawnPoint.x;
        float dy = targetY - spawnPoint.y;

        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance != 0) {
            moveX = dx / distance * movementSpeed;
            moveY = dy / distance * movementSpeed;
        }

        initialize();
    }

    @Override
    public void update(Player player) {
        if (map == null) {
            return;
        }

        moveXHandleCollision(moveX);
        moveYHandleCollision(moveY);

        distanceTraveled += movementSpeed;

        for (Enemy enemy : map.getActiveEnemies()) {
            if (enemy != this &&
                    enemy.getMapEntityStatus() == MapEntityStatus.ACTIVE && this.intersects(enemy)) {
				/*
                if (enemy instanceof IDamagable) {
                    ((IDamagable) enemy).takeDamage(1);
                    this.mapEntityStatus = MapEntityStatus.REMOVED;
                    return;
                }
                */
                
                this.mapEntityStatus = MapEntityStatus.REMOVED;
				enemy.setMapEntityStatus(MapEntityStatus.REMOVED);
				return;
                
            }
        }

        if (distanceTraveled >= maxDistance) {
            this.mapEntityStatus = MapEntityStatus.REMOVED;
            return;
        }

        super.update();
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        if (hasCollided) {
            this.mapEntityStatus = MapEntityStatus.REMOVED;
        }
    }

    @Override
    public void onEndCollisionCheckY(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        if (hasCollided) {
            this.mapEntityStatus = MapEntityStatus.REMOVED;
        }
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(3)
                    .withBounds(1, 1, 5, 5)
                    .build()
            });
        }};
    }
}
