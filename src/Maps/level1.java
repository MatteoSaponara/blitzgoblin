package Maps;

import Enemies.BugEnemy;
import Enemies.GoblinEnemy;
import Enemies.HeroEnemy;
import Enemies.Turret;
import Enemies.CoinMimicEnemy;
import Enemies.GargoyleEnemy;
import Engine.ImageLoader;
import EnhancedMapTiles.Coin;
import EnhancedMapTiles.EndLevelBox;
import EnhancedMapTiles.HorizontalMovingPlatform;
// Ammo pickup imports
import EnhancedMapTiles.SingleAmmo;
import EnhancedMapTiles.TwelveAmmo;
import EnhancedMapTiles.CannonAmmo;
import GameObject.Rectangle;
import Level.*;
import NPCs.Walrus;
import Tilesets.CommonTileset;
import Utils.Direction;
import java.util.ArrayList;
import Enemies.BlockMimicEnemy;




public class level1 extends Map {

    public level1() {
        super("level1.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(2, 24).getLocation();
        //this.playerStartPosition = getMapTile(40, 24).getLocation(); //test spawn
    }

    @Override
    public ArrayList<Enemy> loadEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

        Turret turretEnemy = new Turret(getMapTile(16, 12).getLocation().addY(2), Direction.RIGHT);
        enemies.add(turretEnemy);

        Turret turretEnemy2 = new Turret(getMapTile(15, 16).getLocation().addY(2), Direction.RIGHT);
        enemies.add(turretEnemy2);

        Turret turretEnemy3 = new Turret(getMapTile(35, 6).getLocation().addY(2), Direction.RIGHT);
        enemies.add(turretEnemy3);

        Turret turretEnemy4 = new Turret(getMapTile(35, 2).getLocation().addY(2), Direction.RIGHT);
        enemies.add(turretEnemy4);

        Turret turretEnemy5 = new Turret(getMapTile(49, 1).getLocation().addY(2), Direction.LEFT);
        enemies.add(turretEnemy5);

        Turret turretEnemy6 = new Turret(getMapTile(3, 12).getLocation().addY(2), Direction.LEFT);
        enemies.add(turretEnemy6);

        GoblinEnemy goblinEnemy = new GoblinEnemy(getMapTile(17, 17).getLocation().addY(2), Direction.RIGHT);
        enemies.add(goblinEnemy);

        GoblinEnemy goblinEnemy2 = new GoblinEnemy(getMapTile(38, 17).getLocation().addY(2), Direction.LEFT);
        enemies.add(goblinEnemy2);

        GoblinEnemy goblinEnemy3 = new GoblinEnemy(getMapTile(10, 15).getLocation().addY(2), Direction.RIGHT);
        enemies.add(goblinEnemy3);

        GargoyleEnemy gargoyleEnemy = new GargoyleEnemy(getMapTile(62, 10).getLocation().addY(2), Direction.LEFT);
        enemies.add(gargoyleEnemy);
        
        CoinMimicEnemy coinMimicEnemy = new CoinMimicEnemy(getMapTile(59, 13).getLocation(), Direction.LEFT);
        enemies.add(coinMimicEnemy);

        CoinMimicEnemy coinMimicEnemy2 = new CoinMimicEnemy(getMapTile(5, 13).getLocation(), Direction.LEFT);
        enemies.add(coinMimicEnemy2);

        BlockMimicEnemy blockMimicEnemy = new BlockMimicEnemy(getMapTile(36, 1).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy);

        BlockMimicEnemy blockMimicEnemy2 = new BlockMimicEnemy(getMapTile(50, 3).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy2);

        BlockMimicEnemy blockMimicEnemy3 = new BlockMimicEnemy(getMapTile(44, 0).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy3);

        BlockMimicEnemy blockMimicEnemy4 = new BlockMimicEnemy(getMapTile(13, 4).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy4);

        return enemies;
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        HorizontalMovingPlatform hmp = new HorizontalMovingPlatform(
                ImageLoader.load("GreenPlatform.png"),
                getMapTile(56, 17).getLocation(),
                getMapTile(62, 17).getLocation(),
                TileType.JUMP_THROUGH_PLATFORM,
                1.4f,
                new Rectangle(0, 6,16,4),
                Direction.LEFT
        );
        HorizontalMovingPlatform hmp1 = new HorizontalMovingPlatform(
                ImageLoader.load("GreenPlatform.png"),
                getMapTile(56, 14).getLocation(),
                getMapTile(62, 14).getLocation(),
                TileType.JUMP_THROUGH_PLATFORM,
                1.4f,
                new Rectangle(0, 6,16,4),
                Direction.RIGHT
        );
        HorizontalMovingPlatform hmp2 = new HorizontalMovingPlatform(
                ImageLoader.load("GreenPlatform.png"),
                getMapTile(56, 11).getLocation(),
                getMapTile(62, 11).getLocation(),
                TileType.JUMP_THROUGH_PLATFORM,
                1.4f,
                new Rectangle(0, 6,16,4),
                Direction.LEFT
        );
//        HorizontalMovingPlatform hmp3 = new HorizontalMovingPlatform(
//                ImageLoader.load("GreenPlatform.png"),
//                getMapTile(42, 9).getLocation(),
//                getMapTile(47, 9).getLocation(),
//                2f,
//                TileType.JUMP_THROUGH_PLATFORM,
//                3,
//                new Rectangle(0, 6,16,4),
//                Direction.RIGHT
//        );
//        HorizontalMovingPlatform hmp4 = new HorizontalMovingPlatform(
//                ImageLoader.load("GreenPlatform.png"),
//                getMapTile(49, 20).getLocation(),
//                getMapTile(54, 20).getLocation(),
//                TileType.JUMP_THROUGH_PLATFORM,
//                3,
//                new Rectangle(0, 6,16,4),
//                Direction.RIGHT
//        );
//
//        HorizontalMovingPlatform hmp5 = new HorizontalMovingPlatform(
//                ImageLoader.load("GreenPlatform.png"),
//                getMapTile(49, 17).getLocation(),
//                getMapTile(54, 17).getLocation(),
//                TileType.JUMP_THROUGH_PLATFORM,
//                3,
//                new Rectangle(0, 6,16,4),
//                Direction.LEFT
//        );

        enhancedMapTiles.add(hmp);
        enhancedMapTiles.add(hmp1);
        enhancedMapTiles.add(hmp2);
        //enhancedMapTiles.add(hmp3);
        //enhancedMapTiles.add(hmp4);
        //enhancedMapTiles.add(hmp5);
        EndLevelBox endLevelBox = new EndLevelBox(getMapTile(68, 8).getLocation());
        enhancedMapTiles.add(endLevelBox);
        EndLevelBox endLevelBox1 = new EndLevelBox(getMapTile(68, 7).getLocation());
        enhancedMapTiles.add(endLevelBox1);
        EndLevelBox endLevelBox2 = new EndLevelBox(getMapTile(68, 6).getLocation());
        enhancedMapTiles.add(endLevelBox2);

        //ammo boxes
        SingleAmmo singleAmmo = new SingleAmmo(getMapTile(2, 24).getLocation());
        enhancedMapTiles.add(singleAmmo);
        TwelveAmmo twelveAmmo = new TwelveAmmo(getMapTile(2, 24).getLocation());
        enhancedMapTiles.add(twelveAmmo);
        CannonAmmo cannonAmmo = new CannonAmmo(getMapTile(2, 24).getLocation());
        enhancedMapTiles.add(cannonAmmo);

        Coin coin1 = new Coin(getMapTile(18, 23).getLocation());
        enhancedMapTiles.add(coin1);

        Coin coin2 = new Coin(getMapTile(18, 14).getLocation());
        enhancedMapTiles.add(coin2);

        Coin coin3 = new Coin(getMapTile(33, 14).getLocation());
        enhancedMapTiles.add(coin3);
        Coin coin4 = new Coin(getMapTile(51, 16).getLocation());
        enhancedMapTiles.add(coin4);
        Coin coin5 = new Coin(getMapTile(46, 0).getLocation());
        enhancedMapTiles.add(coin5);
        Coin coin6 = new Coin(getMapTile(39, 4).getLocation());
        enhancedMapTiles.add(coin6);
        Coin coin7 = new Coin(getMapTile(47, 6).getLocation());
        enhancedMapTiles.add(coin7);
        Coin coin8 = new Coin(getMapTile(11, 18).getLocation());
        enhancedMapTiles.add(coin8);
        Coin coin9 = new Coin(getMapTile(13, 7).getLocation());
        enhancedMapTiles.add(coin9);
        Coin coin10 = new Coin(getMapTile(42, 2).getLocation());
        enhancedMapTiles.add(coin10);
//        Coin coin11 = new Coin(getMapTile(51, 18).getLocation());
//        enhancedMapTiles.add(coin11);
//        Coin coin12 = new Coin(getMapTile(51, 11).getLocation());
//        enhancedMapTiles.add(coin12);
//        Coin coin13 = new Coin(getMapTile(57, 18).getLocation());
//        enhancedMapTiles.add(coin13);
        return enhancedMapTiles;
    }


    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        //Walrus walrus = new Walrus(getMapTile(23, 14).getLocation().subtractY(13));
        //npcs.add(walrus);

        return npcs;
    }
}
