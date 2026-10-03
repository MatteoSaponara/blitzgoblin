package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Interfaces.IDamagable;
import Level.Enemy;
import Level.MapEntity;
import Level.MapEntityStatus;
import Level.Player;
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;

import java.util.HashMap;

public class GargoyleEnemy extends Enemy implements IDamagable {
    private float flySpeed = .5f; //Speed of the Gargoyle when the player is in its aggro range
    private Direction startFacingDirection;
    private Direction facingDirection;
    private float aggroRadius = 280f; // 35 x 35 pixels is the size of a tile, so 280 pixels is about 8 tiles away from the player
    private boolean chasing = false;
    private int health = 3;

    public GargoyleEnemy(Point location, Direction facingDirection) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("enemygargoyle.png"), 48, 52), "WALK_LEFT");
        this.startFacingDirection = facingDirection;
        this.initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        facingDirection = startFacingDirection;
        if (facingDirection == Direction.RIGHT) {
            currentAnimationName = "FLY_RIGHT";
        } else if (facingDirection == Direction.LEFT) {
            currentAnimationName = "FLY_LEFT";
        }
    }

    @Override
    public void update(Player player) {
        float moveAmountX = 0;
        float moveAmountY = 0;

        if (inRange(player)) {
            chasing = true;
            if (player.getX() > this.getX()) {
                facingDirection = Direction.RIGHT;
                currentAnimationName = "WALK_RIGHT";
            }
            else if (player.getX() <= this.getX()) {
                facingDirection = Direction.LEFT;
                currentAnimationName = "WALK_LEFT";
            }
        }

        if (chasing) {
            if (facingDirection == Direction.RIGHT) {
                moveAmountX += flySpeed;
            } else if (facingDirection == Direction.LEFT) {
                moveAmountX -= flySpeed;
            }
            if (player.getY() > this.getY()) {
                moveAmountY += flySpeed;
            } else if (player.getY() < this.getY()) {
                moveAmountY -= flySpeed;
            }
        }

        // move bug
        moveYHandleCollision(moveAmountY);
        moveXHandleCollision(moveAmountX);

        super.update(player);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("FLY_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                            .withScale(1.5f)
                            .withBounds(0, 0, 48, 52)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                            .withScale(1.5f)
                            .withBounds(0, 0, 48, 52)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(1.5f)
                            .withBounds(0, 0, 48, 52)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(1.5f)
                            .withBounds(0, 0, 48, 52)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(1.5f)
                            .withBounds(0, 0, 48, 52)
                            .build()
            });

            put("FLY_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                            .withScale(1.5f)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(0, 0, 48, 52)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                            .withScale(1.5f)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(0, 0, 48, 52)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(1.5f)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(0, 0, 48, 52)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(1.5f)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(0, 0, 48, 52)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(1.5f)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(0, 0, 48, 52)
                            .build()
            });
        }};
    }

    public int getHealth() {
        return health;
    }
    
    public void takeDamage(int damage) {
        health -= damage;
        if (health < 1) {
            die();
        }
    }

    public void die() {
        this.mapEntityStatus = MapEntityStatus.REMOVED;
    }

    private boolean inRange(Player player) {
        float pX = player.getX();
        float pY = player.getY();
        if (Math.pow((pX - this.getX()),2) + Math.pow((pY - this.getY()),2) <= Math.pow(aggroRadius,2)) {
            return true;
        }
        return false;
    }
}
