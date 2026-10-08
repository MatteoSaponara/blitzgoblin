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

public class GoblinEnemy extends Enemy implements IDamagable{
    private float gravity = .5f;
    private float movementSpeed;
    private float walkSpeed = .5f;
    private float chaseSpeed = 2f; // Speed of Goblin when the player is in its aggro range
    private Direction startFacingDirection;
    private Direction facingDirection;
    private AirGroundState airGroundState;
    private float aggroRadius = 175f; // 35x35 pixels is the size of a tile, so 175 pixels is about 5 tiles away from the player
    private boolean chasing = false;
    private boolean moving = true;
    private int health = 3;

    public GoblinEnemy (Point location, Direction facingDirection) {
        // Change BugEnemy.png to GoblinEnemy.png when the sprite is ready
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("enemygoblin.png"), 48, 52), "WALK_LEFT");
        this.startFacingDirection = facingDirection;
        this.initialize();
    }

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
        float moveAmountX = 0;
        float moveAmountY = 0;

        // add gravity (if in air, this will cause bug to fall)
        moveAmountY += gravity;

        chasing = inRange(player);
        float distanceX = (player.getX1() + player.getX2()) / 2f - (getX1() + getX2()) / 2f;

        // chasing: face the player, and stop once lined up with them (avoids flipping back and forth over their position)
        if (chasing) {
            movementSpeed = chaseSpeed;
            if (Math.abs(distanceX) > movementSpeed) {
                facingDirection = distanceX > 0 ? Direction.RIGHT : Direction.LEFT;
                moving = true;
            } else {
                moving = false;
            }
        } else {
            movementSpeed = walkSpeed;
            moving = true;
        }

        // if on ground, walk forward based on facing direction
        if (airGroundState == AirGroundState.GROUND && moving) {
            if (facingDirection == Direction.RIGHT) {
                moveAmountX += movementSpeed;
            } else {
                moveAmountX -= movementSpeed;
            }
        }

        // move bug
        moveYHandleCollision(moveAmountY);
        moveXHandleCollision(moveAmountX);

        // idle animation when standing still (or in the air), walk animation when moving
        boolean walking = moving && airGroundState == AirGroundState.GROUND;
        currentAnimationName = (walking ? "WALK_" : "IDLE_") + (facingDirection == Direction.RIGHT ? "RIGHT" : "LEFT");

        super.update(player);
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction,  MapEntity entityCollidedWith) {
        // if bug has collided into something while walking forward,
        // it turns around (changes facing direction)
        if (hasCollided) {
            facingDirection = direction == Direction.RIGHT ? Direction.LEFT : Direction.RIGHT;
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

    // spritesheet layout (48x52 frames): 0-1 idle, 2-5 walk (art faces right; left is the flipped version)
    private static final int IDLE_START = 0, IDLE_FRAMES = 2, IDLE_DELAY = 24;
    private static final int WALK_START = 2, WALK_FRAMES = 4, WALK_DELAY = 8;

    private static Frame[] buildFrames(SpriteSheet spriteSheet, int start, int count, int delay, ImageEffect effect) {
        Frame[] frames = new Frame[count];
        for (int i = 0; i < count; i++) {
            frames[i] = new FrameBuilder(spriteSheet.getSprite(0, start + i), delay)
                    .withScale(1.4f)
                    .withImageEffect(effect)
                    .withBounds(0, 0, 48, 52)
                    .build();
        }
        return frames;
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("IDLE_RIGHT", buildFrames(spriteSheet, IDLE_START, IDLE_FRAMES, IDLE_DELAY, ImageEffect.NONE));
            put("IDLE_LEFT", buildFrames(spriteSheet, IDLE_START, IDLE_FRAMES, IDLE_DELAY, ImageEffect.FLIP_HORIZONTAL));
            put("WALK_RIGHT", buildFrames(spriteSheet, WALK_START, WALK_FRAMES, WALK_DELAY, ImageEffect.NONE));
            put("WALK_LEFT", buildFrames(spriteSheet, WALK_START, WALK_FRAMES, WALK_DELAY, ImageEffect.FLIP_HORIZONTAL));
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