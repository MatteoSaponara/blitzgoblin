package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.Player;
import Level.TileType;
import Utils.Point;
import java.util.HashMap;

public class DoorBlock extends EnhancedMapTile {
    private static final int PASSABLE_DARKNESS_ALPHA = 52;
    
    public DoorBlock(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("Door.png"), 35, 35), TileType.NOT_PASSABLE);
    }

    @Override
    public void update(Player player) {
        super.update(player);
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        if (getTileType() == TileType.PASSABLE) {
            super.draw(graphicsHandler, graphicsHandler.getDarkenedImage(
                    currentFrame.getImage(), PASSABLE_DARKNESS_ALPHA));
        } else {
            super.draw(graphicsHandler);
        }
    }
    @Override
        public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
            return new HashMap<String, Frame[]>() {{
                put("DEFAULT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 40)
                            .withScale(1.4f)
                            .withBounds(1, 1, 35, 35)
                            .build()
                });
            }};
        }
    public void switchDoorState() {
        if (this.getTileType() == TileType.NOT_PASSABLE) {
            this.setTileType(TileType.PASSABLE);
        } else {
            this.setTileType(TileType.NOT_PASSABLE);
        }
    }
}
