
package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import Engine.GamePanel;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Interfaces.IDamagable;
import Level.Enemy;
import Level.MapEntityStatus;
import Level.Player;
import Utils.Direction;
import Utils.Point;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class BlockMimicEnemy extends Enemy implements IDamagable {
    private float flySpeed = 10f;
    private float aggroRadius = 280;
    private int health = 1;
    protected int transformTimer;
    protected BlockMimicState blockMimicState;
    
    public BlockMimicEnemy(Point location, int numLayerIN, int gamePlayLayerIN, Direction facingDirection) {
        super(location.x, location.y, numLayerIN, gamePlayLayerIN, new SpriteSheet(ImageLoader.load("MimicBlockTest14.png"), 59, 59), "IDLE");
        this.initialize();
    }
    public BlockMimicEnemy(Point location, Direction facingDirection) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("MimicBlockTest14.png"), 59, 59), "IDLE");
        this.initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        blockMimicState = BlockMimicState.IDLE;
        currentAnimationName = "IDLE";
        transformTimer = 0;
        for (Frame frame : animations.get("TRANSFORM")) {
            transformTimer += frame.getDelay();
        }
    }

    @Override
    public void update(Player player) {
        float moveAmountX = 0;
        float moveAmountY = 0;

        if (transformTimer == 0 && blockMimicState == BlockMimicState.TRANSFORM) {
            blockMimicState = BlockMimicState.CHASE;
            currentAnimationName = "CHASE";
        }

        if (inRange(player) && blockMimicState == BlockMimicState.IDLE) {
            blockMimicState = BlockMimicState.TRANSFORM;
            currentAnimationName = "TRANSFORM";
        }

        if (blockMimicState == BlockMimicState.TRANSFORM) {
            transformTimer--;
        }

        if (blockMimicState == BlockMimicState.CHASE) {
            float distanceX = (player.getX1() + player.getX2()) / 2f - (getX1() + getX2()) / 2f;
            float distanceY = (player.getY1() + player.getY2()) / 2f - (getY1() + getY2()) / 2f;
            moveAmountX = Math.signum(distanceX) * Math.min(flySpeed, Math.abs(distanceX));
            moveAmountY = Math.signum(distanceY) * Math.min(flySpeed, Math.abs(distanceY));
        }

        if (blockMimicState != BlockMimicState.IDLE) {
            riseOutOfSolidTiles();
        }

        if (moveAmountY != 0) {
            moveYHandleCollision(moveAmountY);
        }
        if (moveAmountX != 0) {
            moveXHandleCollision(moveAmountX);
        }

        super.update(player);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        BufferedImage idleImage = spriteSheet.getImage()
                .getSubimage(0, 0, 59, 59)
                .getSubimage(5, 5, 49, 49);

        Frame[] idle = new Frame[] {
            new FrameBuilder(idleImage).build()
        };

        Frame[] transform = new Frame[4];
        for (int i = 0; i < 4; i++) {
            BufferedImage frameImage = spriteSheet.getImage()
                    .getSubimage((i + 1) * 59, 0, 59, 59);
            transform[i] = new FrameBuilder(frameImage, 8).build();
        }

        BufferedImage chaseImage = spriteSheet.getImage()
                .getSubimage(5 * 59, 0, 59, 59);

        Frame[] chase = new Frame[] {
            new FrameBuilder(chaseImage, 10).build()
        };

        return new HashMap<String, Frame[]>() {{
            put("IDLE", idle);
            put("TRANSFORM", transform);
            put("CHASE", chase);
        }};
    }

    public int getHealth() { return health; }

    public void takeDamage(int damage) {
        if (damage > 0) startDamageFlash();
        health -= damage;
        if (health < 1) die();
    }

    public void die() {
        this.mapEntityStatus = MapEntityStatus.REMOVED;
        getMap().addEnemy(new DeathAnimEnemies(new Point(getX(), getY()), "DeathAnimationEnemies.png", 32, 32, 4, 8, 1.4f));
    }

    private void riseOutOfSolidTiles() {
        // Copy the original CoinMimicEnemy implementation here.
    }

    private boolean overlapsSolidTile() {
        // Copy the original CoinMimicEnemy implementation here.
        return false;
    }

    private boolean inRange(Player player) {
        float pX = player.getX1();
        float pY = player.getY1();
        return Math.pow((pX - this.getX()), 2) + Math.pow((pY - this.getY()), 2) <= Math.pow(aggroRadius, 2);
    }

    @Override
    public void touchedPlayer(Player player) {
        GamePanel.triggerBlockMimicJumpscare();
        player.killPlayer();
        this.mapEntityStatus = MapEntityStatus.REMOVED;
    }

    public enum BlockMimicState { IDLE, TRANSFORM, CHASE }
}
