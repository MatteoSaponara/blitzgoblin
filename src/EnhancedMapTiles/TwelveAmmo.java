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

public class TwelveAmmo extends Ammo {

    public TwelveAmmo(Point location) {
        super(location, "TwelveAmmo.png");
    }
    
    @Override
    public void update(Player player)
    {
        player.unlockTwelveAmmo();

        super.update(player);
    }
    
}
