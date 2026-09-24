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

public class Coin extends EnhancedMapTile {

    public Coin(Point location)
    {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("Coin.png"), 12, 12), TileType.PASSABLE);
    }

    @Override
    public void update(Player player)
    {
        super.update(player);

        if (intersects(player))
        {
            player.collectCoin();
            this.setMapEntityStatus(MapEntityStatus.REMOVED);
        }
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet)
    {
        return new HashMap<String, Frame[]>() {{
			put("DEFAULT", new Frame[] {
					new FrameBuilder(spriteSheet.getSprite(0, 0), 30)
						.withScale(3)
						.withBounds(1, 1, 10, 10)
						.build(),
					new FrameBuilder(spriteSheet.getSprite(0, 1), 30)
						.withScale(3)
						.withBounds(1, 1, 10, 10)
						.build(),
					new FrameBuilder(spriteSheet.getSprite(0, 2), 30)
						.withScale(3)
						.withBounds(1, 1, 10, 10)
						.build(),
					new FrameBuilder(spriteSheet.getSprite(0, 3), 30)
						.withScale(3)
						.withBounds(1, 1, 10, 10)
						.build(),
				new FrameBuilder(spriteSheet.getSprite(0, 4), 30)
						.withScale(3)
						.withBounds(1, 1, 10, 10)
					.build(),
				new FrameBuilder(spriteSheet.getSprite(0, 5), 30)
						.withScale(3)
						.withBounds(1, 1, 10, 10)
						.build(),
				new FrameBuilder(spriteSheet.getSprite(0, 6), 30)
						.withScale(3)
						.withBounds(1, 1, 10, 10)
						.build(),
				new FrameBuilder(spriteSheet.getSprite(0, 7), 30)
						.withScale(3)
						.withBounds(1, 1, 10, 10)
						.build()
				});
        }};
    }
}

