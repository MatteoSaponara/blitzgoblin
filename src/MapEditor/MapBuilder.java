package MapEditor;

import Level.Map;
import Level.MapTile;
import Tilesets.CommonEntityTileset;
import Utils.Colors;
import java.awt.*;
import javax.swing.*;
public class MapBuilder extends JPanel {
    private static Map map;
    private static MapTile[] currentEntityMapTiles;
    private static JScrollPane tileBuilderScroll;
    private static TileBuilder tileBuilder;
    private static JLabel mapWidthLabel;
    private static JLabel mapHeightLabel;
    private static JLabel hoveredTileIndexLabel;
    private static JLabel currentlyViewedLayerLabel;

    public MapBuilder(SelectedTileIndexHolder controlPanelHolder) {
        setBackground(Colors.CORNFLOWER_BLUE);
        setLocation(205, 5);
        setLayout(new BorderLayout());

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(null);
        labelPanel.setPreferredSize(new Dimension(200, 30));
        labelPanel.setBackground(Colors.CORNFLOWER_BLUE);
        mapWidthLabel = new JLabel("Width: ");
        mapWidthLabel.setSize(70, 20);
        mapWidthLabel.setLocation(2, 5);
        labelPanel.add(mapWidthLabel);
        mapHeightLabel = new JLabel("Height: ");
        mapHeightLabel.setSize(70, 20);
        mapHeightLabel.setLocation(76, 5);
        labelPanel.add(mapHeightLabel);
        hoveredTileIndexLabel = new JLabel("X: , Y:");
        hoveredTileIndexLabel.setSize(140, 20);
        hoveredTileIndexLabel.setLocation(152, 5);
        labelPanel.add(hoveredTileIndexLabel);
        currentlyViewedLayerLabel = new JLabel("Editing Layer: 0/2");
        currentlyViewedLayerLabel.setSize(120, 20);
        currentlyViewedLayerLabel.setLocation(280, 5);
        labelPanel.add(currentlyViewedLayerLabel);
        add(labelPanel, BorderLayout.SOUTH);

        tileBuilder = new TileBuilder(controlPanelHolder, hoveredTileIndexLabel);
        tileBuilderScroll = new JScrollPane();
        tileBuilderScroll.setViewportView(tileBuilder);
        scrollToMaxY();
        tileBuilderScroll.setLocation(0, 0);
        tileBuilderScroll.setSize(585, 546);
        add(tileBuilderScroll, BorderLayout.CENTER);
    }

    public void setMap(Map map) {
        this.map = map;
        refreshTileBuilder();
    }
    public void setEntityMapTiles(MapTile[] inputMapTiles) {
        this.currentEntityMapTiles = inputMapTiles;
        refreshTileBuilder();
    }

    public static void refreshTileBuilder() {
        tileBuilder.setMap(map);
        tileBuilder.setEntityMapTiles(currentEntityMapTiles);
        tileBuilder.setEntityTileset(new CommonEntityTileset());
        tileBuilderScroll.setViewportView(tileBuilder);
        tileBuilderScroll.getVerticalScrollBar().setValue(tileBuilderScroll.getVerticalScrollBar().getMaximum());
        mapWidthLabel.setText("Width: " + map.getWidth());
        mapHeightLabel.setText("Height: " + map.getHeight());
        currentlyViewedLayerLabel.setText("Editing Layer: "+tileBuilder.getCurrentlyViewedLayer()+"/"+(map.getCurrentMapLayerNumber()-1));
    }

    public void scrollToMaxY() {
        tileBuilderScroll.getVerticalScrollBar().setValue(tileBuilderScroll.getVerticalScrollBar().getMaximum());
    }

    public TileBuilder getTileBuilder() { return tileBuilder; }

    public static MapTile[] createEmptyVersionOfInputMapTiles(MapTile[] inputTiles){
        MapTile[] resultTiles = new MapTile[inputTiles.length];
        for (int i = 0; i < resultTiles.length; i++) {
            resultTiles[i] = inputTiles[i].copyEmpty();
        }
        return resultTiles;
    }
}
