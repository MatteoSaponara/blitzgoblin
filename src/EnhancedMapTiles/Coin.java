package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.Config;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.PhysicsParticle;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.MapEntityStatus;
import Level.Player;
import Level.TileType;
import Utils.Point;

import java.awt.Color;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Coin extends EnhancedMapTile {

    public Coin(Point location)
    {
        super(location.x + 6, location.y + 6, new SpriteSheet(ImageLoader.load("Coin.png"), 26, 26), TileType.PASSABLE);
		numLayer = 1;
    }
	public Coin(Point location, int numLayerIN)
    {
        super(location.x + 6, location.y + 6, new SpriteSheet(ImageLoader.load("Coin.png"), 26, 26), TileType.PASSABLE);

		numLayer = numLayerIN;
	}

    @Override
    public void update(Player player)
    {
        super.update(player);

        if (intersects(player))
        {
            player.collectCoin();
            spawnPickupSparkles();
            this.setMapEntityStatus(MapEntityStatus.REMOVED);
        }
    }

    private void spawnPickupSparkles()
    {
        if (map == null) {
            return;
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();
        int sparkleCount = random.nextInt(5, 9);
        float centerX = getX() + getWidth() / 2f;
        float centerY = getY() + getHeight() / 2f;

        for (int i = 0; i < sparkleCount; i++) {
            double angle = random.nextDouble(0, Math.PI * 2);
            float radius = (float) random.nextDouble(6, Math.max(getWidth(), getHeight()) / 2f);
            PhysicsParticle sparkle = new PhysicsParticle(
                    centerX + (float) Math.cos(angle) * radius,
                    centerY + (float) Math.sin(angle) * radius,
                    (float) random.nextDouble(4, 6.6),
                    random.nextBoolean() ? new Color(0xFF, 0xFB, 0xC5) : new Color(0xFB, 0xE1, 0x36));
            sparkle.setShape(PhysicsParticle.Shape.RECTANGLE);
            sparkle.setLifetimeFrames(random.nextInt(16, 25));
            sparkle.setShrinksToNothing(true);
            map.addParticle(sparkle);
        }
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet)
    {
        return new HashMap<String, Frame[]>() {{
			put("DEFAULT", new Frame[] {
					new FrameBuilder(spriteSheet.getSprite(0, 0), 12)
						.withScale(1.4f)
						.withBounds(1, 1, 25, 25)
						.build(),
					new FrameBuilder(spriteSheet.getSprite(0, 1), 12)
						.withScale(1.4f)
                            .withBounds(1, 1, 25, 25)
						.build(),
					new FrameBuilder(spriteSheet.getSprite(0, 2), 12)
						.withScale(1.4f)
                            .withBounds(1, 1, 25, 25)
						.build(),
					new FrameBuilder(spriteSheet.getSprite(0, 3), 12)
						.withScale(1.4f)
                            .withBounds(1, 1, 25, 25)
						.build(),
				new FrameBuilder(spriteSheet.getSprite(0, 4), 12)
						.withScale(1.4f)
						.withBounds(1, 1, 25, 25)
					.build(),
				new FrameBuilder(spriteSheet.getSprite(0, 5), 12)
						.withScale(1.4f)
                        .withBounds(1, 1, 25, 25)
						.build(),
				new FrameBuilder(spriteSheet.getSprite(0, 6), 12)
						.withScale(1.4f)
                        .withBounds(1, 1, 25, 25)
						.build(),
				new FrameBuilder(spriteSheet.getSprite(0, 7), 12)
						.withScale(1.4f)
                        .withBounds(1, 1, 25, 25)
						.build()
				});
        }};
    }
}
