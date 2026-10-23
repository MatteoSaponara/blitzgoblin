package EnhancedMapTiles;

import Level.Player;
import Utils.Point;

public class SingleAmmo extends Ammo {

    public SingleAmmo(Point location) {
        super(location, "SingleAmmo.png");
    }
    public SingleAmmo(Point location, int numLayerIN) {
        super(location, "SingleAmmo.png");
        numLayer = numLayerIN;
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
