package MapEditor;

import Level.Map;
import Maps.MatteosTestMap;
import Maps.TestMap;
import Maps.TestMapBackup;
import Maps.TitleScreenMap;

import java.util.ArrayList;

public class EditorMaps {
    public static ArrayList<String> getMapNames() {
        return new ArrayList<String>() {{
            add("TestMap");
            add("TitleScreen");
            add("MatteosTestMap");
            add("TestMapBackup");
        }};
    }

    public static Map getMapByName(String mapName) {
        switch(mapName) {
            case "TestMap":
                return new TestMap();
            case "TitleScreen":
                return new TitleScreenMap();
            case "MatteosTestMap":
                return new MatteosTestMap();
            case "TestMapBackup":
                return new TestMapBackup();
            default:
                throw new RuntimeException("Unrecognized map name");
        }
    }
}
