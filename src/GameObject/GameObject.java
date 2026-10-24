package GameObject;

import Engine.GraphicsHandler;
import Engine.ScreenManager;
import Game.ScreenCoordinator;
import Level.*;
import Utils.Direction;
import Utils.ImageUtils;
import Utils.MathUtils;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.HashMap;

/*
	The all important GameObject class is what every "entity" used in this game should be based off of
	It encapsulates all the other class logic in the GameObject package to be a "one stop shop" for all entity needs
	This includes:
	1. displaying an image (as a sprite) to represent the entity
	2. animation logic for the sprite
	3. collision detection with a map
	4. performing proper draw logic based on camera movement
 */
public class GameObject extends AnimatedSprite {

    // stores game object's start position
    // important to keep track of this as it's what allows the special draw logic to work
    protected float startPositionX, startPositionY;

    // how much game object's position has changed from start position over time
    // also important to keep track of for the special draw logic
    protected float amountMovedX, amountMovedY;

    // previous location the game object was in from the last frame
    protected float previousX, previousY;

    // the map instance this game object "belongs" to.
    protected Map map;
    protected int numLayer = 0;
    public int distanceFromGameplayLayer = 0;

    // true for map tiles: stretch between grid positions of the edges so neighbours never gap or overlap
    protected boolean sharedEdgeDraw = false;

    protected SpriteSheet darkSpriteSheet;
    protected HashMap<String, Frame[]> animationsDark;
    float baseDarknessValue = 0.65f; //initial factor/value a tile layer behind the gameplay one is darkened by
    protected boolean hasTransparentPixels = false;
    public boolean getHasTransparentPixels(){ return hasTransparentPixels;}
    
    private BufferedImage darkenImage(BufferedImage bufferedImageIN, float factor){
        BufferedImage outputImage = copyImage(bufferedImageIN);
        int[] outputImageDataBuffer = ((DataBufferInt)outputImage.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < outputImage.getHeight(); i++) {
            for (int j = 0; j < outputImage.getWidth(); j++) {
                if(((outputImageDataBuffer[(i * (outputImage.getWidth())) + j]) >>> 6) > 0x00) 
                    outputImageDataBuffer[(i * (outputImage.getWidth())) + j] = (adjustBrightnessOfHexColor(((outputImageDataBuffer[(i * (outputImage.getWidth())) + j])-0xFF000000), factor) + 0xFF000000
                );
                else
                    this.hasTransparentPixels = true;
            }    
        }
        return outputImage;
    }

    public GameObject(SpriteSheet spriteSheet, int numLayerIN, int gameplayLayerIN, float x, float y, String startingAnimation) {
        super(spriteSheet, x, y, startingAnimation);
        numLayer = numLayerIN;
        distanceFromGameplayLayer = numLayerIN-gameplayLayerIN;
        darkSpriteSheet = new SpriteSheet(darkenImage(GameObject.copyImage(spriteSheet.getImage()), baseDarknessValue - Math.clamp(.10f*
            (distanceFromGameplayLayer), 0, baseDarknessValue))
    ,spriteSheet.getSpriteWidth()
    ,spriteSheet.getSpriteHeight());
        animationsDark = loadAnimations(darkSpriteSheet);

        this.startPositionX = x;
        this.startPositionY = y;
        this.previousX = x;
        this.previousY = y;
    }

    public GameObject(float x, float y, int numLayerIN, int gameplayLayerIN, HashMap<String, Frame[]> animations, String startingAnimation) {
        super(x, y, animations, startingAnimation);
        numLayer = numLayerIN;
        if(true){
            animationsDark = new HashMap<String, Frame[]>();
            distanceFromGameplayLayer = numLayerIN-gameplayLayerIN;
            for (String animName: animations.keySet()) {
                Frame[] animationFrames = new Frame[animations.get(animName).length];
                for (Frame animFrame: animations.get(animName)) {
                    for (int j = 0; j < animations.get(animName).length; j++) {
                        animationFrames[j] = new Frame(darkenImage(animFrame.copy().getImage(),baseDarknessValue - Math.clamp(.10f*
            (distanceFromGameplayLayer),0, baseDarknessValue)));
                        //animationsDark.get(animName)[j].setImage(darkenImage(animationsDark.get(animName)[j].getImage()));
                    }
                }
                animationsDark.put(animName, animationFrames);
            }
        }
        this.startPositionX = x;
        this.startPositionY = y;
        this.previousX = x;
        this.previousY = y;
    }

    public GameObject(float x, float y, Frame[] frames) {
        super(x, y, frames);
        this.startPositionX = x;
        this.startPositionY = y;
        this.previousX = x;
        this.previousY = y;
    }

    public GameObject(float x, float y, Frame frame) {
        super(x, y, frame);
        //this.animationsDark = new HashMap<String, Frame[]>();
        //this.animationsDark.put(currentAnimationName, new Frame[]{new Frame(darkenImage(frame.getImage(), baseDarknessValue - Math.clamp(10f*
        //    (numLayer-map.getCurrentGameplayLayer()), 100-baseDarknessValue, 0)))} );
        this.startPositionX = x;
        this.startPositionY = y;
        this.previousX = x;
        this.previousY = y;
    }

    public GameObject(float x, float y) {
        super(x, y, new Frame(ImageUtils.createSolidImage(new Color(255, 0, 255)), ImageEffect.NONE, 1, null));
        this.startPositionX = x;
        this.startPositionY = y;
        this.previousX = x;
        this.previousY = y;
    }

    @Override
    public void update() {
        // update previous position to be the current position
        previousX = x;
        previousY = y;

        // call to animation logic
        super.update();
    }

    // move game object along the x axis
    // will stop object from moving based on map collision logic (such as if it hits a solid tile)
    public float moveXHandleCollision(float dx) {
        if (map != null) {
            return handleCollisionX(dx);
        } else {
            super.moveX(dx);
            return dx;
        }
    }

    // move game object along the y axis
    // will stop object from moving based on map collision logic (such as if it hits a solid tile)
    public float moveYHandleCollision(float dy) {
        if (map != null) {
            return handleCollisionY(dy);
        } else {
            super.moveY(dy);
            return dy;
        }
    }

    // performs collision check logic for moving along the x axis against the map's tiles
    private float handleCollisionX(float moveAmountX) {
        // determines amount to move (whole number)
        int amountToMove = (int) Math.abs(moveAmountX);


        // gets decimal remainder from amount to move
        float moveAmountXRemainder = MathUtils.getRemainder(moveAmountX);

        // determines direction that will be moved in based on if moveAmountX is positive or negative
        Direction direction = moveAmountX < 0 ? Direction.LEFT : Direction.RIGHT;

        // moves game object one pixel at a time until total move amount is reached
        // if at any point a map tile collision is determined to have occurred from the move, adjust player position to right in front of the "solid" map tile's position, and stop attempting to move further
        // there is special logic to handle movement across slope map tiles
        float amountMoved = 0;
        boolean hasCollided = false;
        MapEntity entityCollidedWith = null;
        for (int i = 0; i < amountToMove; i++) {
            // determines if player is in proximity with a slope (needed for later if moving down a slope)
            SlopeProximityStatus slopeProximityStatus = MapCollisionHandler.getCurrentSlopeProximityStatus(this, map, direction);

            // move player in intended direction
            moveX(direction.getVelocity());

            // adjust x position if a collision occurred
            MapCollisionCheckResult collisionCheckResult = MapCollisionHandler.getAdjustedPositionAfterCollisionCheckX(this, map, direction);
            if (collisionCheckResult.getAdjustedLocation() != null) {
                hasCollided = true;
                entityCollidedWith = collisionCheckResult.getEntityCollidedWith();
                setX(collisionCheckResult.getAdjustedLocation().x);
            }

            // adjust y position if moving down a slope
            MapCollisionCheckResult slopeCollisionMovingDownCheckResult = MapCollisionHandler.getAdjustedPositionAfterCollisionSlopeCheckX(this, map, slopeProximityStatus);
            if (slopeCollisionMovingDownCheckResult.getAdjustedLocation() != null) {
                setY(slopeCollisionMovingDownCheckResult.getAdjustedLocation().y);
            }

            // adjust y position if moving up a slope
            MapCollisionCheckResult slopeCollisionMovingUpCheckResult = MapCollisionHandler.getAdjustedPositionAfterCollisionSlopeCheckY(this, map);
            if (slopeCollisionMovingUpCheckResult.getAdjustedLocation() != null) {
                setY(slopeCollisionMovingUpCheckResult.getAdjustedLocation().y);
            }

            if (hasCollided) {
                break;
            }

            amountMoved = i + 1;
        }

        // if no collision occurred in the above steps, this deals with the decimal remainder from the original move amount (stored in moveAmountXRemainder)
        // it starts by moving the game object by that decimal amount
        // it then does one more check for a collision in the case that this added decimal amount was enough to change the rounding and move the game object to the next pixel over
        // if a collision occurs from this move, the player is moved back to right in front of the "solid" map tile's position
        // there is special logic to handle movement across slope map tiles
        SlopeProximityStatus slopeProximityStatus = MapCollisionHandler.getCurrentSlopeProximityStatus(this, map, direction);

        if (!hasCollided) {
            moveX(moveAmountXRemainder * direction.getVelocity());
            MapCollisionCheckResult collisionCheckResult = MapCollisionHandler.getAdjustedPositionAfterCollisionCheckX(this, map, direction);
            if (collisionCheckResult.getAdjustedLocation() != null) {
                hasCollided = true;
                entityCollidedWith = collisionCheckResult.getEntityCollidedWith();
                float xLocationBeforeAdjustment = getX();
                setX(collisionCheckResult.getAdjustedLocation().x);
                amountMoved += Math.abs(xLocationBeforeAdjustment - getX());
            }
        }

        // adjust y position if moving down a slope
        MapCollisionCheckResult slopeCollisionMovingDownCheckResult = MapCollisionHandler.getAdjustedPositionAfterCollisionSlopeCheckX(this, map, slopeProximityStatus);
        if (slopeCollisionMovingDownCheckResult.getAdjustedLocation() != null) {
            setY(slopeCollisionMovingDownCheckResult.getAdjustedLocation().y);
        }

        // adjust y position if moving up a slope
        MapCollisionCheckResult slopeCollisionMovingUpCheckResult = MapCollisionHandler.getAdjustedPositionAfterCollisionSlopeCheckY(this, map);
        if (slopeCollisionMovingUpCheckResult.getAdjustedLocation() != null) {
            setY(slopeCollisionMovingUpCheckResult.getAdjustedLocation().y);
        }

        // call this method which a game object subclass can override to listen for collision events and react accordingly
        onEndCollisionCheckX(hasCollided, direction, entityCollidedWith);

        // returns the amount actually moved
        return amountMoved * direction.getVelocity();
    }

    // performs collision check logic for moving along the y axis against the map's tiles
    private float handleCollisionY(float moveAmountY) {
        // determines amount to move (whole number)
        int amountToMove = (int) Math.abs(moveAmountY);

        // gets decimal remainder from amount to move
        float moveAmountYRemainder = MathUtils.getRemainder(moveAmountY);

        // determines direction that will be moved in based on if moveAmountY is positive or negative
        Direction direction = moveAmountY < 0 ? Direction.UP : Direction.DOWN;

        // moves game object one pixel at a time until total move amount is reached
        // if at any point a map tile collision is determined to have occurred from the move, adjust player position back to right in front of the "solid" map tile's position, and stop attempting to move further
        // there is special logic to handle movement across slope map tiles
        float amountMoved = 0;
        boolean hasCollided = false;
        MapEntity entityCollidedWith = null;
        for (int i = 0; i < amountToMove; i++) {
            moveY(direction.getVelocity());

            MapCollisionCheckResult collisionCheckResult = MapCollisionHandler.getAdjustedPositionAfterCollisionCheckY(this, map, direction);
            if (collisionCheckResult.getAdjustedLocation() != null) {
                hasCollided = true;
                entityCollidedWith = collisionCheckResult.getEntityCollidedWith();
                setY(collisionCheckResult.getAdjustedLocation().y);
            }

            if (direction == Direction.DOWN) {
                MapCollisionCheckResult slopeCollisionMovingDownCheckResult = MapCollisionHandler.getAdjustedPositionAfterCollisionSlopeCheckY(this, map);
                if (slopeCollisionMovingDownCheckResult.getAdjustedLocation() != null) {
                    hasCollided = true;
                    entityCollidedWith = slopeCollisionMovingDownCheckResult.getEntityCollidedWith();
                    setY(slopeCollisionMovingDownCheckResult.getAdjustedLocation().y);
                }
            }

            amountMoved = i + 1;
            if (hasCollided) {
                break;
            }
        }

        // if no collision occurred in the above steps, this deals with the decimal remainder from the original move amount (stored in moveAmountYRemainder)
        // it starts by moving the game object by that decimal amount
        // it then does one more check for a collision in the case that this added decimal amount was enough to change the rounding and move the game object to the next pixel over
        // if a collision occurs from this move, the player is moved back to right in front of the "solid" map tile's position
        // there is special logic to handle movement across slope map tiles
        if (!hasCollided) {
            moveY(moveAmountYRemainder * direction.getVelocity());
            MapCollisionCheckResult collisionCheckResult = MapCollisionHandler.getAdjustedPositionAfterCollisionCheckY(this, map, direction);
            if (collisionCheckResult.getAdjustedLocation() != null) {
                hasCollided = true;
                entityCollidedWith = collisionCheckResult.getEntityCollidedWith();
                setY(collisionCheckResult.getAdjustedLocation().y);
            }
        }

        if (direction == Direction.DOWN) {
            MapCollisionCheckResult slopeCollisionMovingDownCheckResult = MapCollisionHandler.getAdjustedPositionAfterCollisionSlopeCheckY(this, map);
            if (slopeCollisionMovingDownCheckResult.getAdjustedLocation() != null) {
                hasCollided = true;
                entityCollidedWith = slopeCollisionMovingDownCheckResult.getEntityCollidedWith();
                setY(slopeCollisionMovingDownCheckResult.getAdjustedLocation().y);
            }
        }

        // call this method which a game object subclass can override to listen for collision events and react accordingly
        onEndCollisionCheckY(hasCollided, direction, entityCollidedWith);

        // returns the amount actually moved
        return amountMoved * direction.getVelocity();
    }

    // game object subclass can override this method to listen for x axis collision events and react accordingly after calling "moveXHandleCollision"
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
    }

    // game object subclass can override this method to listen for y axis collision events and react accordingly after calling "moveYHandleCollision"
    public void onEndCollisionCheckY(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
    }

    // gets x location taking into account map camera position
    public float getCalibratedXLocation() {
        if (map != null) {
            return Math.round(x) - map.getCamera().getX();
        } else {
            return Math.round(getX());
        }
    }

    // gets y location taking into account map camera position
    public float getCalibratedYLocation() {
        if (map != null) {
            return Math.round(y) - map.getCamera().getY();
        } else {
            return Math.round(getY());
        }
    }

    // gets bounds taking into account map camera position
    public Rectangle getCalibratedBounds() {
        if (map != null) {
            Rectangle bounds = getBounds();
            return new Rectangle(
                    Math.round(bounds.getX1()) - Math.round(map.getCamera().getX()),
                    Math.round(bounds.getY1()) - Math.round(map.getCamera().getY()),
                    Math.round(bounds.getWidth()),
                    Math.round(bounds.getHeight())
            );
        } else {
            return getBounds();
        }
    }

    // set this game object's map to make it a "part of" the map, allowing calibrated positions and collision handling logic to work
    public void setMap(Map map) {
        this.map = map;
    }
    public Map getMap() {
        return this.map;
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        if (map != null) {
            if (!graphicsHandler.isPixelGridEnabled()) {
                if (distanceFromGameplayLayer < 0) {
                    if(map.getTileByPosition(this.x, this.y) != null &&
                        map.getTileByPosition(this.x, this.y).getHasTransparentPixels()) {
                        graphicsHandler.drawImage(
                        animationsDark.get(currentAnimationName)[getCurrentFrameIndex()].getImage(),
                        Math.round(getCalibratedXLocation()),
                        Math.round(getCalibratedYLocation()),
                        currentFrame.getWidth(),
                        currentFrame.getHeight(),
                        currentFrame.getImageEffect());
                    }
                    
                }
                else {
                    graphicsHandler.drawImage(
                    currentFrame.getImage(),
                    Math.round(getCalibratedXLocation()),
                    Math.round(getCalibratedYLocation()),
                    currentFrame.getWidth(),
                    currentFrame.getHeight(),
                    currentFrame.getImageEffect());
                }
            } 
            else {
                // position relative to the exact camera, then snapped to the pixel grid
                float relX = Math.round(x) - map.getCamera().getX();
                float relY = Math.round(y) - map.getCamera().getY();
                int worldW = currentFrame.getWidth();
                int worldH = currentFrame.getHeight();

                int px1, py1, px2, py2;
                if (sharedEdgeDraw) {
                    px1 = GraphicsHandler.toPixelGrid(relX);
                    py1 = GraphicsHandler.toPixelGrid(relY);
                    px2 = GraphicsHandler.toPixelGrid(relX + worldW);
                    py2 = GraphicsHandler.toPixelGrid(relY + worldH);
                } else {
                    int pw = Math.max(GraphicsHandler.toPixelGrid(worldW), 1);
                    int ph = Math.max(GraphicsHandler.toPixelGrid(worldH), 1);
                    px1 = GraphicsHandler.toPixelGrid(relX);
                    // anchored by the bottom edge so sprites line up with tile tops
                    py2 = GraphicsHandler.toPixelGrid(relY + worldH);
                    py1 = py2 - ph;
                    px2 = px1 + pw; 
                }
                ScreenCoordinator currentScreen = (ScreenCoordinator)ScreenManager.getCurrentScreen();   
                if (animationsDark != null 
                    && (currentScreen.getCurrentScreen().hasMap() != null)
                    && numLayer+1 == (currentScreen.getCurrentScreen().hasMap().getCurrentGameplayLayer())){
                    graphicsHandler.drawPixelImage((animationsDark.get(currentAnimationName)[getCurrentFrameIndex()].getImage()), px1, py1, px2, py2, currentFrame.getImageEffect());
                }
                else {
                    graphicsHandler.drawPixelImage(currentFrame.getImage(), px1, py1, px2, py2, currentFrame.getImageEffect()); 
                }  
            }
        } 
        else{
            super.draw(graphicsHandler);
            
        }
    }
    public static int adjustBrightnessOfHexColor(int color, double factor) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        r = (int) Math.min(255, Math.max(0, r * factor));
        g = (int) Math.min(255, Math.max(0, g * factor));
        b = (int) Math.min(255, Math.max(0, b * factor));

        return (r << 16) | (g << 8) | b;
    }

    //self-explanatory
    public static BufferedImage copyImage(BufferedImage original) {
        BufferedImage copy = new BufferedImage(
            original.getWidth(),
            original.getHeight(),
            original.getType()
        );

        Graphics2D g = copy.createGraphics();
        g.drawImage(original, 0, 0, null);
        g.dispose();

        return copy;
    }
    public void setNumLayer(int numLayerIN){
        this.numLayer = numLayerIN;
    }
    @Override
    public void drawBounds(GraphicsHandler graphicsHandler, Color color) {
        if (map != null) {
            Rectangle calibratedBounds = getCalibratedBounds();
            calibratedBounds.setColor(color);
            calibratedBounds.draw(graphicsHandler);
        } else {
            super.drawBounds(graphicsHandler, color);
        }
    }
}