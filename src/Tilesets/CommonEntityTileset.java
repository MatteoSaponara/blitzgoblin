package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.Config;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;
import java.util.ArrayList;

// This class represents a "common" tileset of standard tiles defined in the CommonTileset.png file
public class CommonEntityTileset extends Tileset {

    public CommonEntityTileset() {
        super(ImageLoader.load("CommonEntityTileset.png"), Config.TILE_ART_SIZE, Config.TILE_ART_SIZE, Config.TILE_SCALE);
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // empty frame
        Frame emptyEntityFrame = new FrameBuilder(getSubImage(0, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder emptySpawnTile = new MapTileBuilder(emptyEntityFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(emptySpawnTile);

        // goblin spawn
        Frame goblinSpawnFrame = new FrameBuilder(getSubImage(0, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder goblinSpawnTile = new MapTileBuilder(goblinSpawnFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(goblinSpawnTile);

        // bug spawn
        Frame bugSpawnFrame = new FrameBuilder(getSubImage(0, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder bugSpawnTile = new MapTileBuilder(bugSpawnFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(bugSpawnTile);
        // turret spawn
        Frame turretSpawnFrame = new FrameBuilder(getSubImage(0, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder turretSpawnTile = new MapTileBuilder(turretSpawnFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(turretSpawnTile);

        // block mimic spawn
        Frame blockMimicSpawnFrame = new FrameBuilder(getSubImage(1, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder blockMimicSpawnTile = new MapTileBuilder(blockMimicSpawnFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(blockMimicSpawnTile);

        // moving platform entity spawn
        Frame movingPlatformSpawnFrame = new FrameBuilder(getSubImage(1, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder movingPlatformSpawnTile = new MapTileBuilder(movingPlatformSpawnFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(movingPlatformSpawnTile);

        // moving platform end
        Frame movingPlatformEndSpawnFrame = new FrameBuilder(getSubImage(1, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder movingPlatformEndSpawnTile = new MapTileBuilder(movingPlatformEndSpawnFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(movingPlatformEndSpawnTile);

        

        // coin spawn
        Frame coinSpawnFrame = new FrameBuilder(getSubImage(1, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder coinSpawnTile = new MapTileBuilder(coinSpawnFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(coinSpawnTile);

        // end box spawn
        Frame endBoxSpawnFrame = new FrameBuilder(getSubImage(2, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder endBoxSpawnTile = new MapTileBuilder(endBoxSpawnFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(endBoxSpawnTile);

        return mapTiles;
    }
}