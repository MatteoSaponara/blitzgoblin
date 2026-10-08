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
import Level.MapTile;
import Level.TileType;
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
            // move toward the player's center, never further than the remaining distance so it stops on them instead of jittering
            float distanceX = (player.getX1() + player.getX2()) / 2f - (getX1() + getX2()) / 2f;
            float distanceY = (player.getY1() + player.getY2()) / 2f - (getY1() + getY2()) / 2f;
            moveAmountX = Math.signum(distanceX) * Math.min(flySpeed, Math.abs(distanceX));
            moveAmountY = Math.signum(distanceY) * Math.min(flySpeed, Math.abs(distanceY));
        }

        // a coin placed in or on the ground rises out of it as it transforms, instead of being shoved to a tile edge
        if (coinMimicState != CoinMimicState.IDLE) {
            riseOutOfSolidTiles();
        }

        // only run collision handling when actually moving; otherwise the changing hitbox
        // shape can overlap a tile and push the coin out of place while it transforms
        if (moveAmountY != 0) {
            moveYHandleCollision(moveAmountY);
        }
        if (moveAmountX != 0) {
            moveXHandleCollision(moveAmountX);
        }

        super.update(player);
    }


    private static final float SPRITE_SCALE = 1.4f;

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
                .withScale(SPRITE_SCALE)
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

    // lifts the mimic 2px per update while the revealed form's hitbox would overlap a solid tile, so it has
    // already risen clear of the ground by the time it starts chasing (instead of being snapped to a tile edge)
    private void riseOutOfSolidTiles() {
        for (int i = 0; i < 2 && overlapsSolidTile(); i++) {
            moveY(-1);
        }
    }

    private boolean overlapsSolidTile() {
        if (map == null) {
            return false;
        }
        int[] revealed = CHASE_BOUNDS[0];
        float scale = SPRITE_SCALE;
        GameObject.Rectangle box = new GameObject.Rectangle(
                getX() + revealed[0] * scale, getY() + revealed[1] * scale,
                Math.round(revealed[2] * scale), Math.round(revealed[3] * scale));

        int tileWidth = map.getTileset().getScaledSpriteWidth();
        int tileHeight = map.getTileset().getScaledSpriteHeight();
        Point first = map.getTileIndexByPosition(box.getX1(), box.getY1());
        int columns = (int) Math.ceil(box.getWidth() / (float) tileWidth) + 1;
        int rows = (int) Math.ceil(box.getHeight() / (float) tileHeight) + 1;
        for (int row = 0; row <= rows; row++) {
            for (int column = 0; column <= columns; column++) {
                MapTile tile = map.getMapTile(Math.round(first.x) + column, Math.round(first.y) + row);
                if (tile != null && tile.getTileType() == TileType.NOT_PASSABLE && box.intersects(tile)) {
                    return true;
                }
            }
        }
        return false;
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