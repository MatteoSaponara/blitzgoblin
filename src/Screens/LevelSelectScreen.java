package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMapNewVersion;
import SpriteFont.SpriteFont;
import java.awt.*;


public class LevelSelectScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentMenuItemHovered = 0; // current menu item being "hovered" over
    protected int menuItemSelected = -1;
    protected SpriteFont Title;
	protected SpriteFont Level;
	protected SpriteFont Level1;
	protected SpriteFont Level2;
	protected SpriteFont Level3;
	protected SpriteFont GamblingRoom;
    protected SpriteFont ReturnMenu;
    protected SpriteFont returnInstructionsLabel;
    protected Map background;
    protected int keyPressTimer;
    protected int pointerLocationX, pointerLocationY;
    protected KeyLocker keyLocker = new KeyLocker();

    public LevelSelectScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        this.background = new TitleScreenMapNewVersion();
    }

    @Override
    public void initialize() {
		Title = new SpriteFont("Level Select", 200, 305, Config.DEFAULT_FONT, 45, new Color(255, 255, 255)); //new Color(49, 207, 240));
        Title.setOutlineColor(Color.black);
        Title.setOutlineThickness(3);
        returnInstructionsLabel = new SpriteFont("Return to Menu", 200, 342, Config.DEFAULT_FONT, 30, new Color(255, 255, 255));
        returnInstructionsLabel.setOutlineColor(Color.black);
        returnInstructionsLabel.setOutlineThickness(3);
        Level = new SpriteFont("Tutorial", 200, 380, Config.DEFAULT_FONT, 30, new Color(255, 255, 255)); //new Color(49, 207, 240));
        Level.setOutlineColor(Color.black);
        Level.setOutlineThickness(3);
        Level1 = new SpriteFont("Level 1", 200, 418, Config.DEFAULT_FONT, 30, Color.white);
		Level1.setOutlineColor(Color.black);
		Level1.setOutlineThickness(3);
		Level2 = new SpriteFont("Level 2", 200, 456, Config.DEFAULT_FONT, 30, Color.white);
		Level2.setOutlineColor(Color.black);
		Level2.setOutlineThickness(3);
		Level3 = new SpriteFont("Level 3", 200, 494, Config.DEFAULT_FONT, 30, Color.white);
		Level3.setOutlineColor(Color.black);
		Level3.setOutlineThickness(3);
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
                screenCoordinator.startNewGame(GameState.LEVEL);
			} else if (menuItemSelected == 2) {
                screenCoordinator.startNewGame(GameState.LEVEL1);
			} else if (menuItemSelected == 3) {
				screenCoordinator.startNewGame(GameState.LEVEL2);
			} else if (menuItemSelected == 4) {
				screenCoordinator.startNewGame(GameState.LEVEL3);
			} else if (menuItemSelected == 5) {
				screenCoordinator.startNewGame(GameState.GAMBLING_ROOM);
			}
		}
                
     }
    
    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        Title.draw(graphicsHandler);
        Level.draw(graphicsHandler);
        Level1.draw(graphicsHandler);
        Level2.draw(graphicsHandler);
		Level3.draw(graphicsHandler);
		GamblingRoom.draw(graphicsHandler);
        returnInstructionsLabel.draw(graphicsHandler);
        graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20, new Color(255, 255, 255), Color.black, 2); //new Color(49, 207, 240), Color.black, 2);
    }
     
    @Override public Level.Map hasMap(){
        return this.background;
    }
}
