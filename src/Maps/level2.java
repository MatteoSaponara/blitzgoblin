package Maps;

import Enemies.BlockMimicEnemy;
import Enemies.CoinMimicEnemy;
import Enemies.GoblinEnemy;
import Enemies.Lever;
import Enemies.Turret;
import Engine.Config;
import Engine.ImageLoader;
import EnhancedMapTiles.Coin;
import EnhancedMapTiles.DoorBlock;
import EnhancedMapTiles.EndLevelBox;
import EnhancedMapTiles.HorizontalMovingPlatform;
import EnhancedMapTiles.SingleAmmo;
import EnhancedMapTiles.TwelveAmmo;
import GameObject.Rectangle;
import Level.*;
import Tilesets.CommonTileset;
import Utils.Direction;
import Utils.Point;
import java.util.ArrayList;




public class level2 extends Map {
    public static int startingGameplayLayer = 0; //set to layer you want gameplay to start in on load

    public level2() {
        super("level2.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(1, 7).getLocation();
        
        //weird offset issue occurring
        this.playerStartPosition = new Point(playerStartPosition.x - ((Config.TILE_ART_SIZE)/2), playerStartPosition.y);
        //this.playerStartPosition = getMapTile(40, 24).getLocation(); //test spawn
    }

    ArrayList<Lever> leversList;

    @Override
    public ArrayList<Enemy> loadEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();
        leversList = new ArrayList<Lever>();
        
        Turret turretEnemy = new Turret(getMapTile(10, 23).getLocation().addY(2), Direction.LEFT);
        enemies.add(turretEnemy);

        Turret turretEnemy1 = new Turret(getMapTile(67, 13).getLocation().addY(2), Direction.LEFT);
        enemies.add(turretEnemy1);
        Turret turretEnemy2 = new Turret(getMapTile(67, 14).getLocation().addY(2), Direction.LEFT);
        enemies.add(turretEnemy2);
        Turret turretEnemy3 = new Turret(getMapTile(67, 15).getLocation().addY(2), Direction.LEFT);
        enemies.add(turretEnemy3);
        Turret turretEnemy4 = new Turret(getMapTile(56, 8).getLocation().addY(2), Direction.RIGHT);
        enemies.add(turretEnemy4);

        enemies.add(spawnEnemy(GoblinEnemy.class, 13, 16, Direction.RIGHT));
        enemies.add(spawnEnemy(GoblinEnemy.class, 17, 24, Direction.RIGHT));
        enemies.add(spawnEnemy(GoblinEnemy.class, 47, 4, Direction.LEFT));
        enemies.add(spawnEnemy(GoblinEnemy.class, 28, 16, Direction.LEFT));

        
        CoinMimicEnemy coinMimicEnemy = new CoinMimicEnemy(getMapTile(59, 9).getLocation(), Direction.LEFT);
        enemies.add(coinMimicEnemy);

        CoinMimicEnemy coinMimicEnemy1 = new CoinMimicEnemy(getMapTile(26, 14   ).getLocation(), Direction.LEFT);
        enemies.add(coinMimicEnemy1);

        CoinMimicEnemy coinMimicEnemy2 = new CoinMimicEnemy(getMapTile(27, 5).getLocation(), Direction.LEFT);
        enemies.add(coinMimicEnemy2);

        //help

        BlockMimicEnemy blockMimicEnemy = new BlockMimicEnemy(getMapTile(11, 3).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy);
        BlockMimicEnemy blockMimicEnemy1 = new BlockMimicEnemy(getMapTile(37,22).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy1);
        BlockMimicEnemy blockMimicEnemy2 = new BlockMimicEnemy(getMapTile(37,23).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy2);
        BlockMimicEnemy blockMimicEnemy3 = new BlockMimicEnemy(getMapTile(38,23).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy3);
        BlockMimicEnemy blockMimicEnemy4 = new BlockMimicEnemy(getMapTile(39,23).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy4);
        BlockMimicEnemy blockMimicEnemy5 = new BlockMimicEnemy(getMapTile(40,24).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy5);
        BlockMimicEnemy blockMimicEnemy6 = new BlockMimicEnemy(getMapTile(41,24).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy6);
        BlockMimicEnemy blockMimicEnemy7 = new BlockMimicEnemy(getMapTile(42,22).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy7);
        BlockMimicEnemy blockMimicEnemy8 = new BlockMimicEnemy(getMapTile(42,21).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy8);
        BlockMimicEnemy blockMimicEnemy9 = new BlockMimicEnemy(getMapTile(42,20).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy9);
        BlockMimicEnemy blockMimicEnemy10 = new BlockMimicEnemy(getMapTile(41,19).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy10);
        BlockMimicEnemy blockMimicEnemy11 = new BlockMimicEnemy(getMapTile(40,18).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy11);
        BlockMimicEnemy blockMimicEnemy12 = new BlockMimicEnemy(getMapTile(39,18).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy12);
        BlockMimicEnemy blockMimicEnemy13 = new BlockMimicEnemy(getMapTile(38,18).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy13);

        BlockMimicEnemy blockMimicEnemy14 = new BlockMimicEnemy(getMapTile(17,5).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy14);
        BlockMimicEnemy blockMimicEnemy15 = new BlockMimicEnemy(getMapTile(17,4).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy15);

        BlockMimicEnemy blockMimicEnemy16 = new BlockMimicEnemy(getMapTile(54,15).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy16);
        BlockMimicEnemy blockMimicEnemy17 = new BlockMimicEnemy(getMapTile(55,15).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy17);
        BlockMimicEnemy blockMimicEnemy18 = new BlockMimicEnemy(getMapTile(56,15).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy18);
        BlockMimicEnemy blockMimicEnemy19 = new BlockMimicEnemy(getMapTile(54,16).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy19);
        BlockMimicEnemy blockMimicEnemy20 = new BlockMimicEnemy(getMapTile(55,16).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy20);
        //for some reason this block mimic is ignoring certain tiles and i do not have time to fix it
        //BlockMimicEnemy blockMimicEnemy21 = new BlockMimicEnemy(getMapTile(56,16).getLocation(), Direction.LEFT);
        //enemies.add(blockMimicEnemy21);
        BlockMimicEnemy blockMimicEnemy22 =     new BlockMimicEnemy(getMapTile(54,17).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy22);
        BlockMimicEnemy blockMimicEnemy23 = new BlockMimicEnemy(getMapTile(55,17).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy23);
        BlockMimicEnemy blockMimicEnemy24 = new BlockMimicEnemy(getMapTile(56,17).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy24);
        blockMimicEnemy16.setAggroRadius(300);
        blockMimicEnemy17.setAggroRadius(300);
        blockMimicEnemy18.setAggroRadius(300);
        blockMimicEnemy19.setAggroRadius(300);
        blockMimicEnemy20.setAggroRadius(300);
        //blockMimicEnemy21.setAggroRadius(300);
        blockMimicEnemy22.setAggroRadius(300);
        blockMimicEnemy23.setAggroRadius(300);
        blockMimicEnemy24.setAggroRadius(300);

        /*BlockMimicEnemy blockMimicEnemy25 = new BlockMimicEnemy(getMapTile(65,13).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy25);
        BlockMimicEnemy blockMimicEnemy26 = new BlockMimicEnemy(getMapTile(66,13).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy26);
        BlockMimicEnemy blockMimicEnemy27 = new BlockMimicEnemy(getMapTile(67,13).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy27);
        BlockMimicEnemy blockMimicEnemy28 = new BlockMimicEnemy(getMapTile(65,14).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy28);
        BlockMimicEnemy blockMimicEnemy29 = new BlockMimicEnemy(getMapTile(66,14).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy29);
        BlockMimicEnemy blockMimicEnemy30 = new BlockMimicEnemy(getMapTile(67,14).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy30);
        BlockMimicEnemy blockMimicEnemy31 =     new BlockMimicEnemy(getMapTile(65,15).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy31);
        BlockMimicEnemy blockMimicEnemy32 = new BlockMimicEnemy(getMapTile(66,15).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy32);
        BlockMimicEnemy blockMimicEnemy33 = new BlockMimicEnemy(getMapTile(67,15).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy33);
        BlockMimicEnemy blockMimicEnemy34 = new BlockMimicEnemy(getMapTile(65,16).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy34);
        BlockMimicEnemy blockMimicEnemy35 = new BlockMimicEnemy(getMapTile(66,16).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy35);
        BlockMimicEnemy blockMimicEnemy36 =     new BlockMimicEnemy(getMapTile(65,21).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy36);
        BlockMimicEnemy blockMimicEnemy37 = new BlockMimicEnemy(getMapTile(65,22).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy37);
        BlockMimicEnemy blockMimicEnemy38 = new BlockMimicEnemy(getMapTile(65,23).getLocation(), Direction.LEFT);
        enemies.add(blockMimicEnemy38);*/
        /*blockMimicEnemy25.setAggroRadius(300);
        blockMimicEnemy26.setAggroRadius(300);
        blockMimicEnemy27.setAggroRadius(300);
        blockMimicEnemy28.setAggroRadius(300);
        blockMimicEnemy29.setAggroRadius(300);
        blockMimicEnemy30.setAggroRadius(300);
        blockMimicEnemy31.setAggroRadius(300);
        blockMimicEnemy32.setAggroRadius(300);
        blockMimicEnemy33.setAggroRadius(300);
        blockMimicEnemy34.setAggroRadius(300);
        blockMimicEnemy35.setAggroRadius(300);
        //blockMimicEnemy36.setAggroRadius(300);
        //blockMimicEnemy37.setAggroRadius(300);
        //blockMimicEnemy38.setAggroRadius(300);*/
        
        Lever lever1 = new Lever(getMapTile(26, 23).getLocation());
        DoorBlock doorBlock0 = new DoorBlock(getMapTile(15, 17).getLocation());
        DoorBlock doorBlock1 = new DoorBlock(getMapTile(15, 22 ).getLocation());
        DoorBlock doorBlock2 = new DoorBlock(getMapTile(22, 24).getLocation());
        DoorBlock doorBlock3 = new DoorBlock(getMapTile(17, 15).getLocation());
        DoorBlock doorBlock4 = new DoorBlock(getMapTile(17, 14).getLocation());
        DoorBlock doorBlock5 = new DoorBlock(getMapTile(17, 16).getLocation());
        lever1.addDoorBlock(doorBlock0);
        lever1.addDoorBlock(doorBlock1);
        lever1.addDoorBlock(doorBlock2);
        lever1.addDoorBlock(doorBlock3);
        lever1.addDoorBlock(doorBlock4);
        lever1.addDoorBlock(doorBlock5);
        enemies.add(lever1);
        leversList.add(lever1);

        Lever lever2 = new Lever(getMapTile(44, 16).getLocation());
        DoorBlock doorBlock6 = new DoorBlock(getMapTile(47, 19).getLocation());
        DoorBlock doorBlock7 = new DoorBlock(getMapTile(47, 20 ).getLocation());
        DoorBlock doorBlock8 = new DoorBlock(getMapTile(47, 21).getLocation());
        DoorBlock doorBlock9 = new DoorBlock(getMapTile(47, 22).getLocation());
        DoorBlock doorBlock10 = new DoorBlock(getMapTile(47, 23).getLocation());
        DoorBlock doorBlock11 = new DoorBlock(getMapTile(47, 24).getLocation());
        DoorBlock doorBlock12 = new DoorBlock(getMapTile(47, 25).getLocation());
        lever2.addDoorBlock(doorBlock6);
        lever2.addDoorBlock(doorBlock7);
        lever2.addDoorBlock(doorBlock8);
        lever2.addDoorBlock(doorBlock9);
        lever2.addDoorBlock(doorBlock10);
        lever2.addDoorBlock(doorBlock11);
        lever2.addDoorBlock(doorBlock12);
        enemies.add(lever2);
        leversList.add(lever2);

        Lever lever3 = new Lever(getMapTile(50, 17).getLocation());
        DoorBlock doorBlock13 = new DoorBlock(getMapTile(54, 18).getLocation());
        doorBlock13.setTileType(TileType.PASSABLE);
        DoorBlock doorBlock14 = new DoorBlock(getMapTile(55, 18).getLocation());
        doorBlock14.setTileType(TileType.PASSABLE);
        DoorBlock doorBlock15 = new DoorBlock(getMapTile(56, 18).getLocation());
        doorBlock15.setTileType(TileType.PASSABLE);
        
        lever3.addDoorBlock(doorBlock13);
        lever3.addDoorBlock(doorBlock14);
        lever3.addDoorBlock(doorBlock15);
        enemies.add(lever3);
        leversList.add(lever3);

        Lever lever4 = new Lever(getMapTile(67, 21).getLocation());
        
        DoorBlock doorBlock19 = new DoorBlock(getMapTile(61, 20).getLocation());
        DoorBlock doorBlock20 = new DoorBlock(getMapTile(62, 20).getLocation());
        DoorBlock doorBlock21 = new DoorBlock(getMapTile(63, 20).getLocation());
        DoorBlock doorBlock22 = new DoorBlock(getMapTile(64, 20).getLocation());
        lever4.addDoorBlock(doorBlock13);
        lever4.addDoorBlock(doorBlock14);
        lever4.addDoorBlock(doorBlock15);
        
        lever4.addDoorBlock(doorBlock19);
        lever4.addDoorBlock(doorBlock20);
        lever4.addDoorBlock(doorBlock21);
        lever4.addDoorBlock(doorBlock22);
        enemies.add(lever4);
        leversList.add(lever4);

        Lever lever6 = new Lever(getMapTile(66, 18).getLocation());

        lever6.addDoorBlock(doorBlock13);
        lever6.addDoorBlock(doorBlock14);
        lever6.addDoorBlock(doorBlock15);
        
        lever6.addDoorBlock(doorBlock19);
        lever6.addDoorBlock(doorBlock20);
        lever6.addDoorBlock(doorBlock21);
        lever6.addDoorBlock(doorBlock22);
        enemies.add(lever6);
        leversList.add(lever6);

        Lever lever5 = new Lever(getMapTile(58, 6).getLocation());
        DoorBlock doorBlock16 = new DoorBlock(getMapTile(57, 14).getLocation());
        DoorBlock doorBlock17 = new DoorBlock(getMapTile(57, 15).getLocation());
        DoorBlock doorBlock18 = new DoorBlock(getMapTile(58, 15).getLocation());
        DoorBlock doorBlock23 = new DoorBlock(getMapTile(66, 4).getLocation());
        DoorBlock doorBlock24 = new DoorBlock(getMapTile(66 , 5).getLocation());
        DoorBlock doorBlock25 = new DoorBlock(getMapTile(66, 6).getLocation());
        DoorBlock doorBlock26 = new DoorBlock(getMapTile(66, 7).getLocation());
        lever5.addDoorBlock(doorBlock16);
        lever5.addDoorBlock(doorBlock17);
        lever5.addDoorBlock(doorBlock18);
        lever5.addDoorBlock(doorBlock23);
        lever5.addDoorBlock(doorBlock24);
        lever5.addDoorBlock(doorBlock25);
        lever5.addDoorBlock(doorBlock26);
        enemies.add(lever5);
        leversList.add(lever5);
        return enemies;
    }
    
    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        HorizontalMovingPlatform hmp = new HorizontalMovingPlatform(
                ImageLoader.load("GreenPlatform.png"),
                getMapTile(45, 23).getLocation(),
                getMapTile(64, 23).getLocation(),
                TileType.JUMP_THROUGH_PLATFORM,
                1.4f,
                new Rectangle(0, 6,16,4),
                Direction.RIGHT
        );  
        hmp.setIsUpdateOffScreen(true);
        
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
        //enhancedMapTiles.add(hmp3);   
        //enhancedMapTiles.add(hmp4);
        //enhancedMapTiles.add(hmp5);
        

        //ammo boxes
        SingleAmmo singleAmmo = new SingleAmmo(getMapTile(1, 7).getLocation());
        enhancedMapTiles.add(singleAmmo);
        TwelveAmmo twelveAmmo = new TwelveAmmo(getMapTile(52, 2).getLocation());
        enhancedMapTiles.add(twelveAmmo);

        Coin coin1 = new Coin(getMapTile(5, 8).getLocation());
		enhancedMapTiles.add(coin1);

		Coin coin2 = new Coin(getMapTile(9, 5).getLocation());
		enhancedMapTiles.add(coin2);

		Coin coin3 = new Coin(getMapTile(7, 13).getLocation());
		enhancedMapTiles.add(coin3);
        Coin coin4 = new Coin(getMapTile(15, 15).getLocation());
		enhancedMapTiles.add(coin4);
        Coin coin5 = new Coin(getMapTile(5, 18).getLocation());
		enhancedMapTiles.add(coin5);
        Coin coin6 = new Coin(getMapTile(9, 23).getLocation());
		enhancedMapTiles.add(coin6);
        Coin coin7 = new Coin(getMapTile(24,24).getLocation());
		enhancedMapTiles.add(coin7);
         
        Coin coin8 = new Coin(getMapTile(21, 15 ).getLocation());
		enhancedMapTiles.add(coin8);
        Coin coin9 = new Coin(getMapTile(31, 15).getLocation());
		enhancedMapTiles.add(coin9);
        Coin coin10 = new Coin(getMapTile(39, 4).getLocation());
		enhancedMapTiles.add(coin10);
        Coin coin11 = new Coin(getMapTile(52, 3).getLocation());
		enhancedMapTiles.add(coin11);
        Coin coin12 = new Coin(getMapTile(51, 2).getLocation());
		enhancedMapTiles.add(coin12);
        Coin coin13 = new Coin(getMapTile(53, 2).getLocation());
		enhancedMapTiles.add(coin13);
        Coin coin14 = new Coin(getMapTile(52, 1).getLocation());
		enhancedMapTiles.add(coin14);
        Coin coin15 = new Coin(getMapTile(65, 14).getLocation());
		enhancedMapTiles.add(coin15);

        EndLevelBox endLevelBox = new EndLevelBox(getMapTile(69, 4).getLocation());
        enhancedMapTiles.add(endLevelBox);
        EndLevelBox endLevelBox1 = new EndLevelBox(getMapTile(69, 5).getLocation());
        enhancedMapTiles.add(endLevelBox1);
        EndLevelBox endLevelBox2 = new EndLevelBox(getMapTile(69, 6).getLocation());
        enhancedMapTiles.add(endLevelBox2);
        EndLevelBox endLevelBox3 = new EndLevelBox(getMapTile(69, 7).getLocation());
        enhancedMapTiles.add(endLevelBox3);

        for (Lever currentLever : leversList) {
            for (EnhancedMapTile currentTile : currentLever.getEnhancedMapTiles()) {
                if(currentTile instanceof DoorBlock){
                    enhancedMapTiles.add(currentTile);
                }
            }
        }
        
        
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
