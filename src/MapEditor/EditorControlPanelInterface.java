package MapEditor;

import Level.Map;
import java.util.ArrayList;
import javax.swing.*;

public interface EditorControlPanelInterface  {
    ArrayList<String> mapNames = null;
    JComboBox<String> mapNamesComboBox = null;
    TilePicker tilePicker = null;
    MapBuilder mapBuilder = null;
    Map selectedMap = null;
    EditorControlPanel thisEditorControlPanel = null;

    Map getSelectedMap();
    void setMap();
}
