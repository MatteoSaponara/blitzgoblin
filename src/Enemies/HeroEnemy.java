package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.Rectangle;
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
import Interfaces.IDamagable;

// This class is for the hero enemy
// 
// 
public class HeroEnemy extends Enemy implements IDamagable {
    private float gravity = .5f;
    private float movementSpeed = .5f;
    private Direction startFacingDirection;
    private Direction facingDirection;
    private AirGroundState airGroundState;
    private int health = 1;
    private static final int HIT_DURATION = 60;
    private static final int SHAKE_DURATION = 12;
    private HeroState heroState = HeroState.IDLE;
    private int hitTimer;
    private int shakeTimer;

    public HeroEnemy(Point location, int numLayerIN, Direction facingDirection) {
        super(location.x, location.y, numLayerIN, new SpriteSheet(ImageLoader.load("hero.png"), 84, 80), "WALK_LEFT");
        this.startFacingDirection = facingDirection;
        this.initialize();
    }
    public HeroEnemy(Point location, Direction facingDirection) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("hero.png"), 91, 85), "WALK_LEFT");
        this.startFacingDirection = facingDirection;
        this.initialize();
    }
    /*public int getHealth() {

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
        map.addEnemy(new DeathAnimEnemies(new Point(getX(), getY())));
    */
    @Override
    public void initialize() {
        super.initialize();
        facingDirection = startFacingDirection;
        if (facingDirection == Direction.RIGHT) {
            currentAnimationName = "WALK_RIGHT";
        } else if (facingDirection == Direction.LEFT) {
            currentAnimationName = "WALK_LEFT";
        }
        airGroundState = AirGroundState.GROUND;
    }

    @Override
    public void update(Player player) {
        setX(startPositionX);

        float moveAmountX = 0;
        float moveAmountY = 0;

        /*// add gravity (if in air, this will cause bug to fall)
        moveAmountY += gravity;

        // if on ground, walk forward based on facing direction
        if (airGroundState == AirGroundState.GROUND) {
            if (facingDirection == Direction.RIGHT) {
                moveAmountX += movementSpeed;
            } else {
                moveAmountX -= movementSpeed;
            }
        } */

        // move bug
        moveYHandleCollision(moveAmountY);
        moveXHandleCollision(moveAmountX); 

        if (heroState == HeroState.HIT) {
            if (hitTimer == 0) {
                heroState = HeroState.REWARDED;
                currentAnimationName = facingDirection == Direction.RIGHT ? "REWARDED_RIGHT" : "REWARDED_LEFT";
                player.pickupShotgun();
                player.unlockSingleAmmo();
            } else {
                hitTimer--;
            }
        }

        blockPlayerUntilReward(player);
        super.update(player);

        if (heroState == HeroState.HIT && shakeTimer > 0) {
            int shakeOffset = shakeTimer % 4 < 2 ? -2 : 2;
            setX(startPositionX + shakeOffset);
            shakeTimer--;
        }
    }

    @Override
    public void touchedPlayer(Player player) {
    }

    private void blockPlayerUntilReward(Player player) {
        if (heroState == HeroState.REWARDED) {
            return;
        }

        Rectangle playerBounds = player.getBounds();
        Rectangle heroBounds = getBounds();
        if (playerBounds.getY2() <= heroBounds.getY1()
                || playerBounds.getY1() >= heroBounds.getY2()
                || playerBounds.getX2() <= heroBounds.getX1()
                || playerBounds.getX1() >= heroBounds.getX2()) {
            return;
        }

        float playerCenterX = (playerBounds.getX1() + playerBounds.getX2()) / 2;
        float heroCenterX = (heroBounds.getX1() + heroBounds.getX2()) / 2;
        if (playerCenterX < heroCenterX) {
            float rightInset = player.getX2() - playerBounds.getX2();
            player.setX(heroBounds.getX1() - player.getWidth() + rightInset);
        } else {
            float leftInset = playerBounds.getX1() - player.getX();
            player.setX(heroBounds.getX2() + 1 - leftInset);
        }
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction,  MapEntity entityCollidedWith) {
        // if bug has collided into something while walking forward,
        // it turns around (changes facing direction)
        if (hasCollided) {
            if (direction == Direction.RIGHT) {
                facingDirection = Direction.LEFT;
                currentAnimationName = "WALK_LEFT";
            } else {
                facingDirection = Direction.RIGHT;
                currentAnimationName = "WALK_RIGHT";
            }
        }
    }

    @Override
    public void onEndCollisionCheckY(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // if bug is colliding with the ground, change its air ground state to GROUND
        // if it is not colliding with the ground, it means that it's currently in the air, so its air ground state is changed to AIR
        if (direction == Direction.DOWN) {
            if (hasCollided) {
                airGroundState = AirGroundState.GROUND;
            } else {
                airGroundState = AirGroundState.AIR;
            }
        }
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("WALK_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(1.4f)
                            .withBounds(0, 0, 84, 80)
                            .build()
            });

            put("WALK_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(1.4f)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(0, 0, 84, 80)
                            .build()
            });

            put("HIT_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 1))
                            .withScale(1.4f)
                            .withBounds(0, 0, 84, 80)
                            .build()
            });

            put("HIT_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 1))
                            .withScale(1.4f)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(0, 0, 84, 80)
                            .build()
            });

            put("REWARDED_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 2))
                            .withScale(1.4f)
                            .withBounds(0, 0, 84, 80)
                            .build()
            });

            put("REWARDED_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 2))
                            .withScale(1.4f)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(0, 0, 84, 80)
                            .build()
            });
        }};
    }
    
    public int getHealth() {
        return health;
    }
    
    public void takeDamage(int damage) {
        if (heroState != HeroState.IDLE || damage <= 0) {
            return;
        }

        startDamageFlash();
        health = Math.max(0, health - damage);
        if (health == 0) {
            die();
        }
    }

    public void die() {
        if (heroState != HeroState.IDLE) {
            return;
        }

        health = 0;
        heroState = HeroState.HIT;
        hitTimer = HIT_DURATION;
        shakeTimer = SHAKE_DURATION;
        currentAnimationName = facingDirection == Direction.RIGHT ? "HIT_RIGHT" : "HIT_LEFT";
    }

    private enum HeroState {
        IDLE, HIT, REWARDED
    }
}
