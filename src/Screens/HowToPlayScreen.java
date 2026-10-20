package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMapNewVersion;
import SpriteFont.SpriteFont;

import java.awt.*;

// This class is for the credits screen
public class HowToPlayScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected Map background;
    protected KeyLocker keyLocker = new KeyLocker();
    protected SpriteFont HTPLabel;
    protected SpriteFont ControlsLabel;
    protected SpriteFont WControlsLabel;
    protected SpriteFont AControlsLabel;
    protected SpriteFont SControlsLabel;
    protected SpriteFont DControlsLabel;
    protected SpriteFont SPControlsLabel;
    protected SpriteFont returnInstructionsLabel;

    public HowToPlayScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // setup graphics on screen (background map, spritefont text)
        background = new TitleScreenMapNewVersion();
        background.setAdjustCamera(false);
        HTPLabel = new SpriteFont("How to Play", 200, 100, "Copperplate Gothic Light", 40, Color.white);
        ControlsLabel = new SpriteFont("Controls:", 200, 180, "Copperplate Gothic Light", 30, Color.white);
        WControlsLabel = new SpriteFont("Jump: W", 200, 210, "Copperplate Gothic Light", 25, Color.white);
        AControlsLabel = new SpriteFont("MoveLeft: A", 200, 240, "Copperplate Gothic Light", 25, Color.white);
        SControlsLabel = new SpriteFont("MoveRight: S", 200, 270, "Copperplate Gothic Light", 25, Color.white);
        DControlsLabel = new SpriteFont("Crouch: D", 200, 300, "Copperplate Gothic Light", 25, Color.white);
        SPControlsLabel = new SpriteFont("Space: Interact", 200, 330, "Copperplate Gothic Light", 25, Color.white);
        
        
        returnInstructionsLabel = new SpriteFont("Press Space to return to the menu", 20, 532, "Copperplate Gothic Light", 30, Color.white);
        keyLocker.lockKey(Key.SPACE);
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
        HTPLabel.draw(graphicsHandler);
        ControlsLabel.draw(graphicsHandler);
        WControlsLabel.draw(graphicsHandler);
        AControlsLabel.draw(graphicsHandler);
        SControlsLabel.draw(graphicsHandler);
        DControlsLabel.draw(graphicsHandler);
        SPControlsLabel.draw(graphicsHandler);
        returnInstructionsLabel.draw(graphicsHandler);
    }
}
