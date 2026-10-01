package Engine;

import Utils.Colors;

import java.awt.*;

/*
 * This class holds some constants like window width/height and resource folder locations
 * Tweak these as needed prior to running the application
 */
public class Config {
    public static final int TARGET_FPS = 60;
    public static final String RESOURCES_PATH = "Resources/";
    public static final String MAP_FILES_PATH = "MapFiles/";
    public static final int GAME_WINDOW_WIDTH = 800;
    public static final int GAME_WINDOW_HEIGHT = 605;
    public static final Color TRANSPARENT_COLOR = Colors.MAGENTA;

    // world units per art pixel; sprites scaled by this value are drawn 1:1 on the internal pixel grid
    // (parsed at runtime so the value isn't inlined into other classes; edits always take effect on a normal rebuild)
    public static final float PIXEL_SCALE = Float.parseFloat("1.4");

    // tileset scale; use PIXEL_SCALE (or a multiple of it) to keep tiles consistent with sprites
    public static final float TILE_SCALE = PIXEL_SCALE;

    // width/height in art pixels of one tile in CommonTileset.png (tiles are separated by a 1px gap)
    // on-screen tile size = TILE_ART_SIZE * TILE_SCALE; multiples of 5 give whole-number sizes at 1.4
    public static final int TILE_ART_SIZE = Integer.parseInt("35");

    // POWER_SAVER does not hog CPU as much, but can potentially stutter/lag on lower end computers if they cannot handle reaching the target FPS
    // MAX_PERFORMANCE will have the game do whatever it takes to reach the target FPS, even if that means hogging the CPU
    public static final GameLoopType GAME_LOOP_TYPE = GameLoopType.POWER_SAVER;

    // prevents Config from being instantiated
    private Config() { }
}