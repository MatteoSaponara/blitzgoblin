package Maps;

import Enemies.BugEnemy;
import Enemies.GoblinEnemy;
import Enemies.HeroEnemy;
import Enemies.Turret;
import Enemies.CoinMimicEnemy;
import Enemies.BlockMimicEnemy;
import Enemies.GargoyleEnemy;
import Enemies.ArmoredEnemy;
import Enemies.Statlactite;
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




public class level3 extends Map {

    public level3() {
        super("level3.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(2, 6).getLocation();
        //this.playerStartPosition = getMapTile(40, 24).getLocation(); //test spawn
    }

    @Override
    public ArrayList<Enemy> loadEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

        Turret turretEnemy2 = new Turret(getMapTile(3, 13).getLocation().addY(2), Direction.RIGHT);
        enemies.add(turretEnemy2);
        
        Turret turretEnemy3 = new Turret(getMapTile(3, 14).getLocation().addY(2), Direction.RIGHT);
        enemies.add(turretEnemy3);

        Turret turretEnemy4 = new Turret(getMapTile(3, 15).getLocation().addY(2), Direction.RIGHT);
        enemies.add(turretEnemy4);     

        GoblinEnemy goblinEnemy = new GoblinEnemy(getMapTile(17, 17).getLocation().addY(2), Direction.RIGHT);
        enemies.add(goblinEnemy);

        ArmoredEnemy armoredEnemy = new ArmoredEnemy(getMapTile(28, 25).getLocation().addY(2), Direction.LEFT);
        enemies.add(armoredEnemy);
        
        ArmoredEnemy armoredEnemy1 = new ArmoredEnemy(getMapTile(32, 25).getLocation().addY(2), Direction.LEFT);
        enemies.add(armoredEnemy1);
        
        //GoblinEnemy goblinEnemy4 = new GoblinEnemy(getMapTile(28, 25).getLocation().addY(2), Direction.LEFT);
        //enemies.add(goblinEnemy4);
             
        GargoyleEnemy gargoyleEnemy = new GargoyleEnemy(getMapTile(23, 15).getLocation().addY(2), Direction.RIGHT);
        enemies.add(gargoyleEnemy);

        GargoyleEnemy gargoyleEnemy2 = new GargoyleEnemy(getMapTile(59, 9).getLocation().addY(2), Direction.LEFT);
        enemies.add(gargoyleEnemy2);
        
        CoinMimicEnemy coinMimicEnemy = new CoinMimicEnemy(getMapTile(59, 13).getLocation(), Direction.LEFT);
        enemies.add(coinMimicEnemy);
        
        CoinMimicEnemy coinMimicEnemy2 = new CoinMimicEnemy(getMapTile(18, 23).getLocation(), Direction.LEFT);
        enemies.add(coinMimicEnemy2);
        
        Statlactite statlactite = new Statlactite(getMapTile(12, 3).getLocation());
        enemies.add(statlactite);
        
        Statlactite statlactite2 = new Statlactite(getMapTile(13, 3).getLocation());
        enemies.add(statlactite2);
        
        Statlactite statlactite4 = new Statlactite(getMapTile(25, 3).getLocation());
        enemies.add(statlactite4);
        
        Statlactite statlactite5 = new Statlactite(getMapTile(31, 3).getLocation());
        enemies.add(statlactite5);
        
        Statlactite statlactite6 = new Statlactite(getMapTile(40, 3).getLocation());
        enemies.add(statlactite6);
        
        Statlactite statlactite7 = new Statlactite(getMapTile(43, 2).getLocation());
        enemies.add(statlactite7);
        
        Statlactite statlactite8 = new Statlactite(getMapTile(37, 3).getLocation());
        enemies.add(statlactite8);
        
        Statlactite statlactite9 = new Statlactite(getMapTile(36, 3).getLocation());
        enemies.add(statlactite9);
        
        Statlactite statlactite10 = new Statlactite(getMapTile(16, 3).getLocation());
        enemies.add(statlactite10);
        
        Statlactite statlactite11 = new Statlactite(getMapTile(30, 3).getLocation());
        enemies.add(statlactite11);
        
        Statlactite statlactite12 = new Statlactite(getMapTile(58, 2).getLocation());
        enemies.add(statlactite12);
        
        Statlactite statlactite13 = new Statlactite(getMapTile(57, 3).getLocation());
        enemies.add(statlactite13);
        
        Statlactite statlactite14 = new Statlactite(getMapTile(56, 4).getLocation());
        enemies.add(statlactite14);
        
        Statlactite statlactite15 = new Statlactite(getMapTile(51, 2).getLocation());
        enemies.add(statlactite15);
        
        Statlactite statlactite16 = new Statlactite(getMapTile(65, 2).getLocation());
        enemies.add(statlactite16);
        
        Statlactite statlactite17 = new Statlactite(getMapTile(64, 3).getLocation());
        enemies.add(statlactite17);
        
        Statlactite statlactite18 = new Statlactite(getMapTile(47, 9).getLocation());
        enemies.add(statlactite18);
        
        Statlactite statlactite19 = new Statlactite(getMapTile(36, 10).getLocation());
        enemies.add(statlactite19);
        
        Statlactite statlactite20 = new Statlactite(getMapTile(38, 11).getLocation());
        enemies.add(statlactite20);
        
        Statlactite statlactite21 = new Statlactite(getMapTile(31, 11).getLocation());
        enemies.add(statlactite21);
        
        Statlactite statlactite22 = new Statlactite(getMapTile(21, 11).getLocation());
        enemies.add(statlactite22);
        
        Statlactite statlactite23 = new Statlactite(getMapTile(20, 12).getLocation());
        enemies.add(statlactite23);
        
        Statlactite statlactite24 = new Statlactite(getMapTile(13, 12).getLocation());
        enemies.add(statlactite24);
        
        Statlactite statlactite25 = new Statlactite(getMapTile(12, 12).getLocation());
        enemies.add(statlactite25);
        
        Statlactite statlactite26 = new Statlactite(getMapTile(6, 11).getLocation());
        enemies.add(statlactite26);
        
        Statlactite statlactite27 = new Statlactite(getMapTile(5, 12).getLocation());
        enemies.add(statlactite27);
        
        Statlactite statlactite28 = new Statlactite(getMapTile(6, 11).getLocation());
        enemies.add(statlactite28);
        
        Statlactite statlactite29 = new Statlactite(getMapTile(10, 20).getLocation());
        enemies.add(statlactite29);
        
        Statlactite statlactite30 = new Statlactite(getMapTile(9, 21).getLocation());
        enemies.add(statlactite30);
        
        Statlactite statlactite31 = new Statlactite(getMapTile(25, 21).getLocation());
        enemies.add(statlactite31);
        
        Statlactite statlactite32 = new Statlactite(getMapTile(34, 17).getLocation());
        enemies.add(statlactite32);
        
        Statlactite statlactite33 = new Statlactite(getMapTile(35, 17).getLocation());
        enemies.add(statlactite33);
        
        Statlactite statlactite34 = new Statlactite(getMapTile(26, 21).getLocation());
        enemies.add(statlactite34);
        
        BlockMimicEnemy blockMimicEnemy = new BlockMimicEnemy(getMapTile(14, 2).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy);
        
        BlockMimicEnemy blockMimicEnemy1 = new BlockMimicEnemy(getMapTile(19, 1).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy1);
        
        BlockMimicEnemy blockMimicEnemy2 = new BlockMimicEnemy(getMapTile(26, 9).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy2);
        
        BlockMimicEnemy blockMimicEnemy3 = new BlockMimicEnemy(getMapTile(21, 8).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy3);
        
        BlockMimicEnemy blockMimicEnemy4 = new BlockMimicEnemy(getMapTile(26, 2).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy4);
        
        BlockMimicEnemy blockMimicEnemy5 = new BlockMimicEnemy(getMapTile(29, 2).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy5);
        
        BlockMimicEnemy blockMimicEnemy6 = new BlockMimicEnemy(getMapTile(36, 8).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy6);
        
        BlockMimicEnemy blockMimicEnemy7 = new BlockMimicEnemy(getMapTile(39, 2).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy7);
        
        BlockMimicEnemy blockMimicEnemy8 = new BlockMimicEnemy(getMapTile(42, 2).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy8);
        
        BlockMimicEnemy blockMimicEnemy9 = new BlockMimicEnemy(getMapTile(31, 8).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy9);
        
        BlockMimicEnemy blockMimicEnemy10 = new BlockMimicEnemy(getMapTile(50, 1).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy10);
        
        BlockMimicEnemy blockMimicEnemy11 = new BlockMimicEnemy(getMapTile(46, 16).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy11);
        
        BlockMimicEnemy blockMimicEnemy12 = new BlockMimicEnemy(getMapTile(27, 13).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy12);
        
        BlockMimicEnemy blockMimicEnemy13 = new BlockMimicEnemy(getMapTile(34, 15).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy13);
        
        BlockMimicEnemy blockMimicEnemy14 = new BlockMimicEnemy(getMapTile(15, 28).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy14);
        
        BlockMimicEnemy blockMimicEnemy15 = new BlockMimicEnemy(getMapTile(68, 8).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy15);
        
        BlockMimicEnemy blockMimicEnemy16 = new BlockMimicEnemy(getMapTile(29, 26).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy16);
        
        
        
        

        return enemies;
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();
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

        //enhancedMapTiles.add(hmp3);
        //enhancedMapTiles.add(hmp4);
        //enhancedMapTiles.add(hmp5);
        EndLevelBox endLevelBox = new EndLevelBox(getMapTile(66, 26).getLocation());
        enhancedMapTiles.add(endLevelBox);
        EndLevelBox endLevelBox1 = new EndLevelBox(getMapTile(65, 27).getLocation());
        enhancedMapTiles.add(endLevelBox1);
        EndLevelBox endLevelBox2 = new EndLevelBox(getMapTile(64, 28).getLocation());
        enhancedMapTiles.add(endLevelBox2);

        //ammo boxes
        SingleAmmo singleAmmo = new SingleAmmo(getMapTile(3, 7).getLocation());
        enhancedMapTiles.add(singleAmmo);
        TwelveAmmo twelveAmmo = new TwelveAmmo(getMapTile(3, 7).getLocation());
        enhancedMapTiles.add(twelveAmmo);
        CannonAmmo cannonAmmo = new CannonAmmo(getMapTile(63, 9).getLocation());
        enhancedMapTiles.add(cannonAmmo);

       // Coin coin1 = new Coin(getMapTile(18, 23).getLocation());
      //  enhancedMapTiles.add(coin1);

        Coin coin2 = new Coin(getMapTile(18, 14).getLocation());
        enhancedMapTiles.add(coin2);

        Coin coin3 = new Coin(getMapTile(33, 13).getLocation());
        enhancedMapTiles.add(coin3);
        
        Coin coin4 = new Coin(getMapTile(7, 6).getLocation());
        enhancedMapTiles.add(coin4);
        
        Coin coin5 = new Coin(getMapTile(45, 14).getLocation());
        enhancedMapTiles.add(coin5);
        
        Coin coin6 = new Coin(getMapTile(37, 18).getLocation());
        enhancedMapTiles.add(coin6);
        
        Coin coin7 = new Coin(getMapTile(5, 20).getLocation());
        enhancedMapTiles.add(coin7);
        
        Coin coin8 = new Coin(getMapTile(16, 15).getLocation());
        enhancedMapTiles.add(coin8);
        
        Coin coin9 = new Coin(getMapTile(13, 25).getLocation());
        enhancedMapTiles.add(coin9);
        
        Coin coin10 = new Coin(getMapTile(53, 26).getLocation());
        enhancedMapTiles.add(coin10);
        
        Coin coin11 = new Coin(getMapTile(58, 19).getLocation());
        enhancedMapTiles.add(coin11);



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
