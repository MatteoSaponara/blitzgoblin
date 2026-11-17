package MapEditor;

import Level.Map;
import Tilesets.CommonEntityTileset;
import Utils.Colors;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

public class EditorEntityControlPanel extends JPanel implements EditorControlPanelInterface { 
    private ArrayList<String> mapNames;
    private JComboBox<String> mapNamesComboBox;
    private TilePicker tilePicker;
    private MapBuilder mapBuilder;
    private Map selectedMap;
    private EditorControlPanel thisEditorControlPanel;

    public EditorEntityControlPanel(SelectedTileIndexHolder selectedTileIndexHolder, MapBuilder mapBuilder, JFrame parent) {
        thisEditorControlPanel = EditorControlPanel.thisEditorControlPanel;
        setLayout(new BorderLayout());
        setBackground(Colors.CORNFLOWER_BLUE);
        setLocation(0, 0);
        setPreferredSize(new Dimension(200, 600));

        mapNames = EditorMaps.getMapNames();

        this.mapBuilder = mapBuilder;

        JPanel mapChoosePanel = new JPanel();
        mapChoosePanel.setLayout(null);
        mapChoosePanel.setPreferredSize(new Dimension(200, 80));
        mapChoosePanel.setBackground(Colors.CORNFLOWER_BLUE);

        /*JLabel mapLabel = new JLabel();
        mapLabel.setLocation(5, 0);
        mapLabel.setText("Choose a Map:");
        mapLabel.setSize(100, 40);
        mapChoosePanel.add(mapLabel);*/

        mapNamesComboBox = new JComboBox<String>();
        mapNamesComboBox.setSize(190, 40);
        mapNamesComboBox.setLocation(5, 30);
        mapNames.sort(String::compareToIgnoreCase);
        for (String mapName : mapNames) {
            mapNamesComboBox.addItem(mapName);
        }
        mapNamesComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMap();
            }
        });
        mapChoosePanel.add(mapNamesComboBox);
        selectedMap = EditorMaps.getMapByName(mapNamesComboBox.getSelectedItem().toString());

        add(mapChoosePanel, BorderLayout.NORTH);

        tilePicker = new TilePicker(selectedTileIndexHolder);
        JScrollPane tilePickerScroll = new JScrollPane();
        tilePickerScroll.setViewportView(tilePicker);
        tilePickerScroll.setLocation(5, 78);
        tilePickerScroll.setSize(190, 394);
        add(tilePickerScroll, BorderLayout.CENTER);
        tilePicker.setTileset(getSelectedMap(), new CommonEntityTileset());


        JPanel mapButtonsPanel = new JPanel();
        mapButtonsPanel.setLayout(null);
        mapButtonsPanel.setPreferredSize(new Dimension(200, 95));
        mapButtonsPanel.setBackground(Colors.CORNFLOWER_BLUE);

        

        add(mapButtonsPanel, BorderLayout.SOUTH);
    }

    public Map getSelectedMap() {
        return selectedMap;
    }

    public void setMap() {
        selectedMap = EditorMaps.getMapByName(mapNamesComboBox.getSelectedItem().toString());
        tilePicker.setTileset(selectedMap, new CommonEntityTileset());
        mapBuilder.setMap(selectedMap);
    }
}
