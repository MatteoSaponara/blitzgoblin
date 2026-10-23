package MapEditor;

import Engine.GraphicsHandler;
import Level.*;
import Utils.Colors;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import javax.swing.*;

public class TileBuilder extends JPanel {
    private Map map;
    private MapTile hoveredMapTile;
    private SelectedTileIndexHolder controlPanelHolder;
    private GraphicsHandler graphicsHandler = new GraphicsHandler();
    {
        graphicsHandler.setPixelGridEnabled(false);
    }
    private JLabel hoveredTileIndexLabel;
    private boolean showNPCs;
    private boolean showEnhancedMapTiles;
    private boolean showEnemies;

    private static int currentlyViewedLayer = 0;

    public static int getCurrentlyViewedLayer(){ return currentlyViewedLayer; }
    public static void setCurrentlyViewedLayer(int inputLayer){ currentlyViewedLayer = inputLayer;}
    public TileBuilder(SelectedTileIndexHolder controlPanelHolder, JLabel hoveredTileIndexLabel) {
        setBackground(Colors.MAGENTA);
        setLocation(0, 0);
        setPreferredSize(new Dimension(585, 562));
        this.controlPanelHolder = controlPanelHolder;
        this.hoveredTileIndexLabel = hoveredTileIndexLabel;
        addMouseListener(new MouseListener() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredMapTile = null;
                System.err.println("current "+currentlyViewedLayer);
                hoveredTileIndexLabel.setText("");
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.err.println("testestes");
                tileSelected(e.getPoint());
            }

            @Override
            public void mouseClicked(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                tileHovered(e.getPoint());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                tileHovered(e.getPoint());
                tileSelected(e.getPoint());
            }
        });
    }

    public void setMap(Map map) {
        this.map = map;
        setPreferredSize(new Dimension(map.getWidthPixels(), map.getHeightPixels()));
        repaint();
    }
    public Map getMap(){
        return map;
    }

    public void draw() {
        for (int layer = 0; layer < map.getCurrentMapLayerNumber(); layer++) {
            for (MapTile tile : map.getMapTiles(layer)) {
            /*if (layer+1 == (map.getCurrentGameplayLayer())){
                BufferedImage test = GameObject.copyImage(map.getTileset().getTile(tile.getTileIndex()).cloneAnimations().get("DEFAULT")[0].getImage());
                int[] fsfe = ((DataBufferInt)test.getRaster().getDataBuffer()).getData();
                for (int i = 0; i < test.getHeight(); i++) {
                    for (int j = 0; j < test.getWidth(); j++) {
                        if(((fsfe[(i * (test.getWidth())) + j]) >>> 6) > 0x00)
                            fsfe[(i * (test.getWidth())) + j] = (GameObject.adjustBrightnessOfHexColor(((fsfe[(i * (test.getWidth())) + j])-0xFF000000), 0.65) + 0xFF000000
                        );
                    }    
                }
                tile.setImage
            }*/
                if(tile != null)
                    tile.draw(graphicsHandler);
            }
        }
        

        if (showEnhancedMapTiles) {
            for (EnhancedMapTile enhancedMapTile : map.getEnhancedMapTiles()) {
                enhancedMapTile.draw(graphicsHandler);
            }
        }

        if (showNPCs) {
            for (NPC npc : map.getNPCs()) {
                npc.draw(graphicsHandler);
            }
        }

        if (showEnemies) {
            for (Enemy enemy : map.getEnemies()) {
                enemy.draw(graphicsHandler);
            }
        }

        if (hoveredMapTile != null) {
            graphicsHandler.drawRectangle(
                    Math.round(hoveredMapTile.getX()) + 2,
                    Math.round(hoveredMapTile.getY()) + 2,
                    hoveredMapTile.getWidth() - 5,
                    hoveredMapTile.getHeight() - 5,
                    Color.YELLOW,
                    5
            );
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        graphicsHandler.setGraphics((Graphics2D) g);
        draw();
    }

    public void tileSelected(Point selectedPoint) {
        
        int selectedTileIndex = getSelectedTileIndex(selectedPoint);
        if (selectedTileIndex != -1) {
            MapTile oldMapTile = map.getMapTiles()[selectedTileIndex];
            MapTile newMapTile =  map.getTileset().getTile(controlPanelHolder.getSelectedTileIndex()).build(oldMapTile.getX(), oldMapTile.getY());
            newMapTile.setNumLayer(currentlyViewedLayer);
            newMapTile.setMap(map);
            map.getAllMapTiles()[selectedTileIndex][currentlyViewedLayer] = newMapTile;
        }
        repaint();
    }

    public void tileHovered(Point hoveredPoint) {
        this.hoveredMapTile = getHoveredTile(hoveredPoint);
        if (this.hoveredMapTile != null) {
            int hoveredIndexX = Math.round(this.hoveredMapTile.getX()) / map.getTileset().getScaledSpriteWidth();
            int hoveredIndexY = Math.round(this.hoveredMapTile.getY()) / map.getTileset().getScaledSpriteHeight();
            hoveredTileIndexLabel.setText("X: " + hoveredIndexX + ", Y: " + hoveredIndexY);
            repaint();
        }
    }

    protected MapTile getHoveredTile(Point mousePoint) {
        for (MapTile mapTile : map.getMapTiles()) {
            if (isPointInTile(mousePoint, mapTile)) {
                return mapTile;
            }
        }
        return null;
    }

    protected int getSelectedTileIndex(Point mousePoint) {
        MapTile[] mapTiles = map.getMapTiles();
        for (int i = 0; i < mapTiles.length; i++) {
            if (isPointInTile(mousePoint, mapTiles[i])) {
                return i;
            }
        }
        return -1;
    }

    protected boolean isPointInTile(Point point, MapTile tile) {
        return (point.x >= tile.getX() && point.x <= tile.getX() + tile.getWidth() &&
                point.y >= tile.getY() && point.y <= tile.getY() + tile.getHeight());
    }

    public boolean getShowNPCs() {
        return showNPCs;
    }

    public void setShowNPCs(boolean showNPCs) {
        this.showNPCs = showNPCs;
        repaint();
    }

    public boolean getShowEnhancedMapTiles() {
        return showEnhancedMapTiles;
    }

    public void setShowEnhancedMapTiles(boolean showEnhancedMapTiles) {
        this.showEnhancedMapTiles = showEnhancedMapTiles;
        repaint();
    }

    public boolean getShowEnemies() {
        return showEnemies;
    }

    public void setShowEnemies(boolean showEnemies) {
        this.showEnemies = showEnemies;
        repaint();
    }
}