package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Rectangle;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.Player;
import Level.TileType;
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;
import java.awt.image.BufferedImage;
import Level.MapTile;

public class DoorBlock extends EnhancedMapTile {
    
    public DoorBlock(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("Door.png"), 35, 35), TileType.NOT_PASSABLE);
    }

    @Override
    public void update(Player player) {
        super.update(player);
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

    public void switchDoorState() {
        if (this.getTileType() == TileType.NOT_PASSABLE) {
            this.setTileType(TileType.PASSABLE);
        } else {
            this.setTileType(TileType.NOT_PASSABLE);
        }
    }
}
