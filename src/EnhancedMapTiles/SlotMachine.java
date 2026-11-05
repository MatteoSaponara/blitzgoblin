package EnhancedMapTiles;

import Engine.Key;
import Engine.Keyboard;
import GameObject.Frame;
import Level.EnhancedMapTile;
import Level.Player;
import Level.TileType;
import Utils.ImageUtils;
import Utils.Point;
import java.awt.Color;

public class SlotMachine extends EnhancedMapTile {
    private static final int WIDTH = 147;
    private static final int HEIGHT = 147;
    private boolean interactionKeyReleased = true;

    public SlotMachine(Point location) {
        super(location.x, location.y,
                new Frame(ImageUtils.createSolidImage(new Color(130, 45, 80), WIDTH, HEIGHT)),
                TileType.PASSABLE);
    }

    @Override
    public void update(Player player) {
        super.update(player);

        if (Keyboard.isKeyUp(Key.E)) {
            interactionKeyReleased = true;
        } else if (interactionKeyReleased && intersects(player)) {
            interactionKeyReleased = false;
            if (player.getCoins() > 0) {
                player.setCoins(player.getCoins() - 1);
            }
        }
    }
}
