package GameObject;

import Engine.Config;
import Engine.GraphicsHandler;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * reusable physics stuff for particles that don't need to collide with anything
 */
public class PhysicsParticle {
    private static final float MINIMUM_SIZE = Config.PIXEL_SCALE;

    public enum Shape {
        CIRCLE,
        RECTANGLE
    }

    public static final int INFINITE_LIFETIME = -1;

    private float x;
    private float y;
    private float velocityX;
    private float velocityY;
    private float gravityX;
    private float gravityY;
    private float width;
    private float height;
    private float rotationDegrees;
    private float spinDegreesPerFrame;
    private float drag;
    private int lifetimeFrames = INFINITE_LIFETIME;
    private int ageFrames;
    private boolean fadesOut;
    private boolean shrinksToNothing;
    private Color color;
    private Shape shape = Shape.CIRCLE;
    private BufferedImage image;

    public PhysicsParticle(float x, float y, float size, Color color) {
        this(x, y, size, size, 0, 0, color);
    }

    public PhysicsParticle(float x, float y, float width, float height,
                           float velocityX, float velocityY, Color color) {
        this.x = x;
        this.y = y;
        setSize(width, height);
        setColor(color);
        setVelocity(velocityX, velocityY);
    }

    public PhysicsParticle(float x, float y, BufferedImage image) {
        this(x, y, Config.PIXEL_SCALE, Color.WHITE);
        setImage(image);
    }

    public void update() {
        if (!isAlive()) {
            return;
        }

        velocityX = (velocityX + gravityX) * (1 - drag);
        velocityY = (velocityY + gravityY) * (1 - drag);
        x += velocityX;
        y += velocityY;
        rotationDegrees = (rotationDegrees + spinDegreesPerFrame) % 360;
        ageFrames++;
    }

    public void draw(GraphicsHandler graphicsHandler) {
        draw(graphicsHandler, 0, 0);
    }

    /** Draws the particle */
    public void draw(GraphicsHandler graphicsHandler, float cameraOffsetX, float cameraOffsetY) {
        if (!isAlive()) {
            return;
        }

        float opacity = 1;
        if (fadesOut && lifetimeFrames != INFINITE_LIFETIME) {
            opacity = Math.max(0, (lifetimeFrames - ageFrames) / (float) lifetimeFrames);
        }

        float shrinkScale = shrinksToNothing && lifetimeFrames != INFINITE_LIFETIME
                ? (lifetimeFrames - ageFrames) / (float) lifetimeFrames
                : 1;
        float drawWidth = shrinksToNothing
                ? snapShrinkingSizeToPixelGrid(width, shrinkScale)
                : snapSizeToPixelGrid(width);
        float drawHeight = shrinksToNothing
                ? snapShrinkingSizeToPixelGrid(height, shrinkScale)
                : snapSizeToPixelGrid(height);
        if (drawWidth == 0 || drawHeight == 0) {
            return;
        }

        float drawX = snapToPixelGrid(x - drawWidth / 2 - cameraOffsetX);
        float drawY = snapToPixelGrid(y - drawHeight / 2 - cameraOffsetY);
        if (image != null) {
            graphicsHandler.drawParticleImage(image, drawX, drawY, drawWidth, drawHeight, rotationDegrees, opacity);
            return;
        }

        graphicsHandler.drawParticle(
                drawX,
                drawY,
                drawWidth,
                drawHeight,
                rotationDegrees,
                color,
                shape == Shape.CIRCLE,
                opacity);
    }

    private static float snapToPixelGrid(float value) {
        return Math.round(value / Config.PIXEL_SCALE) * Config.PIXEL_SCALE;
    }

    private static float snapSizeToPixelGrid(float size) {
        return Math.max(Config.PIXEL_SCALE,
                Math.round(size / Config.PIXEL_SCALE) * Config.PIXEL_SCALE);
    }

    private static float snapShrinkingSizeToPixelGrid(float size, float shrinkScale) {
        return Math.max(0, Math.round(size * shrinkScale / Config.PIXEL_SCALE)) * Config.PIXEL_SCALE;
    }

    public boolean isAlive() {
        return lifetimeFrames == INFINITE_LIFETIME || ageFrames < lifetimeFrames;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocity(float velocityX, float velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public void setGravity(float gravityX, float gravityY) {
        this.gravityX = gravityX;
        this.gravityY = gravityY;
    }

    public float getGravityX() {
        return gravityX;
    }

    public float getGravityY() {
        return gravityY;
    }

    public void setSize(float size) {
        setSize(size, size);
    }

    public void setSize(float width, float height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Particle dimensions must be positive");
        }
        this.width = Math.max(width, MINIMUM_SIZE);
        this.height = Math.max(height, MINIMUM_SIZE);
    }

    public BufferedImage getImage() {
        return image;
    }

    /**
     * Sets the particle sprite and resets its dimensions to the image's native
     * pixel size scaled to the game
     */
    public void setImage(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Particle image cannot be null");
        }
        this.image = image;
        setSize(image.getWidth() * Config.PIXEL_SCALE, image.getHeight() * Config.PIXEL_SCALE);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setRotationDegrees(float rotationDegrees) {
        this.rotationDegrees = rotationDegrees;
    }

    public float getRotationDegrees() {
        return rotationDegrees;
    }

    public void setSpinDegreesPerFrame(float spinDegreesPerFrame) {
        this.spinDegreesPerFrame = spinDegreesPerFrame;
    }

    public float getSpinDegreesPerFrame() {
        return spinDegreesPerFrame;
    }

    public void setDrag(float drag) {
        if (drag < 0 || drag > 1) {
            throw new IllegalArgumentException("Drag must be between 0 and 1");
        }
        this.drag = drag;
    }

    public float getDrag() {
        return drag;
    }

    public void setLifetimeFrames(int lifetimeFrames) {
        if (lifetimeFrames != INFINITE_LIFETIME && lifetimeFrames <= 0) {
            throw new IllegalArgumentException("Lifetime must be positive or INFINITE_LIFETIME");
        }
        this.lifetimeFrames = lifetimeFrames;
    }

    public int getAgeFrames() {
        return ageFrames;
    }

    public void setFadesOut(boolean fadesOut) {
        this.fadesOut = fadesOut;
    }

    public boolean isFadesOut() {
        return fadesOut;
    }

    public void setShrinksToNothing(boolean shrinksToNothing) {
        this.shrinksToNothing = shrinksToNothing;
    }

    public boolean isShrinksToNothing() {
        return shrinksToNothing;
    }

    public void setColor(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("Particle color cannot be null");
        }
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setShape(Shape shape) {
        if (shape == null) {
            throw new IllegalArgumentException("Particle shape cannot be null");
        }
        this.shape = shape;
    }

    public Shape getShape() {
        return shape;
    }
}
