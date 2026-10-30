package Level;

import Enemies.BugEnemy;
import Enemies.CoinMimicEnemy;
import Enemies.GargoyleEnemy;
import Enemies.GoblinEnemy;
import Enemies.HeroEnemy;
import Enemies.Turret;
import Engine.Config;
import Engine.GraphicsHandler;
import Engine.ScreenManager;
import EnhancedMapTiles.CannonAmmo;
import EnhancedMapTiles.Coin;
import EnhancedMapTiles.EndLevelBox;
import EnhancedMapTiles.SingleAmmo;
import EnhancedMapTiles.TwelveAmmo;
import Tilesets.CommonEntityTileset;
import Utils.Direction;
import Utils.Point;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/*
    This class is for defining a map that is used for a specific level
    The map class handles/manages a lot of different things, including:
    1. tile map -- the map tiles that make up the map
    2. entities in the map -- this includes enemies, enhanced map tiles, and npcs
    3. the map's camera, which does a lot of work itself in the Camera class
    4. adjusting camera location based off of player location
    5. calculating which tile a game object is currently on based on its x and y location
*/

public abstract class Map {
    // the tile map (map tiles that make up the entire map image)
    protected MapTile[][] mapTiles;
    protected MapTile[] entityMapTiles;

    // width and height of the map in terms of the number of tiles width-wise and height-wise
    protected int width;
    protected int height;

    // the tileset this map uses for its map tiles
    protected Tileset tileset;

    // camera class that handles the viewable part of the map that is seen by the player of a game during a level
    protected Camera camera;

    // location player should start on when this map is first loaded
    protected Point playerStartPosition;

    // the location of the "mid point" of the screen
    // this is what tells the game that the player has reached the center of the screen, therefore the camera should move instead of the player
    // this goes into creating that "map scrolling" effect
    protected int xMidPoint, yMidPoint;

    // in pixels, this basically creates a rectangle defining how big the map is
    // startX and Y will always be 0, endX and Y is the number of tiles multiplied by the number of pixels each tile takes up
    protected int startBoundX, startBoundY, endBoundX, endBoundY;

    // the name of the map text file that has the tile map information
    protected String mapFileName;

    // lists to hold map entities that are a part of the map
    protected ArrayList<Enemy> enemies;
    protected ArrayList<Enemy> enemyQueue = new ArrayList<Enemy>();
    protected ArrayList<EnhancedMapTile> enhancedMapTiles;
    protected ArrayList<NPC> npcs;

    protected boolean hasEntities = false;

    public boolean getHasEntities() { return hasEntities;}

    // if set to false, camera will not move as player moves
    protected boolean adjustCamera = true;

    // number of layers to render
    protected int numLayers = 2;

    // visual layer the player appears in
    protected int currentGameplayLayer = 0;

    public Color mapBackgroundColor = new Color(44, 17, 61);
    
    private static final float CAMERA_FOLLOW_SMOOTHING = 0.2f;
    // map tiles in map that are animated
    protected ArrayList<MapTile> animatedMapTiles;

    public Map(String mapFileName, Tileset tilesetIN) {
        this.mapFileName = mapFileName;
        this.tileset = tilesetIN;
        setupMap();
        this.startBoundX = 0;
        this.startBoundY = 0;
        this.endBoundX = width * tilesetIN.getScaledSpriteWidth();
        this.endBoundY = height * tilesetIN.getScaledSpriteHeight();
        this.xMidPoint = ScreenManager.getScreenWidth() / 2;
        this.yMidPoint = (ScreenManager.getScreenHeight() / 2);
        this.playerStartPosition = new Point(0, 0);
    }
    public Map(String mapFileName, Tileset tilesetIN, int startingGameplayLayerIN) {
        this.mapFileName = mapFileName;
        this.tileset = tilesetIN;
        this.currentGameplayLayer = startingGameplayLayerIN;
        setupMap();
        this.startBoundX = 0;
        this.startBoundY = 0;
        this.endBoundX = width * tilesetIN.getScaledSpriteWidth();
        this.endBoundY = height * tilesetIN.getScaledSpriteHeight();
        this.xMidPoint = ScreenManager.getScreenWidth() / 2;
        this.yMidPoint = (ScreenManager.getScreenHeight() / 2);
        this.playerStartPosition = new Point(0, 0);
    }

    // sets up map by reading in the map file to create the tile map
    // loads in enemies, enhanced map tiles, and npcs
    // and instantiates a Camera
    public void setupMap() {
        this.animatedMapTiles = new ArrayList<>();
        this.enemyQueue = new ArrayList<Enemy>();
        loadMapFile();

        this.enemies = loadEnemies();
        for (Enemy enemy: this.enemies) {
            enemy.setMap(this);
        }

        this.enhancedMapTiles = loadEnhancedMapTiles();
        for (EnhancedMapTile enhancedMapTile: this.enhancedMapTiles) {
            enhancedMapTile.setMap(this);
        }

        this.npcs = loadNPCs();
        for (NPC npc: this.npcs) {
            npc.setMap(this);
        }

        this.camera = new Camera(0, 0, tileset.getScaledSpriteWidth(), tileset.getScaledSpriteHeight(), this);
    }

    // reads in a map file to create the map's tilemap
    private void loadMapFile() {
        Scanner fileInput;
        try {
            // open map file that is located in the MAP_FILES_PATH directory
            fileInput = new Scanner(new File(Config.MAP_FILES_PATH + this.mapFileName));
        } catch(FileNotFoundException ex) {
            // if map file does not exist, create a new one for this map (the map editor uses this)
            System.out.println("Map file " + Config.MAP_FILES_PATH + this.mapFileName + " not found! Creating empty map file...");

            try {
                createEmptyMapFile();
                fileInput = new Scanner(new File(Config.MAP_FILES_PATH + this.mapFileName));
            } catch(IOException ex2) {
                ex2.printStackTrace();
                System.out.println("Failed to create an empty map file!");
                throw new RuntimeException();
            }
        }   

        // read in map width and height from the first line of map file
        this.width = fileInput.nextInt();
        this.height = fileInput.nextInt();
        this.numLayers = fileInput.nextInt();

        // define array size for map tiles, which is width * height (this is a standard array, NOT a 2D array)
        this.mapTiles = new MapTile[this.height * this.width][numLayers];
        this.entityMapTiles = new MapTile[this.height * this.width];
        fileInput.nextLine();

        // read in each tile index from the map file, use the defined tileset to get the associated MapTile to that tileset, and place it in the array
        for (int i = 0; i < (height*numLayers); i++) {
            for (int j = 0; j < width; j++) {
                int tileIndex = fileInput.nextInt();
                int xLocation = j * tileset.getScaledSpriteWidth();
                int yLocation = (i%height) * tileset.getScaledSpriteHeight();
                MapTile tile = tileset.getTile(tileIndex).build(xLocation, yLocation, (i/height), currentGameplayLayer);
                tile.setMap(this);
                setMapTile(j, i%height, (i/height), tile);
                if((fileInput.hasNext(":"))) {
                    hasEntities = true;
                    fileInput.next();
                    int testint = fileInput.nextInt();
                    MapTile testd = new CommonEntityTileset()
                    .getTile(testint)
                    .build(xLocation, yLocation);

                    if(testint > 0){
                        System.out.println("adding " +testd.getTileType() + testd.getTileIndex() +"\nat : ("+
                        String.valueOf((testd.getX()/(Config.TILE_ART_SIZE*Config.TILE_SCALE)))+", "
                    + String.valueOf((testd.getY()/(Config.TILE_ART_SIZE*Config.TILE_SCALE)))+")");
                        entityMapTiles[((i*width) + j) % (height*width)] = testd;
                        switch (testint) {
                            case 1:
                                enemyQueue.add(new GoblinEnemy(new Point(testd.getX(), testd.getY()),
                                 getCurrentGameplayLayer(),
                                 getCurrentGameplayLayer(),
                                Direction.LEFT));
                                break;
                            case 2:
                                enemyQueue.add(new BugEnemy(new Point(testd.getX(), testd.getY()),
                                getCurrentGameplayLayer(),
                                getCurrentGameplayLayer(),
                                Direction.LEFT));
                                break;
                            case 3:
                                enemyQueue.add(new Turret(new Point(testd.getX(), testd.getY()),
                                getCurrentGameplayLayer(),
                                getCurrentGameplayLayer(),
                                Direction.LEFT));
                                break;
                            default:
                                throw new AssertionError();
                        }
                    }
                }
                    
                if (tile.isAnimated()) {
                    animatedMapTiles.add(tile);
                }
            }
        }

        fileInput.close();
    }

    // creates an empty map file for this map if one does not exist
    // defaults the map dimensions to 0x0
    private void createEmptyMapFile() throws IOException {
        FileWriter fileWriter = null;
        fileWriter = new FileWriter(Config.MAP_FILES_PATH + this.mapFileName);
        fileWriter.write("0 0\n");
        fileWriter.close();
    }

    // gets player start position based on player start tile (basically the start tile's position on the map)
    public Point getPlayerStartPosition() {
        return playerStartPosition;
    }

    // get position on the map based on a specfic tile index
    public Point getPositionByTileIndex(int xIndex, int yIndex) {
        MapTile tile = getMapTile(xIndex, yIndex);
        return new Point(tile.getX(), tile.getY());
    }

    public Tileset getTileset() {
        return tileset;
    }

    public String getMapFileName() {
        return mapFileName;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidthPixels() {
        return width * tileset.getScaledSpriteWidth();
    }

    public int getHeightPixels() {
        return height * tileset.getScaledSpriteHeight();
    }
    public void setAllMapTiles(MapTile[][] inputMapTiles) {
        mapTiles = inputMapTiles;
    }

    public MapTile[] setMapTiles(MapTile[] inputMapTiles) {
        for (int i = 0; i < mapTiles.length; i++) {
            mapTiles[i][currentGameplayLayer] = inputMapTiles[i];
        }
        return getMapTiles();
    }

    public MapTile[] getMapTiles(int accessedLayerIN) {
        MapTile[] resultMapTile = new MapTile[this.height * this.width]; 
        
        for (int i = 0; i < mapTiles.length; i++) {
            if((accessedLayerIN < mapTiles[i].length))
                resultMapTile[i] = mapTiles[i][accessedLayerIN];
        }
        return resultMapTile;
    }

    public MapTile[] getMapTiles() {
        return getMapTiles(currentGameplayLayer);
    }
    public MapTile[][] getAllMapTiles(){
        return mapTiles;
    }   
    public MapTile[] getEntityMapTiles() { return entityMapTiles; }

    

    /*public void setMapTiles(MapTile[] mapTiles) {
        this.mapTiles = mapTiles;
    }*/

    // get specific map tile from tile map

    public MapTile getMapTile(int x, int y) {
        if (isInBounds(x, y)) {
            return mapTiles[getConvertedIndex(x, y)][currentGameplayLayer];
        } else {
            return null;
        }
    }

    public MapTile getMapTile(int x, int y, int accessedLayerIn) {
        if (isInBounds(x, y)) {
            return mapTiles[getConvertedIndex(x, y)][accessedLayerIn];
        } else {
            return null;
        }
    }

    // set specific map tile from tile map to a new map tile
    public void setMapTile(int x, int y, int accessedLayerIn, MapTile tile) {
        if (isInBounds(x, y)) {
            MapTile oldMapTile = getMapTile(x, y);
            animatedMapTiles.remove(oldMapTile);
            mapTiles[getConvertedIndex(x, y)][accessedLayerIn] = tile;
            if (tile.isAnimated()) {
                animatedMapTiles.add(tile);
            }
        }
    }
    
    // set specific map tile from tile map to a new map tile
    public void setMapTile(int x, int y, MapTile tile) {
        setMapTile(x, y, currentGameplayLayer, tile);
    }

    public int getCurrentGameplayLayer() {return currentGameplayLayer;}

    public int getCurrentMapLayerNumber() {return numLayers;}


    public void setCurrentGameplayLayer(int currentGameplayLayer) { this.currentGameplayLayer = currentGameplayLayer; }

    public void setCurrentMapLayerNumber(int newNumLayers) {this.numLayers = newNumLayers;}
    // returns a tile based on a position in the map
    public MapTile getTileByPosition(float xPosition, float yPosition) {
        Point tileIndex = getTileIndexByPosition(xPosition, yPosition);
        if (isInBounds(Math.round(tileIndex.x), Math.round(tileIndex.y))) {
            return getMapTile(Math.round(tileIndex.x), Math.round(tileIndex.y));
        } else {
            return null;
        }
    }

    // returns the index of a tile (x index and y index) based on a position in the map
    public Point getTileIndexByPosition(float xPosition, float yPosition) {
        int xIndex = Math.round(xPosition) / tileset.getScaledSpriteWidth();
        int yIndex = Math.round(yPosition) / tileset.getScaledSpriteHeight();
        return new Point(xIndex, yIndex);
    }

    // checks if map tile being requested is in bounds of the tile map array
    private boolean isInBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    // since tile map array is a standard (1D) array and not a 2D,
    // instead of doing [y][x] to get a value, instead the same can be achieved with x + width * y
    private int getConvertedIndex(int x, int y) {
        return x + (width * y);
    }

    // list of enemies defined to be a part of the map, should be overridden in a subclass
    protected ArrayList<Enemy> loadEnemies() {
        for (Enemy enem: enemyQueue) {
            this.enemies.add(enem);
        }
        return new ArrayList<>();
    }

    // list of enhanced map tiles defined to be a part of the map, should be overridden in a subclass
    protected ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        return new ArrayList<>();
    }

    // list of npcs defined to be a part of the map, should be overridden in a subclass
    protected ArrayList<NPC> loadNPCs() {
        return new ArrayList<>();
    }

    public Camera getCamera() {
        return camera;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
    public ArrayList<EnhancedMapTile> getEnhancedMapTiles() {
        return enhancedMapTiles;
    }
    public ArrayList<NPC> getNPCs() {
        return npcs;
    }

    public ArrayList<MapTile> getAnimatedMapTiles() {
        return animatedMapTiles;
    }
     
    public Enemy spawnEnemy(Class<?> enemyClassIN, int x, int y,  Direction directionIN){
        
        Enemy enemyOut;
        if(enemyClassIN == GoblinEnemy.class) {
            enemyOut = new GoblinEnemy(getMapTile(x, y).getLocation().subtractY(25), currentGameplayLayer, currentGameplayLayer, directionIN);
        }
        else if(enemyClassIN == BugEnemy.class){
            enemyOut = new BugEnemy(getMapTile(x, y).getLocation().subtractY(25), currentGameplayLayer, currentGameplayLayer, directionIN);
        }
        else if(enemyClassIN == Turret.class){
            enemyOut = new Turret(getMapTile(x, y).getLocation().addY(2), currentGameplayLayer, currentGameplayLayer, directionIN);
        }
        else if(enemyClassIN == CoinMimicEnemy.class){
            enemyOut = new CoinMimicEnemy(getMapTile(x, y).getLocation(), currentGameplayLayer, currentGameplayLayer, directionIN);
        }
        else if(enemyClassIN == GargoyleEnemy.class){
            enemyOut = new GargoyleEnemy(getMapTile(x, y).getLocation(), currentGameplayLayer, currentGameplayLayer, directionIN);
        }
        else if(enemyClassIN == HeroEnemy.class){
            enemyOut = new HeroEnemy(getMapTile(x, y).getLocation(), currentGameplayLayer, currentGameplayLayer, directionIN);
        }
        else {
            enemyOut = new BugEnemy(getMapTile(x, y).getLocation(), currentGameplayLayer, currentGameplayLayer, directionIN);
        }
        return enemyOut;
    }
    public EnhancedMapTile spawnEnhancedMapTile(Class<?> enhancedMapTileIN, int x, int y){
        EnhancedMapTile enhancedMapTileOut;
        if(enhancedMapTileIN == CannonAmmo.class) {
            enhancedMapTileOut = new CannonAmmo(getMapTile(x, y).getLocation(), currentGameplayLayer);
        }
        else if(enhancedMapTileIN == SingleAmmo.class){
            enhancedMapTileOut = new SingleAmmo(getMapTile(x, y).getLocation(), currentGameplayLayer);
        }
        else if(enhancedMapTileIN == TwelveAmmo.class){
            enhancedMapTileOut = new TwelveAmmo(getMapTile(x, y).getLocation(), currentGameplayLayer);
        }
        else if(enhancedMapTileIN == EndLevelBox.class) {
            enhancedMapTileOut = new EndLevelBox(getMapTile(x, y).getLocation(), currentGameplayLayer);
        }
        else if (enhancedMapTileIN == Coin.class) {
          enhancedMapTileOut = new Coin(getMapTile(x, y).getLocation(), currentGameplayLayer);
        }
        else {
            enhancedMapTileOut = new TwelveAmmo(getMapTile(x, y).getLocation(), currentGameplayLayer);
        }
        return enhancedMapTileOut;
    }

    // returns all active enemies (enemies that are a part of the current update cycle) -- this changes every frame by the Camera class
    public ArrayList<Enemy> getActiveEnemies() {
        return camera.getActiveEnemies();
    }

    // returns all active enhanced map tiles (enhanced map tiles that are a part of the current update cycle) -- this changes every frame by the Camera class
    public ArrayList<EnhancedMapTile> getActiveEnhancedMapTiles() {
        return camera.getActiveEnhancedMapTiles();
    }

    // returns all active npcs (npcs that are a part of the current update cycle) -- this changes every frame by the Camera class
    public ArrayList<NPC> getActiveNPCs() {
        return camera.getActiveNPCs();
    }

    // add an enemy to the map's list of enemies
    public void addEnemy(Enemy enemy) {
        enemy.setMap(this);
        this.enemies.add(enemy);
    }

    // add an enhanced map tile to the map's list of enhanced map tiles
    public void addEnhancedMapTile(EnhancedMapTile enhancedMapTile) {
        enhancedMapTile.setMap(this);
        this.enhancedMapTiles.add(enhancedMapTile);
    }

    // add an npc to the map's list of npcs
    public void addNPC(NPC npc) {
        npc.setMap(this);
        this.npcs.add(npc);
    }

    public void setAdjustCamera(boolean adjustCamera) {
        this.adjustCamera = adjustCamera;
    }

    public void centerCameraOnPlayer(Player player) {
        float cameraX = player.getX() + player.getWidth() / 2f - xMidPoint;
        float cameraY = player.getY() + player.getHeight() / 2f - yMidPoint;
        float maxCameraX = Math.max(startBoundX, endBoundX - camera.getEndBoundX());
        float maxCameraY = Math.max(startBoundY, endBoundY - camera.getEndBoundY());

        camera.setLocation(
                Math.max(startBoundX, Math.min(cameraX, maxCameraX)),
                Math.max(startBoundY, Math.min(cameraY, maxCameraY)));
    }

    public void update(Player player) {
        if (adjustCamera) {
            adjustMovementY(player);
            adjustMovementX(player);
        }
        camera.update(player);
    }
    

    // based on the player's current X position (which in a level can potentially be updated each frame),
    // adjust the player's and camera's positions accordingly in order to properly create the map "scrolling" effect
    private void adjustMovementX(Player player) {
        // if player goes past center screen (on the right side) and there is more map to show on the right side, push player back to center and move camera forward
        if ((player.getCalibratedXLocation() + (player.getWidth() / 2)) > xMidPoint && camera.getEndBoundX() < endBoundX) {
            float xMidPointDifference = xMidPoint - (player.getCalibratedXLocation() + (player.getWidth() / 2));
            camera.moveX(-xMidPointDifference * CAMERA_FOLLOW_SMOOTHING);

            // if camera moved past the right edge of the map as a result from the move above, move camera back and push player forward
            if (camera.getEndBoundX() > endBoundX) {
                float cameraDifference = camera.getEndBoundX() - endBoundX;
                camera.moveX(-cameraDifference);
            }
        }
        // if player goes past center screen (on the left side) and there is more map to show on the left side, push player back to center and move camera backwards
        else if ((player.getCalibratedXLocation() + (player.getWidth() / 2)) < xMidPoint && camera.getX() > startBoundX) {
            float xMidPointDifference = xMidPoint - (player.getCalibratedXLocation() + (player.getWidth() / 2));
            camera.moveX(-xMidPointDifference * CAMERA_FOLLOW_SMOOTHING);

            // if camera moved past the left edge of the map as a result from the move above, move camera back and push player backward
            if (camera.getX() < startBoundX) {
                float cameraDifference = startBoundX - camera.getX();
                camera.moveX(cameraDifference);
            }
        }
    }

    // based on the player's current Y position (which in a level can potentially be updated each frame),
    // adjust the player's and camera's positions accordingly in order to properly create the map "scrolling" effect
    private void adjustMovementY(Player player) {
        // if player goes past center screen (below) and there is more map to show below, push player back to center and move camera upward
        if ((player.getCalibratedYLocation() + (player.getHeight() / 2)) > yMidPoint && camera.getEndBoundY() < endBoundY) {
            float yMidPointDifference = yMidPoint - (player.getCalibratedYLocation() + (player.getHeight() / 2));
            camera.moveY(-yMidPointDifference * CAMERA_FOLLOW_SMOOTHING);

            // if camera moved past the bottom of the map as a result from the move above, move camera upwards and push player downwards
            if (camera.getEndBoundY() > endBoundY) {
                float cameraDifference = camera.getEndBoundY() - endBoundY;
                camera.moveY(-cameraDifference);
            }
        }
        // if player goes past center screen (above) and there is more map to show above, push player back to center and move camera upwards
        else if ((player.getCalibratedYLocation() + (player.getHeight() / 2)) < yMidPoint && camera.getY() > startBoundY) {
            float yMidPointDifference = yMidPoint - (player.getCalibratedYLocation() + (player.getHeight() / 2));
            camera.moveY(-yMidPointDifference * CAMERA_FOLLOW_SMOOTHING);

            // if camera moved past the top of the map as a result from the move above, move camera downwards and push player upwards
            if (camera.getY() < startBoundY) {
                float cameraDifference = startBoundY - camera.getY();
                camera.moveY(cameraDifference);
            }
        }
    }

    public void reset() {
        setupMap();
    }

    public void draw(GraphicsHandler graphicsHandler) {
        camera.draw(graphicsHandler);
    }
    public void draw(GraphicsHandler graphicsHandler, boolean renderAboveGameplay) {
        camera.draw(graphicsHandler, renderAboveGameplay);
    }

    public void drawDebugHitboxes(GraphicsHandler graphicsHandler, Player player) {
        camera.drawDebugHitboxes(graphicsHandler, player);
    }

    public int getEndBoundX() { return endBoundX; }
    public int getEndBoundY() { return endBoundY; }
}
