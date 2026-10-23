package MapEditor;

import Level.Map;
import Level.MapTile;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MenuBar extends JMenuBar {
    JMenu options;
    JCheckBoxMenuItem showNpcs;
    JCheckBoxMenuItem showEnchancedMapTiles;
    JCheckBoxMenuItem showTriggers;
    JCheckBoxMenuItem toggleEntityEditingMode;
    JButton incrementCurrentlyViewedLayer;
    JButton decrementCurrentlyViewedLayer;
    JButton addLayer;
    JButton removeTopMostLayer;

    public MenuBar(TileBuilder tileBuilder) {
        options = new JMenu("Options");
        showNpcs = new JCheckBoxMenuItem("Show NPCs");
        showNpcs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tileBuilder.setShowNPCs(!tileBuilder.getShowNPCs());
            }
        });
        options.add(showNpcs);
        showEnchancedMapTiles = new JCheckBoxMenuItem("Show Enhanced Map Tiles");
        showEnchancedMapTiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tileBuilder.setShowEnhancedMapTiles(!tileBuilder.getShowEnhancedMapTiles());
            }
        });
        options.add(showEnchancedMapTiles);
        showTriggers = new JCheckBoxMenuItem("Show Enemies");
        showTriggers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tileBuilder.setShowEnemies(!tileBuilder.getShowEnemies());
            }
        });
        options.add(showTriggers);
        toggleEntityEditingMode = new JCheckBoxMenuItem("Toggle Entity Editing Mode");
        toggleEntityEditingMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tileBuilder.setShowEnhancedMapTiles(!tileBuilder.getShowEnhancedMapTiles());
            }
        });
        options.add(showEnchancedMapTiles);
        incrementCurrentlyViewedLayer = new JButton("Go Up a Layer");
        incrementCurrentlyViewedLayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tileBuilder.setCurrentlyViewedLayer((int)Math.clamp(tileBuilder.getCurrentlyViewedLayer() + 1, 0, tileBuilder.getMap().getCurrentMapLayerNumber()-1));
                MapBuilder.refreshTileBuilder();
            }
        });
        options.add(incrementCurrentlyViewedLayer);
        decrementCurrentlyViewedLayer = new JButton("Go Down a Layer");
        decrementCurrentlyViewedLayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.err.println("asweeeeeeeeeeeeeeeeeeeeeeee");
                tileBuilder.setCurrentlyViewedLayer((int)Math.clamp(tileBuilder.getCurrentlyViewedLayer() - 1, 0, tileBuilder.getMap().getCurrentMapLayerNumber()-1));
                MapBuilder.refreshTileBuilder();
            }
        });
        options.add(decrementCurrentlyViewedLayer);
        addLayer = new JButton("Add One Layer");
        addLayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmptyLayerOntoMap(tileBuilder.getMap());

                tileBuilder.setCurrentlyViewedLayer((int)Math.clamp(tileBuilder.getCurrentlyViewedLayer() + 1, 0, tileBuilder.getMap().getCurrentMapLayerNumber()-1));

                //tileBuilder.setCurrentlyViewedLayer((int)Math.clamp(tileBuilder.getCurrentlyViewedLayer() - 1, 0, tileBuilder.getMap().getCurrentMapLayerNumber()-1));
                MapBuilder.refreshTileBuilder();
            }
        });
        options.add(addLayer);
        removeTopMostLayer = new JButton("Remove top-most layer");
        removeTopMostLayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeTopMostLayerFromMap(tileBuilder.getMap());
                if(tileBuilder.getCurrentlyViewedLayer() >= tileBuilder.getMap().getCurrentMapLayerNumber()) {
                    tileBuilder.setCurrentlyViewedLayer(tileBuilder.getMap().getCurrentMapLayerNumber()-1);
                }
                //tileBuilder.getMap().
                //tileBuilder.setCurrentlyViewedLayer((int)Math.clamp(tileBuilder.getCurrentlyViewedLayer() - 1, 0, tileBuilder.getMap().getCurrentMapLayerNumber()-1));
                MapBuilder.refreshTileBuilder();
            }
        });
        options.add(removeTopMostLayer);
        add(options);
    }
    public void addEmptyLayerOntoMap(Map inputMap) {
        inputMap.setCurrentMapLayerNumber(Math.clamp(inputMap.getCurrentMapLayerNumber()+1, 0, 99));
        MapTile[][] resultMap = new MapTile[inputMap.getAllMapTiles().length][inputMap.getCurrentMapLayerNumber()];
        for (int iLayer = 0; iLayer < inputMap.getCurrentMapLayerNumber(); iLayer++) {
            for (int i = 0; i < inputMap.getHeight(); i++) {
                for (int j = 0; j < inputMap.getWidth(); j++) {
                    if(iLayer < inputMap.getCurrentMapLayerNumber()-1) {
                        resultMap[(i * inputMap.getWidth() ) + j][iLayer] = inputMap.getMapTile(j, i, iLayer);
                    }
                    else {
                        resultMap[(i * inputMap.getWidth() ) + j][iLayer] = inputMap.getTileset().getTile(1).build(j, i, iLayer); 
                    }
                }
            }
        }
        inputMap.setAllMapTiles(resultMap);
    }
    public void removeTopMostLayerFromMap(Map inputMap) {
        inputMap.setCurrentMapLayerNumber(Math.clamp(inputMap.getCurrentMapLayerNumber()-1, 1, 99));
        if(inputMap.getCurrentGameplayLayer() > inputMap.getCurrentMapLayerNumber()-1) 
            inputMap.setCurrentGameplayLayer(inputMap.getCurrentMapLayerNumber()-1);
        MapTile[][] resultMap = new MapTile[inputMap.getAllMapTiles().length][inputMap.getCurrentMapLayerNumber()];
        for (int iLayer = 0; iLayer < inputMap.getCurrentMapLayerNumber(); iLayer++) {
            for (int i = 0; i < inputMap.getHeight(); i++) {
                for (int j = 0; j < inputMap.getWidth(); j++) {
                    resultMap[(i * inputMap.getWidth() ) + j][iLayer] = inputMap.getMapTile(j, i, iLayer);
                }
            }
        }
        inputMap.setAllMapTiles(resultMap);
    }
}
