package Screens;

import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Screen;
import Engine.GamePanel;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Level.Player;
import Level.PlayerListener;
import Players.Cat;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;


// This class is for when the platformer game is actually being played
public class PlayLevelScreen extends Screen implements PlayerListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected BufferedImage heartImage;
    protected BufferedImage testImage;
    protected PlayLevelScreenState playLevelScreenState;
    protected int screenTimer;
    protected LevelClearedScreen levelClearedScreen;
    protected LevelLoseScreen levelLoseScreen;
    protected boolean levelCompletedStateChangeStart;

   public PlayLevelScreen(ScreenCoordinator screenCoordinator, Map map) {
	   this.screenCoordinator = screenCoordinator;
	   this.map = map;
	   }

    public void initialize() {
        // define/setup map
        //this.map = new TestMap();

        // setup player
        this.heartImage = ImageLoader.load("HeartGB2.png");
        this.player = new Cat(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y, false);
        this.player.setMap(map);
        map.centerCameraOnPlayer(player);
        this.player.addListener(this);

        levelClearedScreen = new LevelClearedScreen();
        levelLoseScreen = new LevelLoseScreen(this);

        this.playLevelScreenState = PlayLevelScreenState.RUNNING;
    }

    public void update() {
        // based on screen state, perform specific actions
        switch (playLevelScreenState) {
            // if level is "running" update player and map to keep game logic for the platformer level going
            case RUNNING:
                player.update();
                map.update(player);
                if(player.hasShotgun())
                    if (player.getShotgun() != null) {
                        player.getShotgun().update();
                    }
                break;
            // if level has been completed, bring up the level cleared screen
            case LEVEL_COMPLETED:
                if (levelCompletedStateChangeStart) {
                    screenTimer = 130;
                    levelCompletedStateChangeStart = false;
                } else {
                    levelClearedScreen.update();
                    screenTimer--;
                    if (screenTimer <= 0) {
                        screenCoordinator.advanceLevel();
					}
				}
                    
                    break;
            // wait on level lose screen to make a decision (either resets level or sends player back to main menu)
            case LEVEL_LOSE:
                levelLoseScreen.update();
                break;
        }
    }
    
    public void draw(GraphicsHandler graphicsHandler) {
        // based on screen state, draw appropriate graphics
        switch (playLevelScreenState) {
            case RUNNING:
                map.draw(graphicsHandler);
                player.draw(graphicsHandler);
                if(player.getShotgun() != null) { 
                    player.getShotgun().draw(graphicsHandler);
                    if(player.playerCanShootShotgun())
                        graphicsHandler.drawImage(ImageLoader.load("gunui.png"), 27, 80, 55, 25);
                    else
                        graphicsHandler.drawImage(ImageLoader.load("gunuireload.png"), 27, 80, 55, 25);
                }
                map.draw(graphicsHandler, true);

                if (GamePanel.isHitboxesEnabled()) {
                    map.drawDebugHitboxes(graphicsHandler, player);
                }

                for (int i = 0; i < player.getHealth(); i++) {
                    graphicsHandler.drawImage(heartImage, 10 + (i * 40), 10, 60, 60);
                }

                graphicsHandler.drawString("Coins: " + player.getCoins(), 160, 55, new Font(Engine.Config.DEFAULT_FONT, Font.PLAIN, 40), Color.WHITE);

                break;
            case LEVEL_COMPLETED:
                levelClearedScreen.draw(graphicsHandler);
                break;
            case LEVEL_LOSE:
                levelLoseScreen.draw(graphicsHandler);
                break;
        }
    }

    @Override public Level.Map hasMap(){
        return this.map;
    }

    public PlayLevelScreenState getPlayLevelScreenState() {
        return playLevelScreenState;
    }

    @Override
    public void onLevelCompleted() {
        if (playLevelScreenState != PlayLevelScreenState.LEVEL_COMPLETED) {
            playLevelScreenState = PlayLevelScreenState.LEVEL_COMPLETED;
            levelCompletedStateChangeStart = true;
        }
    }

    @Override
    public void onDeath() {
        if (playLevelScreenState != PlayLevelScreenState.LEVEL_LOSE) {
            playLevelScreenState = PlayLevelScreenState.LEVEL_LOSE;
        }
    }

    public void resetLevel() {
		map.reset();
        initialize();
    }

    public void goBackToMenu() {
        screenCoordinator.setGameState(GameState.MENU);
    }

    // This enum represents the different states this screen can be in
    private enum PlayLevelScreenState {
        RUNNING, LEVEL_COMPLETED, LEVEL_LOSE
    }
}
