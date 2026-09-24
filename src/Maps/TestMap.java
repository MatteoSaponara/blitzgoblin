package Maps;

import Enemies.BugEnemy;
import Enemies.GoblinEnemy;
import Enemies.HeroEnemy;
import Enemies.Turret;
import Engine.ImageLoader;
import EnhancedMapTiles.Coin;
import EnhancedMapTiles.EndLevelBox;
import EnhancedMapTiles.HorizontalMovingPlatform;
import GameObject.Rectangle;
import Level.*;
import NPCs.Walrus;
import Tilesets.CommonTileset;
import Utils.Direction;
import java.util.ArrayList;

// Represents a test map to be used in a level
public class TestMap extends Map {

    public TestMap() {
        super("test_map.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(2, 24).getLocation();
        //this.playerStartPosition = getMapTile(40, 24).getLocation(); //test spawn
    }

    @Override
    public ArrayList<Enemy> loadEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

        GoblinEnemy goblinEnemy = new GoblinEnemy(getMapTile(28, 25).getLocation().subtractY(25), Direction.LEFT);
        enemies.add(goblinEnemy);
        GoblinEnemy goblinEnemy1 = new GoblinEnemy(getMapTile(35, 16).getLocation().subtractY(25), Direction.RIGHT);
        enemies.add(goblinEnemy1);
        GoblinEnemy goblinEnemy2 = new GoblinEnemy(getMapTile(42, 26).getLocation().subtractY(25), Direction.RIGHT);
        enemies.add(goblinEnemy2);
        BugEnemy bugEnemy2 = new BugEnemy(getMapTile(13, 15).getLocation().subtractY(25), Direction.LEFT);
        enemies.add(bugEnemy2);

        Turret turretEnemy = new Turret(getMapTile(23, 23).getLocation().addY(2), Direction.LEFT);
        enemies.add(turretEnemy);
        Turret turretEnemy1 = new Turret(getMapTile(12, 10).getLocation().addY(2), Direction.LEFT);
        enemies.add(turretEnemy1);

        HeroEnemy heroEnemy = new HeroEnemy(getMapTile(63, 19).getLocation(), Direction.LEFT);
        enemies.add(heroEnemy);

        return enemies;
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        HorizontalMovingPlatform hmp = new HorizontalMovingPlatform(
                ImageLoader.load("GreenPlatform.png"),
                getMapTile(30, 5).getLocation(),
                getMapTile(33, 5).getLocation(),
                TileType.JUMP_THROUGH_PLATFORM,
                3, 
                new Rectangle(0, 6,16,4),
                Direction.LEFT
        );
        HorizontalMovingPlatform hmp1 = new HorizontalMovingPlatform(
                ImageLoader.load("GreenPlatform.png"),
                getMapTile(34, 5).getLocation(),
                getMapTile(37, 5).getLocation(),
                TileType.JUMP_THROUGH_PLATFORM,
                3,
                new Rectangle(0, 6,16,4),
                Direction.LEFT
        );
        HorizontalMovingPlatform hmp2 = new HorizontalMovingPlatform(
                ImageLoader.load("GreenPlatform.png"),
                getMapTile(42, 3).getLocation(),
                getMapTile(46, 3).getLocation(),
                TileType.JUMP_THROUGH_PLATFORM,
                3,
                new Rectangle(0, 6,16,4),
                Direction.LEFT
        );
        HorizontalMovingPlatform hmp3 = new HorizontalMovingPlatform(
                ImageLoader.load("GreenPlatform.png"),
                getMapTile(42, 9).getLocation(),
                getMapTile(47, 9).getLocation(),
                2f,
                TileType.JUMP_THROUGH_PLATFORM,
                3,
                new Rectangle(0, 6,16,4),
                Direction.RIGHT
        );
        HorizontalMovingPlatform hmp4 = new HorizontalMovingPlatform(
                ImageLoader.load("GreenPlatform.png"),
                getMapTile(49, 20).getLocation(),
                getMapTile(54, 20).getLocation(),
                TileType.JUMP_THROUGH_PLATFORM,
                3,
                new Rectangle(0, 6,16,4),
                Direction.RIGHT
        );

        HorizontalMovingPlatform hmp5 = new HorizontalMovingPlatform(
                ImageLoader.load("GreenPlatform.png"),
                getMapTile(49, 17).getLocation(),
                getMapTile(54, 17).getLocation(),
                TileType.JUMP_THROUGH_PLATFORM,
                3,
                new Rectangle(0, 6,16,4),
                Direction.LEFT
        );
        
        enhancedMapTiles.add(hmp);
        enhancedMapTiles.add(hmp1);
        enhancedMapTiles.add(hmp2);
        enhancedMapTiles.add(hmp3);
        enhancedMapTiles.add(hmp4);
        enhancedMapTiles.add(hmp5);
        EndLevelBox endLevelBox = new EndLevelBox(getMapTile(67, 18).getLocation());
        enhancedMapTiles.add(endLevelBox);

        Coin coin1 = new Coin(getMapTile(9, 22).getLocation());
		enhancedMapTiles.add(coin1);

		Coin coin2 = new Coin(getMapTile(20, 20).getLocation());
		enhancedMapTiles.add(coin2);

		Coin coin3 = new Coin(getMapTile(30, 17).getLocation());
		enhancedMapTiles.add(coin3);
        Coin coin4 = new Coin(getMapTile(38, 15).getLocation());
		enhancedMapTiles.add(coin4);
        Coin coin5 = new Coin(getMapTile(23, 12).getLocation());
		enhancedMapTiles.add(coin5);
        Coin coin6 = new Coin(getMapTile(15, 12).getLocation());
		enhancedMapTiles.add(coin6);
        Coin coin7 = new Coin(getMapTile(16, 3).getLocation());
		enhancedMapTiles.add(coin7);
        Coin coin8 = new Coin(getMapTile(26, 3).getLocation());
		enhancedMapTiles.add(coin8);
        Coin coin9 = new Coin(getMapTile(43, 7).getLocation());
		enhancedMapTiles.add(coin9);
        Coin coin10 = new Coin(getMapTile(42, 20).getLocation());
		enhancedMapTiles.add(coin10);
        Coin coin11 = new Coin(getMapTile(51, 18).getLocation());
		enhancedMapTiles.add(coin11);
        Coin coin12 = new Coin(getMapTile(51, 11).getLocation());
		enhancedMapTiles.add(coin12);
        Coin coin13 = new Coin(getMapTile(57, 18).getLocation());
		enhancedMapTiles.add(coin13);

        return enhancedMapTiles;
    }


    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        Walrus walrus = new Walrus(getMapTile(23, 14).getLocation().subtractY(13));
        npcs.add(walrus);

        return npcs;
    }
}
