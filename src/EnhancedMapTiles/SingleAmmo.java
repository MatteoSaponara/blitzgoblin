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

    public SingleAmmo(Point location) {
        super(location, "SingleAmmo.png");
    }
    
    @Override
    public void update(Player player)
    {
        if (intersects(player))
        {
            player.unlockSingleAmmo();
        }

        super.update(player);
    }
}
