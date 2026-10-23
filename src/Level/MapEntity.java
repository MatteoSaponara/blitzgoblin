package Level;

import Engine.ScreenManager;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import java.util.HashMap;

// This class represents a map entity, which is any "entity" on a map besides the player
// it is basically a game object with a few extra features for handling things like respawning
public class MapEntity extends GameObject {
    protected MapEntityStatus mapEntityStatus = MapEntityStatus.ACTIVE;
    
    //copy of animation frames used to prevent modifications to currentFrame's image permanently
    //changing the sprite image
	protected HashMap<String, Frame[]> originalAnimationFrames;

    private final String[] animationNames = {"test"}; //fill with whatever animations ya'll wanna add in


    // if true, entity will continue to be updated even if off camera
    protected boolean isUpdateOffScreen = false;

    public MapEntity(float x, float y, int numLayerIN, SpriteSheet spriteSheet, String startingAnimation) {
        super(spriteSheet, x, y, startingAnimation);
        numLayer = numLayerIN;
    }

    public MapEntity(float x, float y, SpriteSheet spriteSheet, String startingAnimation) {
        super(spriteSheet, x, y, startingAnimation);
        //numLayer = 1;
        if(ScreenManager.getCurrentScreen() != null && ScreenManager.getCurrentScreen().hasMap() != null)
        numLayer = ScreenManager.getCurrentScreen().hasMap().getCurrentGameplayLayer();
    }

    public MapEntity(float x, float y, HashMap<String, Frame[]> animations, String startingAnimation) {
        super(x, y, animations, startingAnimation);
        if(ScreenManager.getCurrentScreen() != null && ScreenManager.getCurrentScreen().hasMap() != null)
            numLayer = map.getCurrentGameplayLayer();
    }

    public MapEntity(float x, float y, int numLayerIN, HashMap<String, Frame[]> animations, String startingAnimation) {
        super(x, y, animations, startingAnimation);
        numLayer = numLayerIN;
    }

    public MapEntity(float x, float y, Frame[] frames) {
        super(x, y, frames);
    }

    public MapEntity(float x, float y, Frame frame) {
        super(x, y, frame);
    }

    public MapEntity(float x, float y) {
        super(x, y);
    }

    public void initialize() {
        this.x = startPositionX;
        this.y = startPositionY;
        this.amountMovedX = 0;
        this.amountMovedY = 0;
        this.previousX = startPositionX;
        this.previousY = startPositionY;
        
        updateCurrentFrame();
    }

    public MapEntityStatus getMapEntityStatus() {
        return mapEntityStatus;
    }

    public void setMapEntityStatus(MapEntityStatus mapEntityStatus) {
        this.mapEntityStatus = mapEntityStatus;
    }

    public boolean isUpdateOffScreen() {
        return isUpdateOffScreen;
    }

    public void setIsUpdateOffScreen(boolean isUpdateOffScreen) {
        this.isUpdateOffScreen = isUpdateOffScreen;
    }
}
