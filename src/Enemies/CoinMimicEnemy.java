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

public class CoinMimicEnemy extends Enemy implements IDamagable {
    private float flySpeed = 5f; //Speed of the CoinMimic when the player is in its aggro range
    private float aggroRadius = 200; // 35 x 35 pixels is the size of a tile, so 70 pixels is about 2 tiles away from the player
    private int health = 1;
    
    // timer is used to determine how long coin mimic freezes in place to transform into an enemy before chasing the player
    protected int transformTimer;

    protected CoinMimicState coinMimicState;

    public CoinMimicEnemy(Point location, Direction facingDirection) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("fakecoin.png"), 38, 43), "IDLE");
        this.initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        coinMimicState = CoinMimicState.IDLE;
        currentAnimationName = "IDLE";
        // the transform lasts exactly as long as the TRANSFORM animation (sum of its frame delays)
        transformTimer = 0;
        for (Frame frame : animations.get("TRANSFORM")) {
            transformTimer += frame.getDelay();
        }
    }

    @Override
    public void update(Player player) {
        float moveAmountX = 0;
        float moveAmountY = 0;

        if (transformTimer == 0 && coinMimicState == CoinMimicState.TRANSFORM) {
            coinMimicState = CoinMimicState.CHASE;
            currentAnimationName = "CHASE";
        }

        if (inRange(player) && coinMimicState == CoinMimicState.IDLE) {
            coinMimicState = CoinMimicState.TRANSFORM;
            currentAnimationName = "TRANSFORM";
        }

        if (coinMimicState == CoinMimicState.TRANSFORM) {
            transformTimer--;
        }

        if (coinMimicState == CoinMimicState.CHASE) {
            if (player.getX() > this.getX()) {
                moveAmountX += flySpeed;
            } else if (player.getX() < this.getX()) {
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


    // spritesheet layout (38x43 frames): 0-7 disguised coin spinning, 8-11 transformation, 12-13 revealed chase
    private static final int IDLE_START = 0, IDLE_FRAMES = 8, IDLE_DELAY = 12;
    private static final int TRANSFORM_START = 8, TRANSFORM_FRAMES = 4, TRANSFORM_DELAY = 15;
    private static final int CHASE_START = 12, CHASE_FRAMES = 2, CHASE_DELAY = 10;

    // hitboxes (x, y, width, height in sprite pixels) fitted to the art of each frame
    private static final int[] IDLE_BOUNDS = {6, 4, 26, 26};
    private static final int[][] TRANSFORM_BOUNDS = {{6, 4, 26, 26}, {5, 5, 28, 25}, {5, 3, 28, 29}, {2, 3, 34, 38}};
    private static final int[][] CHASE_BOUNDS = {{0, 1, 38, 41}, {1, 0, 36, 43}};

    private static Frame buildFrame(SpriteSheet spriteSheet, int frameIndex, int delay, int[] bounds) {
        return new FrameBuilder(spriteSheet.getSprite(0, frameIndex), delay)
                .withScale(1.4f)
                .withBounds(bounds[0], bounds[1], bounds[2], bounds[3])
                .build();
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet)
    {
        Frame[] idle = new Frame[IDLE_FRAMES];
        for (int i = 0; i < IDLE_FRAMES; i++) {
            idle[i] = buildFrame(spriteSheet, IDLE_START + i, IDLE_DELAY, IDLE_BOUNDS);
        }
        Frame[] transform = new Frame[TRANSFORM_FRAMES];
        for (int i = 0; i < TRANSFORM_FRAMES; i++) {
            transform[i] = buildFrame(spriteSheet, TRANSFORM_START + i, TRANSFORM_DELAY, TRANSFORM_BOUNDS[i]);
        }
        Frame[] chase = new Frame[CHASE_FRAMES];
        for (int i = 0; i < CHASE_FRAMES; i++) {
            chase[i] = buildFrame(spriteSheet, CHASE_START + i, CHASE_DELAY, CHASE_BOUNDS[i]);
        }

        return new HashMap<String, Frame[]>() {{
            put("IDLE", idle);
            put("TRANSFORM", transform);
            put("CHASE", chase);
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

    @Override 
    public void touchedPlayer(Player player) {
        player.killPlayer();
    }

    public enum CoinMimicState {
        IDLE, TRANSFORM, CHASE
    }
}