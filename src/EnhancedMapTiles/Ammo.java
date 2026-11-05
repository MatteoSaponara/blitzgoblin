package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.Config;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.MapEntityStatus;
import Level.Player;
import Level.TileType;
import Utils.Point;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public abstract class Ammo extends EnhancedMapTile {
 
    public Ammo(Point location, String imageFileName)
    {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load(imageFileName), 12, 12), TileType.PASSABLE);
    }

    protected Ammo(Point location, String imageFileName, float scale) {
        this(location, ImageLoader.load(imageFileName), scale);
    }

    private Ammo(Point location, BufferedImage image, float scale) {
        super(
                location.x + getTileCenteringOffset(image.getWidth(), scale),
                location.y + getTileCenteringOffset(image.getHeight(), scale),
                new Frame[]{
                        new FrameBuilder(image)
                                .withScale(scale)
                                .withBounds(1, 1, image.getWidth() - 2, image.getHeight() - 2)
                                .build()
                },
                TileType.PASSABLE
        );
    }

    private static float getTileCenteringOffset(int imageDimension, float scale) {
        float scaledTileSize = Config.TILE_ART_SIZE * Config.TILE_SCALE;
        return (scaledTileSize - imageDimension * scale) / 2f;
    }

    @Override
    public void update(Player player)
    {
        super.update(player);

        if (intersects(player))
        {
            this.setMapEntityStatus(MapEntityStatus.REMOVED);
        }
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build()
            });
        }};
    }
}
