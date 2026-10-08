package Level;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Mouse;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Utils.Point;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.HashMap;

public class Shotgun extends GameObject {
    
    //sort of self-explanatory. the gun travels in an ellipse around the player. the origin/center of this ellipse
    //is based off of the center position of the player sprite.
    private Point gunPivotPosition;

    //seperate aim angles being tracked. one from the player sprite to the cursor. the other from the gun sprite's center to the cursor.
    //
    private double previousAimAngle = -1;
    private double previousGunAimAngle = -1;
    
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private int[] originalGunSpriteBarrelEndPixel = {54, 27}; // must be manually set !!
    //////////////////////////////////////////////////////////////////////////////////
    private int[] currentGunSpriteBarrelEndPixel = {54, 27}; // updates when the gun sprite rotates
    
    private Player weaponOwner;
    private Ellipse gunPositionalRange = new Ellipse(10, 15);
    private boolean mouseAngleChangedThisFrame = true;

    private final double RADIAN_IN_DEGREES = 57.2958;
    private final float SPRITE_SCALE = 1.4f;

    private final String[] animationNames = {"test"}; //fill with whatever animations ya'll wanna add in

    //copy of animation frames used to prevent modifications to currentFrame's image permanently
    //changing the sprite image
	protected HashMap<String, Frame[]> originalAnimationFrames;

    //hard-coded sprite dimension values. set to dimensions of whatever image is used.
    public Shotgun(float x, float y, Player weaponOwnerIN) {
        //!!!!!!!!!!! EXTREMELY IMPORTANT !!!!!!!!!
        //when setting the sprite for the shotgun (ImageLoader.load(file)), ensure that the barrel end 
        //(where you want the pellets to fire from), is aligned with the center of the image.
        //the further from the center the less accurate the gun tracks the mouse
        super(new SpriteSheet(ImageLoader.load("gunnn3n.png"), 55, 55), x, y, "test"); //just a test anim
        ///////////////////////////////////////////
        weaponOwner = weaponOwnerIN;
        map = weaponOwnerIN.getMap();
        gunPivotPosition = weaponOwnerIN.getLocation(); 
        //System.arraycopy(animations.get(currentAnimationName), 0, originalFrames, 0, animations.get(currentAnimationName).length);
        originalAnimationFrames = new HashMap<>();
        for (String animName: animationNames) {
            originalAnimationFrames.put(animName, animations.get(animName));
        }
    }
    public void update() {
        if (map != null) {
            if((previousAimAngle < 0 || mouseAngleHasChanged())) {
                previousAimAngle = weaponOwner.aimAngle;
                previousAimAngle = (Math.PI)-previousAimAngle;
                mouseAngleChangedThisFrame = true;
            }
            else {
                mouseAngleChangedThisFrame = false;
            }
            gunPivotPosition = new Point(weaponOwner.getLocation().x + (weaponOwner.getBounds().getWidth()),
            weaponOwner.getLocation().y + (weaponOwner.getBounds().getHeight()*(3f/4)));
            Point newPos = getGameObjectEllipticalPosition(gunPivotPosition, gunPositionalRange, previousAimAngle);
            if(getPointsDistance(getLocation(), newPos) > 0.01f){
                setLocation(newPos.x - (getWidth()/2), newPos.y - (getHeight()/2));
                //System.err.println("newPos.x, x: "+ newPos.x+ ", " +x+"\n"+
                //    "newPos.y, y: "+ newPos.y+ ", " +y+"\n"+
                //    "weaponOwner.getLocation(): " + weaponOwner.getLocation()
                //);
           }
        }
        else {
            map = weaponOwner.getMap();
            mouseAngleChangedThisFrame = false;
        }
        //System.err.println("previousAimAngle(in degrees): "+(previousAimAngle*57.2958f));
        super.update();
        updateCurrentFrame();
    }

    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
        //drawBounds(graphicsHandler, new Color(255, 0, 0, 170));
    }
    // currentFrame is essentially a sprite, so each game loop cycle the sprite
	// needs to have its current state updated based on animation logic,
	// and location updated to match any changes to the animated sprite class
    double currentDegreeTest = 140D;
    
    private double calibratedCircularMouseAngle() {return (Math.PI)-previousAimAngle;}
    
    private boolean mouseAngleHasChanged() {
        return (weaponOwner != null) && (Math.abs(previousAimAngle-((Math.PI)-weaponOwner.aimAngle)) > 0.01f);
    }
    
////////////////////////////////////////////////////////////

    //copy of sprite from originalAnimHashMap
    private BufferedImage originalGunSprite;
    //output sprite
    private BufferedImage visibleSprite;

    //as visibleSprite is the displayed/output sprite, for efficiency's sake
    //it is left unmodified if the gun is never rotated in a frame. However, if the 
    //sprite changes via animation these changes are skipped too.
    //frameChanged forces an update to visibleSprite if the sprite's animation frame advances/changes
    int lastFrame = 0;
    boolean frameChanged = false;
    
    @Override 
	protected void updateCurrentFrame() {
        frameChanged = false;

        currentFrame = getCurrentFrame();

        //insurance bleh
        if(originalAnimationFrames == null && animationNames != null) {
            originalAnimationFrames = new HashMap<>();
            for (String animName: animationNames) {
                originalAnimationFrames.put(animName, animations.get(animName));
            }
        }
        else if(originalAnimationFrames == null)
            return;
        
        //loading insurance stuff idek
        if(currentFrame == null)
            return;

        //updates original gun sprite if its empty or if the current animation frame changes
        if (lastFrame != currentFrameIndex || originalGunSprite == null) {
            //copying/saving current frame to be used by visible sprite
            originalGunSprite = copyImage(originalAnimationFrames.get(currentAnimationName)[currentFrameIndex].getImage());

            lastFrame = currentFrameIndex;
            frameChanged = true;
        }

        //self-explanatory
        if(visibleSprite == null)
            visibleSprite = copyImage(originalGunSprite);
        
        //insurance
        if(map == null)
            return;
        
        //ew but works
        //(the modified/rotated sprite uses the hypotenuse length from its center to a corner as half of it's width.
        //like a circular radius, it ensures pixels don't get clipped off when the sprite is rotated.)
        double modSpriteRadiusLength;
        Point sprCenterPixel = new Point(originalGunSprite.getWidth()/2, originalGunSprite.getHeight()/2);
        modSpriteRadiusLength = getPointsDistance(sprCenterPixel, new Point(originalGunSprite.getWidth()-1, originalGunSprite.getHeight()-1));
        previousGunAimAngle = (Math.PI)-(Mouse.getAngleTo(x+(modSpriteRadiusLength*SPRITE_SCALE)- map.camera.getX(), y+(modSpriteRadiusLength*SPRITE_SCALE)- map.camera.getY()));
        
        //for flipping the sprite horizontally as a frame/imageEffect when it goes into a certain arc angle
        boolean flip = previousGunAimAngle*RADIAN_IN_DEGREES > 90 && previousGunAimAngle*RADIAN_IN_DEGREES < 270;
        
        //updating displayed sprite during animation updates or when manually aiming the gun with the cursor
        if(mouseAngleChangedThisFrame || frameChanged) {
            //i am actually just throwing this together jesus christ
            visibleSprite = rotateShotgunImageAroundCenter(originalGunSprite,  (flip? -1 : 1)*
            ((flip? -180 : 0) + previousGunAimAngle*RADIAN_IN_DEGREES));
            //System.err.println("Changed");
        }
        else{
            //System.err.println("hasnt Changed");
        }
        ImageEffect imgEffect = ImageEffect.NONE;
        if(flip)
            imgEffect = ImageEffect.FLIP_HORIZONTAL;

        //replicating the frame Scale effect (didn't scale properly when I set currentFrame's image manually)
        Frame tempFrame = currentFrame.copy();
        currentFrame = new Frame(visibleSprite, imgEffect,
             tempFrame.getScale(), tempFrame.getBounds(), tempFrame.getDelay());
        currentFrame.setX(x);
		currentFrame.setY(y);

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

    public BufferedImage getOriginalGunSprite(){
        return originalGunSprite;
    }
    public BufferedImage getVisibleGunSprite(){
        return visibleSprite;
    }

    //i'm sure this already exists in a library, but i am too lazy to search for or read on it
    //method names are self-explanatory
    private class Ellipse {
        float horizDirectionalRadius, vertDirectionalRadius;
        public Ellipse(float hR, float vR) {
            horizDirectionalRadius = hR;
            vertDirectionalRadius = vR;
        }
        public double getDirectionalRadiusAtAngle(double angleRadians){
            double radiusIN;
            radiusIN = (horizDirectionalRadius*vertDirectionalRadius) /
        Math.sqrt(Math.pow(vertDirectionalRadius* Math.cos(angleRadians),2) + Math.pow(horizDirectionalRadius *Math.sin(angleRadians),2));
            return radiusIN;
        }
        public Point getPositionOnEllipseWithAngle(double angleRadians){
             double radius = getDirectionalRadiusAtAngle(angleRadians);
            return new Point((float)Math.round((radius * Math.cos(angleRadians))),
        (float)Math.round((radius * Math.sin(angleRadians))));
        }
    }
    
    //used for calculating where to place the shotgun object along the ellipse surrounding/on the player
    private Point getGameObjectEllipticalPosition(Point origin, Ellipse ellip, double angleRadians){
        float x, y;
        Point test = ellip.getPositionOnEllipseWithAngle(angleRadians);
        x = origin.x+test.x;
        y = origin.y-test.y;
        return new Point(x, y);
    }

    public static double getPointsDistance(Point a, Point b){
            double result = Math.sqrt(((Math.pow((b.x - a.x), 2)) + (Math.pow((b.y - a.y), 2))));
            return result;
    }
    

    public static int[] rotatePixelRelativeToCenterPixel(int xCoorIN, int yCoorIN, double degreeIN, BufferedImage sourceImage, BufferedImage destinationImage){
        
        
        Point sourceImageCenterPixel = new Point((sourceImage.getWidth())/2, (sourceImage.getHeight())/2);
        Point destinationImageCenterPixel = new Point(((destinationImage.getWidth())/2), ((destinationImage.getHeight())/2));

        double pixelRadius = getPointsDistance(sourceImageCenterPixel, new Point(xCoorIN, yCoorIN));

        int modSpaceX = (int)Math.round(xCoorIN - sourceImageCenterPixel.x);
        int modSpaceY = (int)Math.round(sourceImageCenterPixel.y - yCoorIN);

        int newXCoor, newYCoor;
        
        double currentPixelAngleFromCenter = (modSpaceX != 0 ) ? Math.atan((double)(modSpaceY)/modSpaceX) : Double.NaN;
            
        if((currentPixelAngleFromCenter == 0 || Double.isNaN(currentPixelAngleFromCenter)) && (modSpaceX == 0 ^ modSpaceY == 0)) {
            if(modSpaceX == 0) {
                currentPixelAngleFromCenter = (modSpaceY > 1) ? Math.toRadians(90) : Math.toRadians(270);
            }
            else if(modSpaceY == 0) {
                currentPixelAngleFromCenter = (modSpaceX > 1) ? 0 : Math.toRadians(180);
            }
        }
        else {
            if(modSpaceX < 0) {
                if(modSpaceY > 0)
                    currentPixelAngleFromCenter = Math.toRadians(90) + currentPixelAngleFromCenter;
                currentPixelAngleFromCenter += Math.toRadians(90);
            }
            if(modSpaceY < 0) {
                if(modSpaceX > 0) {
                    currentPixelAngleFromCenter = Math.toRadians(90) + currentPixelAngleFromCenter;
                    currentPixelAngleFromCenter += Math.toRadians(180);
                }
                currentPixelAngleFromCenter += Math.toRadians(90);
            }
        }
        double newPixelAngleFromCenter = currentPixelAngleFromCenter + Math.toRadians(degreeIN);

        newXCoor = (int)Math.round((Math.cos(newPixelAngleFromCenter)*pixelRadius));
        newYCoor = (int)Math.round((Math.sin(newPixelAngleFromCenter)*pixelRadius));

        newXCoor += destinationImageCenterPixel.x;
        newYCoor = (int)Math.round(destinationImageCenterPixel.y - newYCoor);

        int[] result = {newXCoor, newYCoor};
        return result;
        
    }
    private BufferedImage rotateShotgunImageAroundCenter(BufferedImage originalImage, double degrees) {
        BufferedImage modSprite;

        Point spriteCenterPixel;
        int centerPixelX = originalImage.getWidth()/2;
        int centerPixelY = originalImage.getHeight()/2;
        
        spriteCenterPixel = new Point(centerPixelX, centerPixelY);
        
        //modSprite is a buffered image, so it is a rectangle. however, this is describing the distance from the center of modsprite to a side.
        //which is derived from the distance from the center of the original sprite to a corner.
        //this makes mod sprite side length (radius length * 2)
        double modSpriteRadiusLength;
        modSpriteRadiusLength = getPointsDistance(spriteCenterPixel, new Point(originalImage.getWidth()-1, originalImage.getHeight()-1));
        /////////////////////////////
        
        //test
        //modSpriteRadiusLength = (originalImage.getWidth()/2);
        
        //cautionary ceiling rounding. (terrified of out-of-bounds errors)
        modSpriteRadiusLength = Math.ceil(modSpriteRadiusLength);

        modSprite = new BufferedImage((int)(modSpriteRadiusLength*2), (int)(modSpriteRadiusLength*2), BufferedImage.TYPE_INT_ARGB);
        //System.out.println("width: "+modSprite.getWidth() + "length: "+modSprite.getHeight());

        Point modSpriteCenterPixel = new Point(((modSprite.getWidth())/2), ((modSprite.getHeight())/2));

        //where image data is read from and written to  
        int[] originalSpritePixels = ((DataBufferInt) originalImage.getRaster().getDataBuffer()).getData();
        int[] modSpritePixels = ((DataBufferInt) modSprite.getRaster().getDataBuffer()).getData();
        
        //radial distance from the center of an inputted image, to an input pixel
        //(input depends on rotatePixelRelativeToCenterPixel())
        double pixelRadius;

        //for testing
        int fail=0;
        
        int[] newBarrelPixel = new int[0];
        //iterate through buffered images
        for (int yCoor = 0; yCoor < modSprite.getHeight(); yCoor++) {
                for (int xCoor = 0; xCoor < modSprite.getWidth(); xCoor++) {
                
                //rotating via inverse mapping to insure every input pixel has a corresponding output pixel
                int[] newCoors = rotatePixelRelativeToCenterPixel(xCoor, yCoor, -degrees, modSprite, originalImage);
                
                if(newCoors[0] == originalGunSpriteBarrelEndPixel[0] && newCoors[1] == originalGunSpriteBarrelEndPixel[1]) {
                    newBarrelPixel = new int[]{xCoor, yCoor};
                    System.arraycopy(newBarrelPixel, 0, currentGunSpriteBarrelEndPixel, 0, 2);
                }

                //skipping out-of-bounds pixels
                if ((newCoors[1] < 0 || newCoors[0] < 0 ||
                newCoors[1]  >= modSprite.getHeight() || newCoors[0] >=modSprite.getWidth()))
                    continue;
                if(newCoors[1]  >= originalImage.getHeight() || newCoors[0] >=originalImage.getWidth())
                    continue;
                
                //writing original sprite pixel color to its corresponding, rotated, mod sprite pixel.
                modSpritePixels[(yCoor) * modSprite.getWidth() + xCoor] 
                = originalSpritePixels[newCoors[1]  * originalImage.getWidth() + newCoors[0]] ;
            }
        }
        //in the event for a particular angle there is no pixel exactly corresponding to the original barrelEndPixel (when using inverse mapping)
        //instead, rotate using forward mapping and round to the closest pixel.
        if (newBarrelPixel.length == 0) {
            newBarrelPixel = rotatePixelRelativeToCenterPixel(originalGunSpriteBarrelEndPixel[0],
                 originalGunSpriteBarrelEndPixel[1], degrees, originalImage, modSprite);
            System.arraycopy(newBarrelPixel, 0, currentGunSpriteBarrelEndPixel, 0, 2);
            //System.out.println("fail");
        }
        //originally for testing purposes, but also looks sort of neat. just sets the barrel pixel to #00FF00
        //comment it out whenever
        modSpritePixels[(currentGunSpriteBarrelEndPixel[1]) * modSprite.getWidth() + currentGunSpriteBarrelEndPixel[0]] =
        0xFF00FF00;

        return modSprite;
    }

    //relative to world space 
    //used by player class to shoot pellets from the barrel position/pixel
    public float[] getCurrentGunBarrelEndPixelPosition(){
        boolean flip = previousGunAimAngle*RADIAN_IN_DEGREES > 90 && previousGunAimAngle*RADIAN_IN_DEGREES < 270;
        return new float[]{
            x+(!flip ? currentGunSpriteBarrelEndPixel[0]*SPRITE_SCALE : (getWidth()) - (currentGunSpriteBarrelEndPixel[0]*SPRITE_SCALE)),
            y+((currentGunSpriteBarrelEndPixel[1])*SPRITE_SCALE)
        };
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("test", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 30)
                            .withScale(SPRITE_SCALE)
                            .withBounds(0, 0, 55, 55)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 30)
                            .withScale(SPRITE_SCALE)
                            .withBounds(0, 0, 55, 55)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 30)
                            .withScale(SPRITE_SCALE)
                            .withBounds(0, 0, 55, 55)
                            .build(),               
            });
        }};
    }
}