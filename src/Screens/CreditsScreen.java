package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMapNewVersion;
import SpriteFont.SpriteFont;
import java.awt.*;
import java.awt.image.BufferedImage;

// This class is for the credits screen
public class CreditsScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected Map background;
    protected KeyLocker keyLocker = new KeyLocker();
    protected SpriteFont creditsLabel;
    protected SpriteFont createdByLabel;
    protected SpriteFont programmers;
    protected SpriteFont scrumMasters;
    protected SpriteFont declan;
    protected SpriteFont aiden;
    protected SpriteFont sawyer;
    protected SpriteFont liam;
    protected SpriteFont matteo;
    protected SpriteFont isaias;
    protected SpriteFont luke;
    protected SpriteFont returnInstructionsLabel;
    protected BufferedImage OkayScale;

    public CreditsScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // setup graphics on screen (background map, spritefont text)
        background = new TitleScreenMapNewVersion();
        background.setAdjustCamera(false);
        creditsLabel = new SpriteFont("Credits", 15, 310, Config.DEFAULT_FONT, 30, Color.white);
        createdByLabel = new SpriteFont("Created by Aggro Range", 100, 375, Config.DEFAULT_FONT, 25, Color.white);
        programmers = new SpriteFont("Programmers", 85, 420, Config.DEFAULT_FONT, 20, Color.white);
        scrumMasters = new SpriteFont("Scrum Masters", 245, 420, Config.DEFAULT_FONT, 20, Color.white);
        declan = new SpriteFont("Declan Angle", 85, 450, Config.DEFAULT_FONT, 20, Color.white);
        aiden = new SpriteFont("Aiden Buckley", 85, 475, Config.DEFAULT_FONT, 20, Color.white);
        sawyer = new SpriteFont("Sawyer Enchura", 85, 500, Config.DEFAULT_FONT, 20, Color.white);
        liam = new SpriteFont("Liam Rua", 85, 525, Config.DEFAULT_FONT, 20, Color.white);
        matteo = new SpriteFont("Matteo Saponara", 85, 550, Config.DEFAULT_FONT, 20, Color.white);
        isaias = new SpriteFont("Isaias Torres", 245, 450, Config.DEFAULT_FONT, 20, Color.white);
        luke = new SpriteFont("Luke Whelan", 245, 475, Config.DEFAULT_FONT, 20, Color.white);
        returnInstructionsLabel = new SpriteFont("Press Space to return to the menu", 20, 858, Config.DEFAULT_FONT, 30, Color.white);
        keyLocker.lockKey(Key.SPACE);
        OkayScale = ImageLoader.load("BlitzGoblin_okay_scale.png");
    }

    public void update() {
        background.update(null);

        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }

        // if space is pressed, go back to main menu
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            screenCoordinator.setGameState(GameState.MENU);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        creditsLabel.draw(graphicsHandler);
        createdByLabel.draw(graphicsHandler);
        programmers.draw(graphicsHandler);
        scrumMasters.draw(graphicsHandler);
        declan.draw(graphicsHandler);
        aiden.draw(graphicsHandler);
        sawyer.draw(graphicsHandler);
        liam.draw(graphicsHandler);
        matteo.draw(graphicsHandler);
        isaias.draw(graphicsHandler);
        luke.draw(graphicsHandler);
        returnInstructionsLabel.draw(graphicsHandler);
        graphicsHandler.drawImage(OkayScale, 450, 350, 300, 200);
    }
}
