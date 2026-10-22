package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMapNewVersion;
import SpriteFont.SpriteFont;
import java.awt.*;
import java.io.File;
import java.io.IOException;

// This is the class for the main menu screen
public class MenuScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentMenuItemHovered = 0; // current menu item being "hovered" over
    protected int menuItemSelected = -1;
    protected SpriteFont Title;
    protected SpriteFont playGame;
    protected SpriteFont credits;
    protected SpriteFont howToPlay;
	protected SpriteFont levelSelect;
    protected SpriteFont warning;
    protected Map background;
    protected int keyPressTimer;
    protected int warningShakeFrame;
    protected int pointerLocationX, pointerLocationY;
    protected KeyLocker keyLocker = new KeyLocker();

    public MenuScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }
    private static Font customFont;


    public static void loadCustomFont() {
        try {
            File fontFile = new File(Config.RESOURCES_PATH+Config.DEFAULT_FONT_FILENAME);

            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            // Fallback font if loading fails
            customFont = new Font("SansSerif", Font.PLAIN, 12);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        }
    }

    @Override
    public void initialize() {
        loadCustomFont();
		Title = new SpriteFont("Blitz Goblin", 200, 100, Config.DEFAULT_FONT, 45, new Color(255, 255, 255)); //new Color(49, 207, 240));
        Title.setOutlineColor(Color.black);
        Title.setOutlineThickness(3);
        playGame = new SpriteFont("PLAY GAME", 200, 170, Config.DEFAULT_FONT, 30, new Color(255, 255, 255)); //new Color(49, 207, 240));
        playGame.setOutlineColor(Color.black);
        playGame.setOutlineThickness(3);
        howToPlay = new SpriteFont("HOW TO PLAY", 200, 221, Config.DEFAULT_FONT, 30, Color.white);
		howToPlay.setOutlineColor(Color.black);
		howToPlay.setOutlineThickness(3);
		levelSelect = new SpriteFont("LEVEL SELECT", 200, 272, Config.DEFAULT_FONT, 30, Color.white);
		levelSelect.setOutlineColor(Color.black);
		levelSelect.setOutlineThickness(3);
        credits = new SpriteFont("CREDITS", 200, 323, Config.DEFAULT_FONT, 30, new Color(255, 255, 255));     // new Color(49, 207, 240));
        warning = new SpriteFont("warning: placeholder assets all over the place", 0, 8,
                Config.DEFAULT_FONT, 28, new Color(255, 220, 80));
        warning.setOutlineColor(Color.black);
        warning.setOutlineThickness(4);

        background = new TitleScreenMapNewVersion();
        background.setAdjustCamera(false);
        keyPressTimer = 0;
        warningShakeFrame = 0;
        menuItemSelected = -1;
        keyLocker.lockKey(Key.SPACE);
    }

    public void update() {
        // update background map (to play tile animations)
        background.update(null);
        warningShakeFrame++;

        // if down or up is pressed, change menu item "hovered" over (blue square in front of text will move along with currentMenuItemHovered changing)
        if (Keyboard.isKeyDown(Key.DOWN, Key.S) &&  keyPressTimer == 0) {
            keyPressTimer = 14;
            currentMenuItemHovered++;
        } else if (Keyboard.isKeyDown(Key.UP, Key.W) &&  keyPressTimer == 0) {
            keyPressTimer = 14;
            currentMenuItemHovered--;
        } else {
            if (keyPressTimer > 0) {
                keyPressTimer--;
            }
        }

        // if down is pressed on last menu item or up is pressed on first menu item, "loop" the selection back around to the beginning/end
        if (currentMenuItemHovered > 3) {
            currentMenuItemHovered = 0;
        } else if (currentMenuItemHovered < 0) {
            currentMenuItemHovered = 3;
        }

        // sets location for blue square in front of text (pointerLocation) and also sets color of spritefont text based on which menu item is being hovered
        if (currentMenuItemHovered == 0) {
            playGame.setColor(new Color(255, 255, 255)); //new Color(255, 215, 0));
            howToPlay.setColor(new Color(255, 255, 255));
            levelSelect.setColor(new Color(255, 255, 255));
            credits.setColor(new Color(255, 255, 255));  //new Color(49, 207, 240));
            pointerLocationX = 170;
            pointerLocationY = 177;
        } else if (currentMenuItemHovered == 1) {
            playGame.setColor(new Color(255, 255, 255)); //new Color(49, 207, 240));
            howToPlay.setColor(new Color(255, 255, 255));
            levelSelect.setColor(new Color(255, 255, 255));
            credits.setColor(new Color(255, 255, 255)); //new Color(255, 215, 0));
            pointerLocationX = 170;
            pointerLocationY = 228;
        } else if (currentMenuItemHovered == 2) {
			playGame.setColor(new Color(255, 255, 255));
			howToPlay.setColor(new Color(255, 255, 255));
			levelSelect.setColor(new Color(255, 255, 255));
			credits.setColor(new Color(255, 255, 255));
			pointerLocationX = 170;
			pointerLocationY = 279;
		} else if (currentMenuItemHovered == 3) {
			playGame.setColor(new Color(255, 255, 255));
			howToPlay.setColor(new Color(255, 255, 255));
			levelSelect.setColor(new Color(255, 255, 255));
			credits.setColor(new Color(255, 255, 255));
			pointerLocationX = 170;
			pointerLocationY = 330;
		}
        

        // if space is pressed on menu item, change to appropriate screen based on which menu item was chosen
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            menuItemSelected = currentMenuItemHovered;
            if (menuItemSelected == 0) {
                screenCoordinator.setGameState(GameState.LEVEL);
            } else if (menuItemSelected == 1) {
                screenCoordinator.setGameState(GameState.HOW_TO_PLAY);
            
            } else if (menuItemSelected == 2) {
                screenCoordinator.setGameState(GameState.LEVEL_SELECT);
            
            } else if (menuItemSelected == 3) {
                screenCoordinator.setGameState(GameState.CREDITS);
            }
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        int warningWidth = graphicsHandler.getGraphics().getFontMetrics(warning.getFont()).stringWidth(warning.getText());
        int shakeX = (int) (Math.sin(warningShakeFrame * 0.9) * 2);
        int shakeY = (int) (Math.cos(warningShakeFrame * 1.3) * 2);
        warning.setLocation(Config.GAME_WINDOW_WIDTH - warningWidth - 10 + shakeX, 8 + shakeY);
        warning.draw(graphicsHandler);
        Title.draw(graphicsHandler);
        playGame.draw(graphicsHandler);
        howToPlay.draw(graphicsHandler);
		levelSelect.draw(graphicsHandler);
        credits.draw(graphicsHandler);
        graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20, new Color(255, 255, 255), Color.black, 2); //new Color(49, 207, 240), Color.black, 2);
    }
}
