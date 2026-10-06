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

public class CannonAmmo extends Ammo {

    public CannonAmmo(Point location) {
        super(location, "CannonAmmo.png");
    }
    
    @Override
    public void update(Player player)
    {
        if (intersects(player))
        {
            player.unlockCannonAmmo();
        }

        super.update(player);
    }
    
}
