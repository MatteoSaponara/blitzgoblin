package Game;

import Engine.DefaultScreen;
import Engine.GraphicsHandler;
import Engine.Screen;
import Maps.TestMap;
import Maps.level1;
import Screens.CreditsScreen;
import Screens.HowToPlayScreen;
import Screens.LevelSelectScreen;
import Screens.MenuScreen;
import Screens.PlayLevelScreen;
//import Maps.Level1Map;

/*
 * Based on the current game state, this class determines which Screen should be shown
 * There can only be one "currentScreen", although screens can have "nested" screens
 */
public class ScreenCoordinator extends Screen {
	// currently shown Screen
	protected Screen currentScreen = new DefaultScreen();

	// keep track of gameState so ScreenCoordinator knows which Screen to show
	protected GameState gameState;
	protected GameState previousGameState;
	protected int currentLevel = 0;

	public GameState getGameState() {
		return gameState;
	}
	
	public void advanceLevel() {
		currentLevel++;
		if (currentLevel == 1) {
			setGameState(GameState.LEVEL1);
		} else if (currentLevel == 2) {
			currentLevel = 0;
			setGameState(GameState.MENU);
		}
	}

	// Other Screens can set the gameState of this class to force it to change the currentScreen
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	public Screen getCurrentScreen() {return currentScreen;}
	@Override
	public void initialize() {
		// start game off with Menu Screen
		gameState = GameState.MENU;
	}

	@Override
	public void update() {
		do {
			// if previousGameState does not equal gameState, it means there was a change in gameState
			// this triggers ScreenCoordinator to bring up a new Screen based on what the gameState is
			if (previousGameState != gameState) {
				switch(gameState) {
					case MENU:
						currentScreen = new MenuScreen(this);
						break;
					case LEVEL:
						currentScreen = new PlayLevelScreen(this, new TestMap());
						break;
						// Level 1 (when you create it)
					case LEVEL1:
						currentScreen =new PlayLevelScreen(this, new level1());
						break;
					
					case HOW_TO_PLAY:
					currentScreen = new HowToPlayScreen(this);
						break;
					case LEVEL_SELECT:
					currentScreen = new LevelSelectScreen(this);
						break;
					case CREDITS:
						currentScreen = new CreditsScreen(this);
						break;
				}
				currentScreen.initialize();
			}
			previousGameState = gameState;

			// call the update method for the currentScreen
			currentScreen.update();
		} while (previousGameState != gameState);
	}
	@Override
	public void draw(GraphicsHandler graphicsHandler) {
		// call the draw method for the currentScreen
		currentScreen.draw(graphicsHandler);
	}
}
