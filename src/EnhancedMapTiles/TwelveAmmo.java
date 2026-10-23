package EnhancedMapTiles;

import Level.Player;
import Utils.Point;

public class TwelveAmmo extends Ammo {

    public TwelveAmmo(Point location) {
        super(location, "ammo12shot.png", 1.4f);
    }
    public TwelveAmmo(Point location, int numLayerIN) {
        super(location, "TwelveAmmo.png");
        numLayer = numLayerIN;
    }
    @Override
    public void update(Player player)
    {
        if (intersects(player))
        {
            player.unlockTwelveAmmo();
        }

        super.update(player);
    }
    
}
