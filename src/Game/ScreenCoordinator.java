package Game;

import Engine.DefaultScreen;
import Engine.GraphicsHandler;
import Engine.Screen;
import Maps.GamblingRoom;
import Maps.level1;
import Maps.level3;
import Screens.CreditsScreen;
import Screens.MenuScreen;
import Screens.PlayLevelScreen;
import Screens.HowToPlayScreen;
import Screens.LevelSelectScreen;
import Maps.TestMap;
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
	protected int coinsCollected = 0;

	public GameState getGameState() {
		return gameState;
	}
	
	public int getCoinsCollected() {
		return coinsCollected;
	}

	public void startNewGame(GameState gameState) {
		switch (gameState) {
			case LEVEL:
				currentLevel = -1;
				break;
			case LEVEL1:
				currentLevel = 1;
				break;
			case LEVEL2:
				currentLevel = 3;
				break;
			case LEVEL3:
				currentLevel = 5;
				break;
			default:
				currentLevel = 0;
				break;
		}
		coinsCollected = 0;
		setGameState(gameState);
	}

	public void advanceLevel(int coinsCollected) {
		
		this.coinsCollected = coinsCollected;
		currentLevel++;

		switch (currentLevel) {
			case 0:
				setGameState(GameState.GAMBLING_ROOM);
				break;
			case 1:
				setGameState(GameState.LEVEL1);
				break;
			case 2:
			case 4:
			case 6:
				setGameState(GameState.GAMBLING_ROOM);
				break;
			case 3:
				setGameState(GameState.LEVEL2);
				break;
			case 5:
				setGameState(GameState.LEVEL3);
				break;
			default:
				currentLevel = 0;
				setGameState(GameState.MENU);
				break;
		}
	}

	// Other Screens can set the gameState of this class to force it to change the currentScreen
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

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
						
					case LEVEL2:
						currentScreen =new PlayLevelScreen(this, new level1());
						break;
						
					case LEVEL3:
						currentScreen =new PlayLevelScreen(this, new level3());
						break;
					case GAMBLING_ROOM:
						currentScreen = new PlayLevelScreen(this, new GamblingRoom());
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
