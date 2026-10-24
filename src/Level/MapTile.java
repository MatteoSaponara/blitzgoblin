package Level;

import Engine.GraphicsHandler;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Tilesets.CommonEntityTileset;
import java.util.HashMap;

// Represents a map tile in a Map's tile map
public class MapTile extends MapEntity {
    // this determines a tile's properties, like if it's passable or not
    protected TileType tileType;
    protected TileLayout tileLayout; // only used for slopes

    private int tileIndex;
    {
        sharedEdgeDraw = true;
    }

    public MapTile(float x, float y, HashMap<String, Frame[]> animations, TileType tileType, int tileIndex) {
        super(x, y, animations, "DEFAULT");
        this.tileType = tileType;
        this.tileIndex = tileIndex;
    }
    public MapTile(float x, float y, int layer, int gamePlayLayerIN, HashMap<String, Frame[]> animations, TileType tileType, int tileIndex) {
        super(x, y,layer, gamePlayLayerIN, animations, "DEFAULT");
        this.tileType = tileType;
        this.tileIndex = tileIndex;
    }
    public MapTile(float x, float y, HashMap<String, Frame[]> animations, TileType tileType) {
        super(x, y, animations, "DEFAULT");
        this.tileType = tileType;
    }

    public MapTile(float x, float y, SpriteSheet spriteSheet, TileType tileType) {
        super(x, y, spriteSheet, "DEFAULT");
        this.tileType = tileType;
    }

    public MapTile(float x, float y, Frame[] frames, TileType tileType) {
        super(x, y, frames);
        this.tileType = tileType;
    }

    public MapTile(float x, float y, Frame frame, TileType tileType) {
        super(x, y, frame);
        this.tileType = tileType;
    }
    public MapTile copy(){
        return new MapTile(this.x, this.y, this.animations, this.tileType, this.tileIndex);
    }
    public MapTile copyEmpty(){
        //return new MapTile(this.x, this.y, this.animations, TileType.PASSABLE, 1);
        return new MapTile(this.x, this.y, new CommonEntityTileset().getTile(0).cloneAnimations(), TileType.PASSABLE, 0);
    }

    public TileType getTileType() {
        return tileType;
    }

    public int getTileIndex() {
        return tileIndex;
    }

    // determines if tile is animated or not
    public boolean isAnimated() {
        return getCurrentAnimation().length > 1;
    }

    public TileLayout getLayout() { return tileLayout; }

    public void setLayout(TileLayout tileLayout) { this.tileLayout = tileLayout; }

    public void update() {
        super.update();
    }

    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);

        // uncomment this to draw bounds of all non passable tiles (useful for debugging)
        /*
        if (tileType == TileType.NOT_PASSABLE) {
            drawBounds(graphicsHandler, new Color(0, 0, 255, 100));
        }
        */
    }
}