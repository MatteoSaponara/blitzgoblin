package Engine;

import GameObject.Rectangle;
import SpriteFont.SpriteFont;
import Utils.Colors;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/*
 * This is where the game loop process and render back buffer is setup
 */
public class GamePanel extends JPanel {
	// loads Screens on to the JPanel
	// each screen has its own update and draw methods defined to handle a "section" of the game.
	private ScreenManager screenManager;

	// used to draw graphics to the panel
	private GraphicsHandler graphicsHandler;
	private BufferedImage gameImage;

	private boolean isGamePaused = false;
	private SpriteFont pauseLabel;
	private KeyLocker keyLocker = new KeyLocker();
	private final Key pauseKey = Key.P;
	private Thread gameLoopProcess;

	private Key showFPSKey = Key.G;
	private SpriteFont fpsDisplayLabel;
	private boolean showFPS = false;
	private int currentFPS;
	private boolean doPaint;

	// update and paint run on different threads; this keeps a frame from being drawn halfway through an update
	private final Object stateLock = new Object();

	// The JPanel and various important class instances are setup here
	public GamePanel() {
		super();
		this.setDoubleBuffered(true);

		// attaches Keyboard class's keyListener to this JPanel
		this.addKeyListener(Keyboard.getKeyListener());

		// attaches Mouse class's mouseListener to this JPanel
		this.addMouseListener(Mouse.getMouseListener());

		graphicsHandler = new GraphicsHandler();

		screenManager = new ScreenManager();

		pauseLabel = new SpriteFont("PAUSE", 365, 280, "Arial", 24, Color.white);
		pauseLabel.setOutlineColor(Color.black);
		pauseLabel.setOutlineThickness(2.0f);

		fpsDisplayLabel = new SpriteFont("FPS", 4, 3, "Arial", 12, Color.black);

		currentFPS = Config.TARGET_FPS;

		// this game loop code will run in a separate thread from the rest of the program
		// will continually update the game's logic and repaint the game's graphics
		GameLoop gameLoop = new GameLoop(this);
		gameLoopProcess = new Thread(gameLoop.getGameLoopProcess());

		this.addMouseListener(Mouse.getMouseListener());
		this.addMouseMotionListener(Mouse.getMouseMotionListener());
	}

	// this is called later after instantiation, and will initialize screenManager
	// this had to be done outside of the constructor because it needed to know the JPanel's width and height, which aren't available in the constructor
	public void setupGame() {
		setBackground(Colors.CORNFLOWER_BLUE);
		screenManager.initialize(new Rectangle(getX(), getY(), getWidth(), getHeight()));
	}

	// this starts the timer (the game loop is started here)
	public void startGame() {
		gameLoopProcess.start();
	}

	public ScreenManager getScreenManager() {
		return screenManager;
	}

	public void setCurrentFPS(int currentFPS) {
		this.currentFPS = currentFPS;
	}

	public void setDoPaint(boolean doPaint) {
		this.doPaint = doPaint;
	}

	public void update() {
		updatePauseState();
		updateShowFPSState();

		synchronized (stateLock) {
			if (!isGamePaused) {
				screenManager.update();
			}
		}
	}

	private void updatePauseState() {
		if (Keyboard.isKeyDown(pauseKey) && !keyLocker.isKeyLocked(pauseKey)) {
			isGamePaused = !isGamePaused;
			keyLocker.lockKey(pauseKey);
		}

		if (Keyboard.isKeyUp(pauseKey)) {
			keyLocker.unlockKey(pauseKey);
		}
	}

	private void updateShowFPSState() {
		if (Keyboard.isKeyDown(showFPSKey) && !keyLocker.isKeyLocked(showFPSKey)) {
			showFPS = !showFPS;
			keyLocker.lockKey(showFPSKey);
		}

		if (Keyboard.isKeyUp(showFPSKey)) {
			keyLocker.unlockKey(showFPSKey);
		}

		fpsDisplayLabel.setText("FPS: " + currentFPS);
	}

	public void draw() {
		screenManager.draw(graphicsHandler);

		// if game is paused, draw pause gfx over Screen gfx
		if (isGamePaused) {
			pauseLabel.draw(graphicsHandler);
			graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), new Color(0, 0, 0, 100));
		}

		if (showFPS) {
			fpsDisplayLabel.draw(graphicsHandler);
		}

		graphicsHandler.flushPixelLayer();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);	
	
		if (doPaint) {	
			synchronized (stateLock) {
				int gameWidth = Config.GAME_WINDOW_WIDTH;
				int gameHeight = Config.GAME_WINDOW_HEIGHT;

				if (gameImage == null ||
						gameImage.getWidth() != gameWidth ||
						gameImage.getHeight() != gameHeight) {
					gameImage = new BufferedImage(
							gameWidth,
							gameHeight,
							BufferedImage.TYPE_INT_ARGB
					);
				}

				Graphics2D gameGraphics = gameImage.createGraphics();

				gameGraphics.setColor(Color.BLACK);
				gameGraphics.fillRect(0, 0, gameWidth, gameHeight);

				graphicsHandler.setGraphics(gameGraphics);
				draw();

				gameGraphics.dispose();

				int panelWidth = getWidth();
				int panelHeight = getHeight();

				double scaleX = (double) panelWidth / gameWidth;
				double scaleY = (double) panelHeight / gameHeight;
				double scale = Math.min(scaleX, scaleY);

				int drawWidth = (int) (gameWidth * scale);
				int drawHeight = (int) (gameHeight * scale);

				int drawX = (panelWidth - drawWidth) / 2;
				int drawY = (panelHeight - drawHeight) / 2;

				Graphics2D screenGraphics = (Graphics2D) g.create();

				screenGraphics.setColor(Color.BLACK);
				screenGraphics.fillRect(0, 0, panelWidth, panelHeight);

				screenGraphics.setRenderingHint(
						RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
				);	

				screenGraphics.drawImage(
						gameImage,
						drawX,
						drawY,
						drawWidth,
						drawHeight,
						null
				);

				screenGraphics.dispose();
			}

			Toolkit.getDefaultToolkit().sync();
		}
	}


}
