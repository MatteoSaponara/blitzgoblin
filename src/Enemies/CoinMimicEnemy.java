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
    private float flySpeed = .5f; //Speed of the CoinMimic when the player is in its aggro range
    private float aggroRadius = 70; // 35 x 35 pixels is the size of a tile, so 70 pixels is about 2 tiles away from the player
    private boolean chasing = false;
    private int health = 1;

    public CoinMimicEnemy(Point location, Direction facingDirection) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("enemycoinmimic.png"), 48, 52), "WALK_LEFT");
        this.initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        currentAnimationName = "IDLE";
    }

    @Override
    public void update(Player player) {
        float moveAmountX = 0;
        float moveAmountY = 0;

        if (inRange(player)) {
            chasing = true;
            currentAnimationName = "TRANSFORM";
        }

        if (chasing) {
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


    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet)
    {
        return new HashMap<String, Frame[]>() {{
			put("IDLE", new Frame[] {
					new FrameBuilder(spriteSheet.getSprite(0, 0), 12)
						.withScale(3)
						.withBounds(1, 1, 10, 10)
						.build(),
					new FrameBuilder(spriteSheet.getSprite(0, 1), 12)
						.withScale(3)
						.withBounds(1, 1, 10, 10)
						.build(),
					new FrameBuilder(spriteSheet.getSprite(0, 2), 12)
						.withScale(3)
						.withBounds(1, 1, 10, 10)
						.build(),
					new FrameBuilder(spriteSheet.getSprite(0, 3), 12)
						.withScale(3)
						.withBounds(1, 1, 10, 10)
						.build(),
				new FrameBuilder(spriteSheet.getSprite(0, 4), 12)
						.withScale(3)
						.withBounds(1, 1, 10, 10)
					.build(),
				new FrameBuilder(spriteSheet.getSprite(0, 5), 12)
						.withScale(3)
						.withBounds(1, 1, 10, 10)
						.build(),
				new FrameBuilder(spriteSheet.getSprite(0, 6), 12)
						.withScale(3)
						.withBounds(1, 1, 10, 10)
						.build(),
				new FrameBuilder(spriteSheet.getSprite(0, 7), 12)
						.withScale(3)
						.withBounds(1, 1, 10, 10)
						.build()
				});
            put("TRANSFORM", new Frame[] { 

                });
            put("CHASE", new Frame[] { 

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

    @Override 
    public void touchedPlayer(Player player) {
        player.killPlayer();
    }
}
