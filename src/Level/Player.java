package Level;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Engine.Mouse;
import GameObject.GameObject;
import GameObject.Rectangle;
import GameObject.SpriteSheet;
import Utils.AirGroundState;
import Utils.Direction;

import java.awt.Color;
import java.util.ArrayList;

public abstract class Player extends GameObject
{
    // values that affect player movement
    // these should be set in a subclass
    protected float walkSpeed = 0;
    protected float gravity = 0;
    protected float jumpHeight = 0;
    protected float jumpDegrade = 0;
    protected float terminalVelocityY = 0;
    protected float momentumYIncrease = 0;

    // values used to handle player movement
    protected float jumpForce = 0;
    protected float momentumY = 0;
    protected float moveAmountX, moveAmountY;
    protected float lastAmountMovedX, lastAmountMovedY;

    // values used to keep track of player's current state
    protected PlayerState playerState;
    protected PlayerState previousPlayerState;
    protected Direction facingDirection;
    protected AirGroundState airGroundState;
    protected AirGroundState previousAirGroundState;
    protected LevelState levelState;

    // classes that listen to player events can be added to this list
    protected ArrayList<PlayerListener> listeners = new ArrayList<>();

    // define keys
    protected KeyLocker keyLocker = new KeyLocker();
    protected Key[] JUMP_KEYS = {Key.W, Key.SPACE};
    protected boolean jumpKeyLocked = false;
    protected Key MOVE_LEFT_KEY = Key.A;
    protected Key MOVE_RIGHT_KEY = Key.D;
    protected Key CROUCH_KEY = Key.S;

    // flags
    protected boolean isInvincible = false; // if true, player cannot be hurt by enemies (good for testing)

    // melee attack values
    protected boolean isAttacking = false;
    protected boolean attackButtonLocked = false;
    protected int attackTimer = 0;
    protected final int ATTACK_DURATION = 15;
    protected final int ATTACK_WIDTH = 35;
    protected Rectangle attackBox;

    // Aiming system variables
    protected double aimAngle = 0;
    protected double mousePosX = 0;
    protected double mousePosY = 0;

    public Player(SpriteSheet spriteSheet, float x, float y, String startingAnimationName)
    {
        super(spriteSheet, x, y, startingAnimationName);
        facingDirection = Direction.RIGHT;
        airGroundState = AirGroundState.AIR;
        previousAirGroundState = airGroundState;
        playerState = PlayerState.STANDING;
        previousPlayerState = playerState;
        levelState = LevelState.RUNNING;
    }

    public void update()
    {
        moveAmountX = 0;
        moveAmountY = 0;

        // if player is currently playing through level (has not won or lost)
        if (levelState == LevelState.RUNNING)
        {
            applyGravity();

            // Updates mouse aim angle and curser position coordinates.
            updateAiming();

            // update player's state and current actions, which includes things like determining how much it should move each frame and if its walking or jumping
            do
            {
                previousPlayerState = playerState;
                handlePlayerState();
            } while (previousPlayerState != playerState);

            previousAirGroundState = airGroundState;

            updateAttack();

            // move player with respect to map collisions based on how much player needs to move this frame
            lastAmountMovedX = super.moveXHandleCollision(moveAmountX);
            lastAmountMovedY = super.moveYHandleCollision(moveAmountY);

            handlePlayerAnimation();

            updateLockedKeys();

            // update player's animation
            super.update();
        }

        // if player has beaten level
        else if (levelState == LevelState.LEVEL_COMPLETED)
        {
            updateLevelCompleted();
        }

        // if player has lost level
        else if (levelState == LevelState.PLAYER_DEAD)
        {
            updatePlayerDead();
        }
    }

    // add gravity to player, which is a downward force
    protected void applyGravity()
    {
        moveAmountY += gravity + momentumY;
    }

    // based on player's current state, call appropriate player state handling method
    protected void handlePlayerState()
    {
        switch (playerState)
        {
            case STANDING:
                playerStanding();
                break;
            case WALKING:
                playerWalking();
                break;
            case CROUCHING:
                playerCrouching();
                break;
            case JUMPING:
                playerJumping();
                break;
        }
    }

    // player STANDING state logic
    protected void playerStanding()
    {
        // if walk left or walk right key is pressed, player enters WALKING state
        if (Keyboard.isKeyDown(MOVE_LEFT_KEY) || Keyboard.isKeyDown(MOVE_RIGHT_KEY))
        {
            playerState = PlayerState.WALKING;
        }

        // if jump key is pressed, player enters JUMPING state
        else if (Keyboard.isKeyDown(JUMP_KEYS) && !jumpKeyLocked)
        {
            jumpKeyLocked = true;
            playerState = PlayerState.JUMPING;
        }

        // if crouch key is pressed, player enters CROUCHING state
        else if (Keyboard.isKeyDown(CROUCH_KEY))
        {
            playerState = PlayerState.CROUCHING;
        }
    }

    // player WALKING state logic
    protected void playerWalking()
    {
        // if walk left key is pressed, move player to the left
        if (Keyboard.isKeyDown(MOVE_LEFT_KEY))
        {
            moveAmountX -= walkSpeed;
            facingDirection = Direction.LEFT;
        }

        // if walk right key is pressed, move player to the right
        else if (Keyboard.isKeyDown(MOVE_RIGHT_KEY))
        {
            moveAmountX += walkSpeed;
            facingDirection = Direction.RIGHT;
        } else if (Keyboard.isKeyUp(MOVE_LEFT_KEY) && Keyboard.isKeyUp(MOVE_RIGHT_KEY))
        {
            playerState = PlayerState.STANDING;
        }

        // if jump key is pressed, player enters JUMPING state
        if (Keyboard.isKeyDown(JUMP_KEYS) && !jumpKeyLocked)
        {
            jumpKeyLocked = true;
            playerState = PlayerState.JUMPING;
        }

        // if crouch key is pressed,
        else if (Keyboard.isKeyDown(CROUCH_KEY))
        {
            playerState = PlayerState.CROUCHING;
        }
    }

    // player CROUCHING state logic
    protected void playerCrouching()
    {
        // if crouch key is released, player enters STANDING state
        if (Keyboard.isKeyUp(CROUCH_KEY))
        {
            playerState = PlayerState.STANDING;
        }

        // if jump key is pressed, player enters JUMPING state
        if (Keyboard.isKeyDown(JUMP_KEYS) && !jumpKeyLocked)
        {
            jumpKeyLocked = true;
            playerState = PlayerState.JUMPING;
        }
    }

    // player JUMPING state logic
    protected void playerJumping()
    {
        // if last frame player was on ground and this frame player is still on ground, the jump needs to be setup
        if (previousAirGroundState == AirGroundState.GROUND && airGroundState == AirGroundState.GROUND)
        {

            // sets animation to a JUMP animation based on which way player is facing
            currentAnimationName = facingDirection == Direction.RIGHT ? "JUMP_RIGHT" : "JUMP_LEFT";

            // player is set to be in air and then player is sent into the air
            airGroundState = AirGroundState.AIR;
            jumpForce = jumpHeight;
            if (jumpForce > 0)
            {
                moveAmountY -= jumpForce;
                jumpForce -= jumpDegrade;
                if (jumpForce < 0)
                {
                    jumpForce = 0;
                }
            }
        }

        // if player is in air (currently in a jump) and has more jumpForce, continue sending player upwards
        else if (airGroundState == AirGroundState.AIR)
        {
            if (jumpForce > 0)
            {
                moveAmountY -= jumpForce;
                jumpForce -= jumpDegrade;
                if (jumpForce < 0)
                {
                    jumpForce = 0;
                }
            }

            // allows you to move left and right while in the air
            if (Keyboard.isKeyDown(MOVE_LEFT_KEY))
            {
                moveAmountX -= walkSpeed;
            } else if (Keyboard.isKeyDown(MOVE_RIGHT_KEY))
            {
                moveAmountX += walkSpeed;
            }

            // if player is falling, increases momentum as player falls so it falls faster over time
            if (moveAmountY > 0)
            {
                increaseMomentum();
            }
        }

        // if player last frame was in air and this frame is now on ground, player enters STANDING state
        else if (previousAirGroundState == AirGroundState.AIR && airGroundState == AirGroundState.GROUND)
        {
            playerState = PlayerState.STANDING;
        }
    }

    // while player is in air, this is called, and will increase momentumY by a set amount until player reaches terminal velocity
    protected void increaseMomentum()
    {
        momentumY += momentumYIncrease;
        if (momentumY > terminalVelocityY)
        {
            momentumY = terminalVelocityY;
        }
    }

    protected void updateLockedKeys()
    {
        // unlock jump once none of the jump keys are still being held down
        if (!Keyboard.isKeyDown(JUMP_KEYS))
        {
            jumpKeyLocked = false;
        }
    }

    // handles starting/updating/ending the melee attack based on left mouse button input
    protected void updateAttack()
    {
        if (Mouse.isLeftButtonDown() && !attackButtonLocked && !isAttacking)
        {
            attackButtonLocked = true;
            isAttacking = true;
            attackTimer = ATTACK_DURATION;
        }

        if (Mouse.isLeftButtonUp())
        {
            attackButtonLocked = false;
        }

        if (isAttacking)
        {
            updateAttackBoxLocation();
            checkAttackCollisions();
            attackTimer--;
            if (attackTimer <= 0)
            {
                isAttacking = false;
                attackBox = null;
            }
        }
    }

    // positions the attack hitbox right in front of the player based on which way it's facing
    protected void updateAttackBoxLocation()
    {
        Rectangle bounds = getBounds();
        float attackX = facingDirection == Direction.RIGHT ? bounds.getX2() + 1 : bounds.getX1() - ATTACK_WIDTH;
        float attackY = bounds.getY1();

        if (attackBox == null)
        {
            attackBox = new Rectangle(attackX, attackY, ATTACK_WIDTH, Math.round(bounds.getHeight()));
        } else
        {
            attackBox.setLocation(attackX, attackY);
        }
    }

    // kills any enemy currently touching the attack hitbox
    protected void checkAttackCollisions()
    {
        if (map == null || attackBox == null)
        {
            return;
        }

        for (Enemy enemy : map.getActiveEnemies())
        {
            if (enemy.getMapEntityStatus() == MapEntityStatus.ACTIVE && attackBox.intersects(enemy))
            {
                enemy.setMapEntityStatus(MapEntityStatus.REMOVED);
            }
        }
    }

    // anything extra the player should do based on interactions can be handled here
    protected void handlePlayerAnimation()
    {
        if (playerState == PlayerState.STANDING)
        {
            // sets animation to a STAND animation based on which way player is facing
            this.currentAnimationName = facingDirection == Direction.RIGHT ? "STAND_RIGHT" : "STAND_LEFT";

            // handles putting goggles on when standing in water
            // checks if the center of the player is currently touching a water tile
            int centerX = Math.round(getBounds().getX1()) + Math.round(getBounds().getWidth() / 2f);
            int centerY = Math.round(getBounds().getY1()) + Math.round(getBounds().getHeight() / 2f);
            MapTile currentMapTile = map.getTileByPosition(centerX, centerY);
            if (currentMapTile != null && currentMapTile.getTileType() == TileType.WATER)
            {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "SWIM_STAND_RIGHT" : "SWIM_STAND_LEFT";
            }
        } else if (playerState == PlayerState.WALKING)
        {
            // sets animation to a WALK animation based on which way player is facing
            this.currentAnimationName = facingDirection == Direction.RIGHT ? "WALK_RIGHT" : "WALK_LEFT";
        } else if (playerState == PlayerState.CROUCHING)
        {
            // sets animation to a CROUCH animation based on which way player is facing
            this.currentAnimationName = facingDirection == Direction.RIGHT ? "CROUCH_RIGHT" : "CROUCH_LEFT";
        } else if (playerState == PlayerState.JUMPING)
        {
            // if player is moving upwards, set player's animation to jump. if player moving downwards, set player's animation to fall
            if (lastAmountMovedY <= 0)
            {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "JUMP_RIGHT" : "JUMP_LEFT";
            } else
            {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "FALL_RIGHT" : "FALL_LEFT";
            }
        }
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith)
    {
    }

    @Override
    public void onEndCollisionCheckY(boolean hasCollided, Direction direction, MapEntity entityCollidedWith)
    {
        // if player collides with a map tile below it, it is now on the ground
        // if player does not collide with a map tile below, it is in air
        if (direction == Direction.DOWN)
        {
            if (hasCollided)
            {
                momentumY = 0;
                airGroundState = AirGroundState.GROUND;
            } else
            {
                playerState = PlayerState.JUMPING;
                airGroundState = AirGroundState.AIR;
            }
        }

        // if player collides with map tile upwards, it means it was jumping and then hit into a ceiling -- immediately stop upwards jump velocity
        else if (direction == Direction.UP)
        {
            if (hasCollided)
            {
                jumpForce = 0;
            }
        }
    }

    // other entities can call this method to hurt the player
    public void hurtPlayer(MapEntity mapEntity)
    {
        if (!isInvincible)
        {
            // if map entity is an enemy, kill player on touch
            if (mapEntity instanceof Enemy)
            {
                levelState = LevelState.PLAYER_DEAD;
            }
        }
    }

    // other entities can call this to tell the player they beat a level
    public void completeLevel()
    {
        levelState = LevelState.LEVEL_COMPLETED;
    }

    // if player has beaten level, this will be the update cycle
    public void updateLevelCompleted()
    {
        // if player is not on ground, player should fall until it touches the ground
        if (airGroundState != AirGroundState.GROUND && map.getCamera().containsDraw(this))
        {
            currentAnimationName = "FALL_RIGHT";
            applyGravity();
            increaseMomentum();
            super.update();
            moveYHandleCollision(moveAmountY);
        }
        // move player to the right until it walks off screen
        else if (map.getCamera().containsDraw(this))
        {
            currentAnimationName = "WALK_RIGHT";
            super.update();
            moveXHandleCollision(walkSpeed);
        } else
        {
            // tell all player listeners that the player has finished the level
            for (PlayerListener listener : listeners)
            {
                listener.onLevelCompleted();
            }
        }
    }

    // if player has died, this will be the update cycle
    public void updatePlayerDead()
    {
        // change player animation to DEATH
        if (!currentAnimationName.startsWith("DEATH"))
        {
            if (facingDirection == Direction.RIGHT)
            {
                currentAnimationName = "DEATH_RIGHT";
            } else
            {
                currentAnimationName = "DEATH_LEFT";
            }
            super.update();
        }
        // if death animation not on last frame yet, continue to play out death animation
        else if (currentFrameIndex != getCurrentAnimation().length - 1)
        {
            super.update();
        }
        // if death animation on last frame (it is set up not to loop back to start), player should continually fall until it goes off screen
        else if (currentFrameIndex == getCurrentAnimation().length - 1)
        {
            if (map.getCamera().containsDraw(this))
            {
                moveY(3);
            } else
            {
                // tell all player listeners that the player has died in the level
                for (PlayerListener listener : listeners)
                {
                    listener.onDeath();
                }
            }
        }
    }

    public PlayerState getPlayerState()
    {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState)
    {
        this.playerState = playerState;
    }

    public AirGroundState getAirGroundState()
    {
        return airGroundState;
    }

    public Direction getFacingDirection()
    {
        return facingDirection;
    }

    public void setFacingDirection(Direction facingDirection)
    {
        this.facingDirection = facingDirection;
    }

    public void setLevelState(LevelState levelState)
    {
        this.levelState = levelState;
    }

    public void addListener(PlayerListener listener)
    {
        listeners.add(listener);
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler)
    {
        super.draw(graphicsHandler);
        if (isAttacking && attackBox != null)
        {
            float cameraX = map != null ? map.getCamera().getX() : 0;
            float cameraY = map != null ? map.getCamera().getY() : 0;
            graphicsHandler.drawFilledRectangle(
                    Math.round(attackBox.getX() - cameraX),
                    Math.round(attackBox.getY() - cameraY),
                    attackBox.getWidth(),
                    attackBox.getHeight(),
                    new Color(255, 0, 0, 128)
            );
        }
    }

    protected void updateAiming() {
        if (map == null) return;

        // Convert mouse screen position to world coordinates using Camera
        float cameraX = map.getCamera().getX();
        float cameraY = map.getCamera().getY();
        mousePosX = Mouse.getMouseX() + cameraX;
        mousePosY = Mouse.getMouseY() + cameraY;

        // Get player center in screen coordinates
        Rectangle bounds = getBounds();
        float playerScreenX = bounds.getX1() + (bounds.getWidth() / 2f) - cameraX;
        float playerScreenY = bounds.getY1() + (bounds.getHeight() / 2f) - cameraY;

        // Calculate angle from player to mouse cursor
        aimAngle = Mouse.getAngleTo(playerScreenX, playerScreenY);
    }

    // Uncomment this to have game draw player's bounds to make it easier to visualize
    /*
    public void drawBoundsForDebug(GraphicsHandler graphicsHandler) {
        drawBounds(graphicsHandler, new Color(255, 0, 0, 100));
    }
    */
}