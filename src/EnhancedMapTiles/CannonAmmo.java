package EnhancedMapTiles;

import Level.Player;
import Utils.Point;

public class CannonAmmo extends Ammo {

    public CannonAmmo(Point location) {
        super(location, "ammocannon.png", 1.4f);
    }
    public CannonAmmo(Point location, int numLayerIN) {
        super(location, "CannonAmmo.png");
        numLayer = numLayerIN;
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
