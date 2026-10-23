package Maps;

import Engine.Config;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Sprite;
import Level.Map;
import Tilesets.CommonTileset;
import Utils.Colors;
import Utils.Point;

// Represents the map that is used as a background for the main menu and credits menu screen
public class TitleScreenMapNewVersion extends Map {

    private Sprite titleScreen;

    public TitleScreenMapNewVersion() {
        super("title_screen_map_newversion.txt", new CommonTileset());
        currentGameplayLayer = 0;
        titleScreen = new Sprite(ImageLoader.load("titlescreen.png"));
        titleScreen.setScale(Config.PIXEL_SCALE);

    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        titleScreen.draw(graphicsHandler);
    }
    
}
