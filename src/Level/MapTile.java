package Level;

import Engine.GraphicsHandler;
import GameObject.Frame;
import GameObject.SpriteSheet;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;

// Represents a map tile in a Map's tile map
public class MapTile extends MapEntity {
    private static final java.util.Map<BufferedImage, Boolean> OPAQUE_IMAGE_CACHE =
            Collections.synchronizedMap(new java.util.WeakHashMap<BufferedImage, Boolean>());

    // this determines a tile's properties, like if it's passable or not
    protected TileType tileType;
    protected TileLayout tileLayout; // only used for slopes

    private int tileIndex;
    private int darknessAlpha;

    {
        sharedEdgeDraw = true;
    }

    public MapTile(float x, float y, HashMap<String, Frame[]> animations, TileType tileType, int tileIndex) {
        super(x, y, animations, "DEFAULT");
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

    public TileType getTileType() {
        return tileType;
    }

    public int getTileIndex() {
        return tileIndex;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    boolean isFullBlock() {
        if (tileType != TileType.NOT_PASSABLE || tileLayout != null) {
            return false;
        }

        BufferedImage image = currentFrame.getImage();
        synchronized (OPAQUE_IMAGE_CACHE) {
            Boolean isOpaque = OPAQUE_IMAGE_CACHE.get(image);
            if (isOpaque != null) {
                return isOpaque;
            }

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    if ((image.getRGB(x, y) >>> 24) != 0xFF) {
                        OPAQUE_IMAGE_CACHE.put(image, false);
                        return false;
                    }
                }
            }
            OPAQUE_IMAGE_CACHE.put(image, true);
        }
        return true;
    }

    void setDarknessAlpha(int darknessAlpha) {
        this.darknessAlpha = darknessAlpha;
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
        if (darknessAlpha > 0 && map != null) {
            BufferedImage darkenedImage = graphicsHandler.getDarkenedImage(
                    currentFrame.getImage(), darknessAlpha);
            super.draw(graphicsHandler, darkenedImage);
        } else {
            super.draw(graphicsHandler);
        }

        // uncomment this to draw bounds of all non passable tiles (useful for debugging)
        /*
        if (tileType == TileType.NOT_PASSABLE) {
            drawBounds(graphicsHandler, new Color(0, 0, 255, 100));
        }
        */
    }
}