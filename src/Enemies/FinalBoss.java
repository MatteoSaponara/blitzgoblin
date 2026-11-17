package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Interfaces.IDamagable;
import Interfaces.IArmored;
import Level.Enemy;
import Level.MapEntity;
import Level.MapEntityStatus;
import Level.Player;
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;
import java.util.HashMap;
import Enemies.DeathAnimEnemies;

public class FinalBoss extends Enemy implements IDamagable{

    private int movementSpeed = 2; // Set the movement speed of the Final Boss
    private Direction movementDirection; // Current movement direction of the Final Boss
    private int health = 100; // Set the initial health of the Final Boss
    private float aggroRadius = 210f; // The aggro radius of the Final Boss

    // Chance thresholds for changing boss behavior
    private float changeDirectionChance = 0.1f; // Chance to change direction randomly
    private float attackChance = 0.05f; // Chance to do an attack


    protected FinalBossState finalBossState; // Current state of the Final Boss
    protected int transformTimer; // Timer for the transformation state of the Final Boss

    public FinalBoss(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("FinalBoss.png"), 64, 64), "IDLE_LEFT");
        this.movementDirection = Direction.LEFT; 
        this.initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        finalBossState = FinalBossState.IDLE; // Set the initial state of the Final Boss
        movementSpeed = 2; // Set the movement speed of the Final Boss
        movementDirection = Direction.LEFT; // Set the initial movement direction
        for (Frame frame : animations.get("TRANSFORM")) {
            transformTimer += frame.getDelay();
        }
    }

    @Override 
    public void update(Player player) {
        if (finalBossState == FinalBossState.IDLE) {
            if (inRange(player)) {
                finalBossState = FinalBossState.TRANSFORMING;
                currentAnimationName = "TRANSFORM";
            }
            return; // If the Final Boss is dead, skip the update
        }

        if (finalBossState == FinalBossState.TRANSFORMING) {
            transformTimer--;
            if (transformTimer <= 0) {
                finalBossState = FinalBossState.MOVING;
                currentAnimationName = "MOVE_LEFT";
            }
            return; // If the Final Boss is dead, skip the update
        }

        float moveAmountX = 0; // Horizontal movement amount
        
        float distanceX = (player.getX1() + player.getX2()) / 2f - (getX1() + getX2()) / 2f; // Calculate the horizontal distance between the Final Boss and the player
        
        Direction playerDirection = distanceX > 0 ? Direction.RIGHT : Direction.LEFT; // Determine the direction of the player relative to the Final Boss

        float directionChance = changeDirectionChance; // Chance to change direction randomly

        if (playerDirection != movementDirection) {
           directionChance += 0.2f; // Increase chance to change direction if the player is in the opposite direction
        }

        if (Math.random() < directionChance) {
            movementDirection = playerDirection; // Change direction towards the player
            currentAnimationName = movementDirection == Direction.LEFT ? "MOVE_LEFT" : "MOVE_RIGHT"; // Update animation based on direction
        }

        if (Math.random() < attackChance) {
            finalBossState = FinalBossState.SPAWNING;
            currentAnimationName = "SPAWN";
        }

        // Move the Final Boss based on its movement direction
        if (movementDirection == Direction.LEFT) {
            this.setX(this.getX() - movementSpeed);
        } else if (movementDirection == Direction.RIGHT) {
            this.setX(this.getX() + movementSpeed);
        }

        moveXHandleCollision(moveAmountX);

        super.update(player);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            
        }};
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            die();
        }
    }

    public void die() {
        // Handle death logic here
        // For example, you might want to play a death animation or remove the enemy from the game
        System.out.println("Final Boss has been defeated!");
    }

    private boolean inRange(Player player) {
        float pX = player.getX();
        float pY = player.getY();
        if (Math.pow((pX - this.getX()),2) + Math.pow((pY - this.getY()),2) <= Math.pow(aggroRadius,2)) {
            return true;
        }
        return false;
    }

    public enum FinalBossState {
        IDLE,
        TRANSFORMING,
        MOVING,
        SPAWNING,

        DEAD
    }
}
