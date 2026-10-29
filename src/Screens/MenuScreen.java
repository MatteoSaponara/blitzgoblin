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
    protected SpriteFont Title;
    protected SpriteFont playGame;
    protected SpriteFont credits;
    protected SpriteFont howToPlay;
	protected SpriteFont levelSelect;
    protected SpriteFont settings;
    protected SpriteFont settingsTitle;
    protected SpriteFont debugCountersOption;
    protected SpriteFont showHitboxesOption;
    protected SpriteFont backOption;
    protected SpriteFont warning;
    protected Map background;
    protected int keyPressTimer;
    protected int warningShakeFrame;
    protected int pointerLocationX, pointerLocationY;
    protected int settingsMenuItemHovered;
    protected boolean settingsMenuOpen;
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
		settings = new SpriteFont("SETTINGS", 200, 374, Config.DEFAULT_FONT, 30, Color.white);
		settings.setOutlineColor(Color.black);
		settings.setOutlineThickness(3);
		settingsTitle = new SpriteFont("SETTINGS", 200, 170, Config.DEFAULT_FONT, 30, Color.white);
		settingsTitle.setOutlineColor(Color.black);
		settingsTitle.setOutlineThickness(3);
		debugCountersOption = new SpriteFont("", 200, 221, Config.DEFAULT_FONT, 30, Color.white);
		debugCountersOption.setOutlineColor(Color.black);
		debugCountersOption.setOutlineThickness(3);
		showHitboxesOption = new SpriteFont("", 200, 272, Config.DEFAULT_FONT, 30, Color.white);
		showHitboxesOption.setOutlineColor(Color.black);
		showHitboxesOption.setOutlineThickness(3);
		backOption = new SpriteFont("BACK", 200, 323, Config.DEFAULT_FONT, 30, Color.white);
		backOption.setOutlineColor(Color.black);
		backOption.setOutlineThickness(3);
		warning = new SpriteFont("warning: placeholder assets all over the place", 0, 8,
		        Config.DEFAULT_FONT, 28, new Color(255, 220, 80));
        warning.setOutlineColor(Color.black);
        warning.setOutlineThickness(4);

        background = new TitleScreenMapNewVersion();
        background.setAdjustCamera(false);
        keyPressTimer = 0;
        warningShakeFrame = 0;
        settingsMenuItemHovered = 0;
        settingsMenuOpen = false;
        keyLocker.lockKey(Key.SPACE);
    }

    public void update() {
        // update background map (to play tile animations)
        background.update(null);
        warningShakeFrame++;

        if (settingsMenuOpen) {
            debugCountersOption.setText("DEBUG COUNTERS: "
                    + (GamePanel.isDebugCountersEnabled() ? "ON" : "OFF"));
            showHitboxesOption.setText("SHOW HITBOXES: "
                    + (GamePanel.isHitboxesEnabled() ? "ON" : "OFF"));
        }

        // if down or up is pressed, change the highlighted menu item
        if (Keyboard.isKeyDown(Key.DOWN, Key.S) &&  keyPressTimer == 0) {
            keyPressTimer = 14;
            moveSelection(1);
        } else if (Keyboard.isKeyDown(Key.UP, Key.W) &&  keyPressTimer == 0) {
            keyPressTimer = 14;
            moveSelection(-1);
        } else {
            if (keyPressTimer > 0) {
                keyPressTimer--;
            }
        }

        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (Keyboard.isKeyUp(Key.ESC)) {
            keyLocker.unlockKey(Key.ESC);
        }

        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            keyLocker.lockKey(Key.SPACE);
            selectMenuItem();
        }

        if (settingsMenuOpen && Keyboard.isKeyDown(Key.ESC)
                && !keyLocker.isKeyLocked(Key.ESC)) {
            keyLocker.lockKey(Key.ESC);
            settingsMenuOpen = false;
        }

        updatePointerLocation();
    }

    private void moveSelection(int direction) {
        if (settingsMenuOpen) {
            settingsMenuItemHovered = (settingsMenuItemHovered + direction + 3) % 3;
        } else {
            currentMenuItemHovered = (currentMenuItemHovered + direction + 5) % 5;
        }
    }

    private void selectMenuItem() {
        if (settingsMenuOpen) {
            if (settingsMenuItemHovered == 0) {
                GamePanel.setDebugCountersEnabled(!GamePanel.isDebugCountersEnabled());
                debugCountersOption.setText("DEBUG COUNTERS: "
                        + (GamePanel.isDebugCountersEnabled() ? "ON" : "OFF"));
            } else if (settingsMenuItemHovered == 1) {
                GamePanel.setHitboxesEnabled(!GamePanel.isHitboxesEnabled());
                showHitboxesOption.setText("SHOW HITBOXES: "
                        + (GamePanel.isHitboxesEnabled() ? "ON" : "OFF"));
            } else {
                settingsMenuOpen = false;
            }
            return;
        }

        if (currentMenuItemHovered == 0) {
            screenCoordinator.setGameState(GameState.LEVEL);
        } else if (currentMenuItemHovered == 1) {
            screenCoordinator.setGameState(GameState.HOW_TO_PLAY);
        } else if (currentMenuItemHovered == 2) {
            screenCoordinator.setGameState(GameState.LEVEL_SELECT);
        } else if (currentMenuItemHovered == 3) {
            screenCoordinator.setGameState(GameState.CREDITS);
        } else {
            settingsMenuOpen = true;
            settingsMenuItemHovered = 0;
        }
    }

    private void updatePointerLocation() {
        pointerLocationX = 170;
        if (settingsMenuOpen) {
            pointerLocationY = 228 + settingsMenuItemHovered * 51;
        } else {
            pointerLocationY = 177 + currentMenuItemHovered * 51;
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        int warningWidth = graphicsHandler.getGraphics().getFontMetrics(warning.getFont()).stringWidth(warning.getText());
        int shakeX = (int) (Math.sin(warningShakeFrame * 0.9) * 2);
        int shakeY = (int) (Math.cos(warningShakeFrame * 1.3) * 2);
        warning.setLocation(Config.GAME_WINDOW_WIDTH - warningWidth - 10 + shakeX, 8 + shakeY);
        warning.draw(graphicsHandler);
        if (settingsMenuOpen) {
            settingsTitle.draw(graphicsHandler);
            debugCountersOption.draw(graphicsHandler);
            showHitboxesOption.draw(graphicsHandler);
            backOption.draw(graphicsHandler);
        } else {
            Title.draw(graphicsHandler);
            playGame.draw(graphicsHandler);
            howToPlay.draw(graphicsHandler);
            levelSelect.draw(graphicsHandler);
            credits.draw(graphicsHandler);
            settings.draw(graphicsHandler);
        }
        graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20, new Color(255, 255, 255), Color.black, 2); //new Color(49, 207, 240), Color.black, 2);
    }
}
