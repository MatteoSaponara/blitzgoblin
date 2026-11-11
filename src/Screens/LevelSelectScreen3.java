package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMapNewVersion;
import SpriteFont.SpriteFont;

import java.awt.*;


public class LevelSelectScreen3 extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentMenuItemHovered = 0; // current menu item being "hovered" over
    protected int menuItemSelected = -1;
    protected SpriteFont Title;
	protected SpriteFont Back;
	protected SpriteFont Level7;
	protected SpriteFont Level8;
	protected SpriteFont Level9;
	protected SpriteFont GamblingRoom;
    protected SpriteFont ReturnMenu;
    protected SpriteFont returnInstructionsLabel;
    protected Map background;
    protected int keyPressTimer;
    protected int pointerLocationX, pointerLocationY;
    protected KeyLocker keyLocker = new KeyLocker();

    public LevelSelectScreen3(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
		Title = new SpriteFont("Level Select", 200, 305, Config.DEFAULT_FONT, 45, new Color(255, 255, 255)); //new Color(49, 207, 240));
        Title.setOutlineColor(Color.black);
        Title.setOutlineThickness(3);
        returnInstructionsLabel = new SpriteFont("Return to Menu", 200, 342, Config.DEFAULT_FONT, 30, new Color(255, 255, 255));
        returnInstructionsLabel.setOutlineColor(Color.black);
        returnInstructionsLabel.setOutlineThickness(3);
        Back = new SpriteFont("Back", 200, 380, Config.DEFAULT_FONT, 30, new Color(255, 255, 255)); //new Color(49, 207, 240));
        Back.setOutlineColor(Color.black);
        Back.setOutlineThickness(3);
        Level7 = new SpriteFont("Level 7", 200, 418, Config.DEFAULT_FONT, 30, Color.white);
		Level7.setOutlineColor(Color.black);
		Level7.setOutlineThickness(3);
		Level8 = new SpriteFont("Level 8", 200, 456, Config.DEFAULT_FONT, 30, Color.white);
		Level8.setOutlineColor(Color.black);
		Level8.setOutlineThickness(3);
		Level9 = new SpriteFont("Level 9", 200, 494, Config.DEFAULT_FONT, 30, Color.white);
		Level9.setOutlineColor(Color.black);
		Level9.setOutlineThickness(3);;
		GamblingRoom = new SpriteFont("Gambling Room", 200, 532, Config.DEFAULT_FONT, 30, Color.white);
		GamblingRoom.setOutlineColor(Color.black);
		GamblingRoom.setOutlineThickness(3);
        background = new TitleScreenMapNewVersion();
        background.setAdjustCamera(false);
        keyPressTimer = 0;
        menuItemSelected = -1;
        keyLocker.lockKey(Key.SPACE);
    }

    public void update() {
        // update background map (to play tile animations)
        background.update(null);

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
        if (currentMenuItemHovered > 5) {
            currentMenuItemHovered = 0;
        } else if (currentMenuItemHovered < 0) {
            currentMenuItemHovered = 5;
        }

        // sets location for blue square in front of text (pointerLocation) and also sets color of spritefont text based on which menu item is being hovered
        if (currentMenuItemHovered == 0) {
			pointerLocationX = 170;
			pointerLocationY = 349;
		} else if (currentMenuItemHovered == 1) {
			pointerLocationX = 170;
			pointerLocationY = 387;
		} else if (currentMenuItemHovered == 2) {
			pointerLocationX = 170;
			pointerLocationY = 425;
		} else if (currentMenuItemHovered == 3) {
			pointerLocationX = 170;
			pointerLocationY = 463;
		} else if (currentMenuItemHovered == 4) {
			pointerLocationX = 170;
			pointerLocationY = 501;
		} else if (currentMenuItemHovered == 5) {
			pointerLocationX = 170;
			pointerLocationY = 539;
		}

        // if space is pressed on menu item, change to appropriate screen based on which menu item was chosen
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            menuItemSelected = currentMenuItemHovered;
            if (menuItemSelected == 0) {
                screenCoordinator.setGameState(GameState.MENU);
            } else if (menuItemSelected == 1) {
                screenCoordinator.startNewGame(GameState.LEVEL_SELECT2);
			} else if (menuItemSelected == 2) {
                screenCoordinator.startNewGame(GameState.LEVEL4);
			} else if (menuItemSelected == 3) {
				screenCoordinator.startNewGame(GameState.LEVEL5);
			} else if (menuItemSelected == 4) {
				screenCoordinator.startNewGame(GameState.LEVEL6);
			} else if (menuItemSelected == 5) {
				screenCoordinator.startNewGame(GameState.GAMBLING_ROOM);
			}
		}
                
     }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        Title.draw(graphicsHandler);
        Back.draw(graphicsHandler);
        Level7.draw(graphicsHandler);
        Level8.draw(graphicsHandler);
		Level9.draw(graphicsHandler);
		GamblingRoom.draw(graphicsHandler);
        returnInstructionsLabel.draw(graphicsHandler);
        graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20, new Color(255, 255, 255), Color.black, 2); //new Color(49, 207, 240), Color.black, 2);
    }
}
