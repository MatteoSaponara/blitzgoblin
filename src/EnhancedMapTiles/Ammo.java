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

public abstract class Ammo extends EnhancedMapTile {
 
    public Ammo(Point location, String imageFileName)
    {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load(imageFileName), 12, 12), TileType.PASSABLE);
    }

    @Override
    public void update(Player player)
    {
        super.update(player);

        if (intersects(player))
        {
            this.setMapEntityStatus(MapEntityStatus.REMOVED);
        }
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build()
            });
        }};
    }
}
