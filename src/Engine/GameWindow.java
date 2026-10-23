package Engine;

import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

/*
 * The JFrame that holds the GamePanel
 * Just does some setup and exposes the gamePanel's screenManager to allow an external class to setup their own content and attach it to this engine.
 */
public class GameWindow {
	private JFrame gameWindow;
	private GamePanel gamePanel;
	private Rectangle windowedBounds;
	private boolean fullscreen;
	private boolean f11Pressed;

	public GameWindow() {
		gameWindow = new JFrame("Blitzgoblin");
		gamePanel = new GamePanel();
		gamePanel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_F11 && !f11Pressed) {
					f11Pressed = true;
					toggleFullscreen();
				}
			}

			@Override
			public void keyReleased(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_F11) {
					f11Pressed = false;
				}
			}
		});
		gamePanel.setFocusable(true);
		gamePanel.requestFocusInWindow();
		gameWindow.setContentPane(gamePanel);
		gameWindow.setResizable(true);
		//gameWindow.setSize(Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT);
		gameWindow.setSize(Config.GAME_WINDOW_WIDTH - 33, Config.GAME_WINDOW_HEIGHT);
		gameWindow.setLocationRelativeTo(null);
		gameWindow.setVisible(true);
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // it'd be nice if this actually worked more than 1/3rd of the time
		gamePanel.setupGame();
	}

	// triggers the game loop to start as defined in the GamePanel class
	public void startGame() {
		gamePanel.startGame();
	}

	public ScreenManager getScreenManager() {
		return gamePanel.getScreenManager();
	}

	private void toggleFullscreen() {
		if (fullscreen) {
			gameWindow.dispose();
			gameWindow.setUndecorated(false);
			gameWindow.setResizable(true);
			gameWindow.setBounds(windowedBounds);
			gameWindow.setVisible(true);
			fullscreen = false;
		} else {
			GraphicsConfiguration configuration = gameWindow.getGraphicsConfiguration();
			Rectangle fullscreenBounds = configuration.getBounds();
			windowedBounds = gameWindow.getBounds();

			gameWindow.dispose();
			gameWindow.setUndecorated(true);
			gameWindow.setResizable(false);
			gameWindow.setBounds(fullscreenBounds);
			gameWindow.setVisible(true);
			fullscreen = true;
		}

		gamePanel.requestFocusInWindow();
	}
}
