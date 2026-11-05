package Maps;

import EnhancedMapTiles.Coin;
import EnhancedMapTiles.EndLevelBox;
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
        int[][] coinLocations = {
            {4, 10}, {7, 10}, {10, 10}, {13, 10}
        };
        for (int[] location : coinLocations) {
            enhancedMapTiles.add(new Coin(getMapTile(location[0], location[1]).getLocation()));
        }
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
