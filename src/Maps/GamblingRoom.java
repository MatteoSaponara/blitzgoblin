package Maps;

import EnhancedMapTiles.EndLevelBox;
import EnhancedMapTiles.SlotMachine;
import Level.Enemy;
import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Tilesets.CommonTileset;
import java.util.ArrayList;

public class GamblingRoom extends Map {

    public GamblingRoom() {
        super("GamblingRoom.txt", new CommonTileset());
        playerStartPosition = getMapTile(2, 10).getLocation();
    }

    @Override
    public ArrayList<Enemy> loadEnemies() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();
        enhancedMapTiles.add(new SlotMachine(getMapTile(7, 8).getLocation()));
        enhancedMapTiles.add(new EndLevelBox(getMapTile(15, 9).getLocation()));
        enhancedMapTiles.add(new EndLevelBox(getMapTile(15, 10).getLocation()));
        enhancedMapTiles.add(new EndLevelBox(getMapTile(15, 11).getLocation()));
        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        return new ArrayList<>();
    }
}
