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
	private volatile int currentFPS;
	private volatile long lastFrameIntervalMillis;
	private volatile long maxFrameIntervalMillis;
	private volatile long lastDrawDurationMillis;
	private volatile long lastUpdateDurationMillis;
	private volatile long maxUpdateDurationMillis;
	private long lastPaintStartNanos;
	private long paintStatsWindowStartNanos;
	private int paintedFramesInWindow;
	private long updateStatsWindowStartNanos;
	private long maxUpdateDurationNanos;
	private long lastPerformanceLabelUpdateNanos;
	private long lastMemoryLabelUpdateNanos;
	private String currentMemoryLabelText = "Memory: 0.0 MB";
	private String lastSizedCounterText;
	private static final int COUNTER_X = 4;
	private static final int COUNTER_Y = 4;
	private static final int COUNTER_RIGHT_MARGIN = 8;
	private static final int COUNTER_MAX_HEIGHT = 28;
	private static volatile boolean debugCountersEnabled = false;
	private static volatile boolean hitboxesEnabled = false;
	private boolean doPaint;
	private static BufferedImage jumpscareImage;
	private static int jumpscareTimer = 0;
	private static final int JUMPSCARE_DURATION = 60;

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

		pauseLabel = new SpriteFont("PAUSE", 365, 280, Engine.Config.DEFAULT_FONT, 24, Color.white);
		pauseLabel.setOutlineColor(Color.black);
		pauseLabel.setOutlineThickness(2.0f);

		fpsDisplayLabel = new SpriteFont("FPS", COUNTER_X, COUNTER_Y, Engine.Config.DEFAULT_FONT, 12, Color.white);
		fpsDisplayLabel.setOutlineColor(Color.black);
		fpsDisplayLabel.setOutlineThickness(0);

		currentFPS = Config.TARGET_FPS;

		// this game loop code will run in a separate thread from the rest of the program
		// will continually update the game's logic and repaint the game's graphics
		GameLoop gameLoop = new GameLoop(this);
		gameLoopProcess = new Thread(gameLoop.getGameLoopProcess());

		this.addMouseListener(Mouse.getMouseListener());
		this.addMouseMotionListener(Mouse.getMouseMotionListener());
	}
	
	public static void triggerJumpscare() {
		if (jumpscareTimer == 0) {
        jumpscareImage = ImageLoader.load("fakecoinJumpScare.png");
        jumpscareTimer = JUMPSCARE_DURATION;
		}
	}
	
	
	

	// this is called later after instantiation, and will initialize screenManager
	// this had to be done outside of the constructor because it needed to know the JPanel's width and height, which aren't available in the constructor
	public void setupGame() {
		setBackground(Colors.CORNFLOWER_BLUE);
		screenManager.initialize(new Rectangle(0, 0, Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT));
	}

	// this starts the timer (the game loop is started here)
	public void startGame() {
		gameLoopProcess.start();
	}

	public ScreenManager getScreenManager() {
		return screenManager;
	}

	public static boolean isDebugCountersEnabled() {
		return debugCountersEnabled;
	}

	public static void setDebugCountersEnabled(boolean enabled) {
		debugCountersEnabled = enabled;
	}

	public static boolean isHitboxesEnabled() {
		return hitboxesEnabled;
	}

	public static void setHitboxesEnabled(boolean enabled) {
		hitboxesEnabled = enabled;
	}

	public void setDoPaint(boolean doPaint) {
		this.doPaint = doPaint;
	}

	public void update() {
		long updateStartNanos = System.nanoTime();
		updatePauseState();
		updateShowFPSState();

		synchronized (stateLock) {
			if (!isGamePaused) {
				screenManager.update();
			}
		}
		
		if (jumpscareTimer > 0) {
			jumpscareTimer--;
		}
		recordUpdateDuration(System.nanoTime() - updateStartNanos);
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

		if (showFPS || debugCountersEnabled) {
			long now = System.nanoTime();
			if (debugCountersEnabled && now - lastMemoryLabelUpdateNanos >= 1_000_000_000L) {
				Runtime runtime = Runtime.getRuntime();
				double usedMemoryMb = (runtime.totalMemory() - runtime.freeMemory())
						/ (1024.0 * 1024.0);
				currentMemoryLabelText = String.format(java.util.Locale.ROOT,
						"Memory: %.1f MB", usedMemoryMb);
				lastMemoryLabelUpdateNanos = now;
			}
			if (now - lastPerformanceLabelUpdateNanos >= 250_000_000L) {
				String counterText = "FPS: " + currentFPS;
				if (debugCountersEnabled) {
					counterText += " | " + currentMemoryLabelText;
				}
				fpsDisplayLabel.setText(counterText);
				lastPerformanceLabelUpdateNanos = now;
			}
		}
	}

	public void draw() {
		screenManager.draw(graphicsHandler);

		// if game is paused, draw pause gfx over Screen gfx
		if (isGamePaused) {
			pauseLabel.draw(graphicsHandler);
			graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), new Color(0, 0, 0, 100));
		}

		if (debugCountersEnabled) {
			sizeCounterToFit();
			fpsDisplayLabel.draw(graphicsHandler);
		} else if (showFPS) {
			sizeCounterToFit();
			fpsDisplayLabel.draw(graphicsHandler);
		}

		graphicsHandler.flushPixelLayer();
	}

	private void sizeCounterToFit() {
		String text = fpsDisplayLabel.getText();
		if (text.equals(lastSizedCounterText)) {
			return;
		}

		Graphics graphics = graphicsHandler.getGraphics();
		int availableWidth = Config.GAME_WINDOW_WIDTH - COUNTER_X - COUNTER_RIGHT_MARGIN;
		Font baseFont = new Font(Config.DEFAULT_FONT, Font.PLAIN, 1);
		for (int size = COUNTER_MAX_HEIGHT; size >= 1; size--) {
			Font candidate = baseFont.deriveFont((float) size);
			FontMetrics metrics = graphics.getFontMetrics(candidate);
			if (metrics.stringWidth(text) <= availableWidth
					&& metrics.getHeight() <= COUNTER_MAX_HEIGHT) {
				fpsDisplayLabel.setFont(candidate);
				lastSizedCounterText = text;
				return;
			}
		}

		throw new IllegalStateException("Counter text does not fit in the available HUD space");
	}

	@Override
	protected void paintComponent(Graphics g) {
		long paintStartNanos = System.nanoTime();
		super.paintComponent(g);	
	
		if (doPaint) {	
			synchronized (stateLock) {
				int gameWidth = Config.GAME_WINDOW_WIDTH;
				int gameHeight = Config.GAME_WINDOW_HEIGHT;
				int panelWidth = getWidth();
				int panelHeight = getHeight();
				if (panelWidth <= 0 || panelHeight <= 0) {
					return;
				}

				if (gameImage == null ||
						gameImage.getWidth() != panelWidth ||
						gameImage.getHeight() != panelHeight) {
					gameImage = new BufferedImage(
							panelWidth,
							panelHeight,
							BufferedImage.TYPE_INT_ARGB
					);
				}

				Graphics2D gameGraphics = gameImage.createGraphics();

				gameGraphics.setColor(Color.BLACK);
				gameGraphics.fillRect(0, 0, panelWidth, panelHeight);
				
				//System.out.println("ScreenManager: " + ScreenManager.getScreenWidth() + "x" + ScreenManager.getScreenHeight());
				//System.out.println("Panel: " + panelWidth + "x" + panelHeight + " | Game image: " + gameWidth + "x" + gameHeight);

				double scaleX = (double) panelWidth / gameWidth;
				double scaleY = (double) panelHeight / gameHeight;
				double scale = Math.min(scaleX, scaleY);
				int drawWidth = (int) (gameWidth * scale);
				int drawHeight = (int) (gameHeight * scale);

				int drawX = (panelWidth - drawWidth) / 2;
				int drawY = (panelHeight - drawHeight) / 2;
				
				Mouse.setGameTransform(scale, drawX, drawY);

				gameGraphics.translate(drawX, drawY);
				gameGraphics.scale(scale, scale);
				gameGraphics.clipRect(0, 0, gameWidth, gameHeight);
				graphicsHandler.setGraphics(gameGraphics);
				draw();
				gameGraphics.dispose();

				Graphics2D screenGraphics = (Graphics2D) g.create();

				screenGraphics.setColor(Color.BLACK);
				screenGraphics.fillRect(0, 0, panelWidth, panelHeight);

				screenGraphics.setRenderingHint(
						RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
				);	

				screenGraphics.drawImage(gameImage, 0, 0, null);
				
				if (jumpscareTimer > 0 && jumpscareImage != null) {
					screenGraphics.drawImage(jumpscareImage, drawX, drawY, drawWidth, drawHeight, null);
					}

				screenGraphics.dispose();
			}

			Toolkit.getDefaultToolkit().sync();
			recordPaintFrame(paintStartNanos, System.nanoTime());
		}
	}

	private void recordPaintFrame(long paintStartNanos, long paintEndNanos) {
		long frameIntervalNanos = lastPaintStartNanos == 0
				? 0 : paintStartNanos - lastPaintStartNanos;
		lastPaintStartNanos = paintStartNanos;
		lastDrawDurationMillis = (paintEndNanos - paintStartNanos) / 1_000_000;
		lastFrameIntervalMillis = frameIntervalNanos / 1_000_000;

		if (paintStatsWindowStartNanos == 0) {
			paintStatsWindowStartNanos = paintStartNanos;
			paintedFramesInWindow = 1;
			return;
		}

		paintedFramesInWindow++;
		long windowDurationNanos = paintStartNanos - paintStatsWindowStartNanos;
		if (windowDurationNanos >= 1_000_000_000L) {
			currentFPS = (int) Math.round(paintedFramesInWindow
					* 1_000_000_000.0 / windowDurationNanos);
			paintStatsWindowStartNanos = paintStartNanos;
			paintedFramesInWindow = 0;
			maxFrameIntervalMillis = frameIntervalNanos / 1_000_000;
		} else if (frameIntervalNanos > maxFrameIntervalMillis * 1_000_000L) {
			maxFrameIntervalMillis = frameIntervalNanos / 1_000_000;
		}
	}

	private void recordUpdateDuration(long updateDurationNanos) {
		lastUpdateDurationMillis = updateDurationNanos / 1_000_000;
		long now = System.nanoTime();
		if (updateStatsWindowStartNanos == 0
				|| now - updateStatsWindowStartNanos >= 1_000_000_000L) {
			updateStatsWindowStartNanos = now;
			maxUpdateDurationNanos = updateDurationNanos;
			maxUpdateDurationMillis = lastUpdateDurationMillis;
		} else if (updateDurationNanos > maxUpdateDurationNanos) {
			maxUpdateDurationNanos = updateDurationNanos;
			maxUpdateDurationMillis = lastUpdateDurationMillis;
		}
	}


}
