package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.MapEntityStatus;
import Level.Player;
import Level.TileType;
import Utils.Point;

import java.util.HashMap;

public class SingleAmmo extends Ammo {

    public SingleAmmo(Point location, String imageFileName) {
        super(location, imageFileName);
    }
    
    @Override
    public void update(Player player)
    {
        

        super.update(player);
    }
}
