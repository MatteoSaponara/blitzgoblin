package MapEditor;

import Level.MapTile;
import java.awt.*;
import javax.swing.*;

public class EditorMainPanel extends JPanel {
    private EditorControlPanel editorControlPanel;
    private MapBuilder mapBuilder;
    public EditorEntityControlPanel editorEntityControlPanel;

    public EditorMainPanel(JFrame parent) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        SelectedTileIndexHolder selectedTileIndexHolder = new SelectedTileIndexHolder();
        mapBuilder = new MapBuilder(selectedTileIndexHolder);
        add(mapBuilder, BorderLayout.CENTER);
        editorControlPanel = new EditorControlPanel(selectedTileIndexHolder, mapBuilder, parent);
        editorEntityControlPanel = new EditorEntityControlPanel(selectedTileIndexHolder, mapBuilder, parent);
        mapBuilder.setMap(editorControlPanel.getSelectedMap());
        if (!editorControlPanel.getSelectedMap().getHasEntities()) {
            mapBuilder.setEntityMapTiles(MapBuilder.createEmptyVersionOfInputMapTiles(editorControlPanel.getSelectedMap().getMapTiles()));
        }
        else {
            MapTile[] newEnemyMaptiles = MapBuilder.createEmptyVersionOfInputMapTiles(editorControlPanel.getSelectedMap().getMapTiles());
            for (MapTile tileIN : editorControlPanel.getSelectedMap().getEntityMapTiles()) {
                if(tileIN == null)
                    continue;
                newEnemyMaptiles[Math.round(tileIN.getY()/(Engine.Config.TILE_ART_SIZE*Engine.Config.TILE_SCALE)) * editorControlPanel.getSelectedMap().getWidth() +
                    Math.round(tileIN.getX()/(Engine.Config.TILE_ART_SIZE*Engine.Config.TILE_SCALE))] = tileIN;
            }
            mapBuilder.setEntityMapTiles(newEnemyMaptiles);
        }

        add(editorControlPanel, BorderLayout.WEST);
        add(editorEntityControlPanel, BorderLayout.EAST);
    }

    public EditorControlPanel getEditorControlPanel() { return editorControlPanel; }
    public MapBuilder getMapBuilder() { return mapBuilder; }
}