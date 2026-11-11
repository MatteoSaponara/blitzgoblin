package Engine;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

        // loading custom cursor image
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image cursorImage = toolkit.getImage(Config.RESOURCES_PATH+"crosshair.png");

        Cursor customCursor = toolkit.createCustomCursor(
            cursorImage,
            new java.awt.Point(4, 4),  // sets where clicks registers at the center of the crosshair image 
            "Crosshair"
        );

        gameWindow.setCursor(customCursor);

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
		gamePanel.setPreferredSize(new Dimension(Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT));
		gameWindow.setContentPane(gamePanel);
		gameWindow.setResizable(true);
		gameWindow.pack();
		gameWindow.setLocationRelativeTo(null);
		gameWindow.setVisible(true);
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // it'd be nice if this actually worked more than 1/3rd of the time
		gameWindow.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				AudioManager.getInstance().close();
			}
		});
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
