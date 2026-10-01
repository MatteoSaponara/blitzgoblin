package Engine;

import GameObject.ImageEffect;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


/*
 * Sprites and tiles are drawn to an internal pixel grid (1 art pixel = 1 buffer pixel), which is flushed to the
 * window with a sharp-bilinear upscale before any other draw call and at the end of the frame.
 */
public class GraphicsHandler {
    private Graphics2D g;

    private BufferedImage pixelBuffer;
    private Graphics2D pixelGraphics;
    private BufferedImage upscaledBuffer;
    private boolean pixelLayerDirty = false;
    private boolean pixelGridEnabled = true;

    // when disabled (map editor), sprites are drawn straight to the window at world size
    public boolean isPixelGridEnabled() {
        return pixelGridEnabled;
    }

    public void setPixelGridEnabled(boolean pixelGridEnabled) {
        this.pixelGridEnabled = pixelGridEnabled;
    }

    // world units -> pixel grid
    public static int toPixelGrid(float worldValue) {
        return Math.round(worldValue / Config.PIXEL_SCALE);
    }

    public Graphics2D getGraphics() {
        flushPixelLayer();
        return g;
    }

    public void setGraphics(Graphics2D g) {
        this.g = g;
        this.pixelLayerDirty = false;
    }

    private void ensurePixelBuffer() {
        int width = (int) Math.ceil(ScreenManager.getScreenWidth() / Config.PIXEL_SCALE) + 1;
        int height = (int) Math.ceil(ScreenManager.getScreenHeight() / Config.PIXEL_SCALE) + 1;
        if (pixelBuffer == null || pixelBuffer.getWidth() != width || pixelBuffer.getHeight() != height) {
            if (pixelGraphics != null) {
                pixelGraphics.dispose();
            }
            pixelBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
            pixelGraphics = pixelBuffer.createGraphics();
            pixelGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            upscaledBuffer = new BufferedImage(width * 2, height * 2, BufferedImage.TYPE_INT_ARGB_PRE);
        }
    }

    // draws to the pixel grid; destination is (x1, y1) to (x2, y2) in grid pixels
    public void drawPixelImage(BufferedImage image, int x1, int y1, int x2, int y2, ImageEffect imageEffect) {
        ensurePixelBuffer();
        int width = x2 - x1;
        int height = y2 - y1;
        if (width <= 0 || height <= 0) {
            return;
        }
        pixelLayerDirty = true;
        switch (imageEffect) {
            case FLIP_HORIZONTAL:
                pixelGraphics.drawImage(image, x2, y1, -width, height, null);
                break;
            case FLIP_VERTICAL:
                pixelGraphics.drawImage(image, x1, y2, width, -height, null);
                break;
            case FLIP_H_AND_V:
                pixelGraphics.drawImage(image, x2, y2, -width, -height, null);
                break;
            default:
                pixelGraphics.drawImage(image, x1, y1, width, height, null);
                break;
        }
    }

    // draws the pixel grid to the window and clears it
    public void flushPixelLayer() {
        if (!pixelLayerDirty || g == null) {
            return;
        }
        pixelLayerDirty = false;

        // 2x nearest neighbour
        Graphics2D up = upscaledBuffer.createGraphics();
        up.setComposite(AlphaComposite.Src);
        up.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        up.drawImage(pixelBuffer, 0, 0, upscaledBuffer.getWidth(), upscaledBuffer.getHeight(), null);
        up.dispose();

        // smooth resize to final size
        Object oldInterpolation = g.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        float finalScale = Config.PIXEL_SCALE / 2f;
        g.drawImage(upscaledBuffer, AffineTransform.getScaleInstance(finalScale, finalScale), null);
        if (oldInterpolation != null) {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldInterpolation);
        }

        pixelGraphics.setComposite(AlphaComposite.Clear);
        pixelGraphics.fillRect(0, 0, pixelBuffer.getWidth(), pixelBuffer.getHeight());
        pixelGraphics.setComposite(AlphaComposite.SrcOver);
    }

    public void drawImage(BufferedImage image, int x, int y) {
        flushPixelLayer();
        g.drawImage(image, x, y, null);
    }

    public void drawImage(BufferedImage image, int x, int y, int width, int height) {
        flushPixelLayer();
        g.drawImage(image, x, y, width, height, null);
    }

    public void drawImage(BufferedImage image, int x, int y, int width, int height, ImageEffect imageEffect) {
        flushPixelLayer();
        switch (imageEffect) {
            case NONE:
                drawImage(image, x, y, width, height);
                break;
            case FLIP_HORIZONTAL:
                g.drawImage(image, x + width, y, -width, height, null);
                break;
            case FLIP_VERTICAL:
                g.drawImage(image, x, y + height, width, -height, null);
                break;
            case FLIP_H_AND_V:
                g.drawImage(image, x + width, y + height, -width, -height, null);
                break;
        }
    }

    public void drawRectangle(int x, int y, int width, int height, Color color) {
        flushPixelLayer();
        Color oldColor = g.getColor();

        g.setColor(color);
        g.drawRect(x, y, width, height);

        g.setColor(oldColor);
    }

    public void drawRectangle(int x, int y, int width, int height, Color color, int borderThickness) {
        flushPixelLayer();
        Stroke oldStroke = g.getStroke();
        Color oldColor = g.getColor();

        g.setStroke(new BasicStroke(borderThickness));
        g.setColor(color);
        g.drawRect(x, y, width, height);

        g.setStroke(oldStroke);
        g.setColor(oldColor);
    }

    public void drawFilledRectangle(int x, int y, int width, int height, Color color) {
        flushPixelLayer();
        Color oldColor = g.getColor();

        g.setColor(color);
        g.fillRect(x, y, width, height);

        g.setColor(oldColor);
    }

    public void drawFilledRectangleWithBorder(int x, int y, int width, int height, Color fillColor, Color borderColor, int borderThickness) {
        drawFilledRectangle(x, y, width, height, fillColor);
        drawRectangle(x, y, width, height, borderColor, borderThickness);
    }

    public void drawString(String text, int x, int y, Font font, Color color) {
        flushPixelLayer();
        Font oldFont = g.getFont();
        Color oldColor = g.getColor();

        g.setFont(font);
        g.setColor(color);
        g.drawString(text, x, y);

        g.setFont(oldFont);
        g.setColor(oldColor);
    }

    // https://stackoverflow.com/a/35222059 and https://stackoverflow.com/a/31831120
    public void drawStringWithOutline(String text, int x, int y, Font font, Color textColor, Color outlineColor, float outlineThickness) {
        flushPixelLayer();
        // remember original settings
        Color originalColor = g.getColor();
        Stroke originalStroke = g.getStroke();
        RenderingHints originalHints = g.getRenderingHints();
        g.setStroke(new BasicStroke(outlineThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // create a glyph vector from your text
        GlyphVector glyphVector = font.createGlyphVector(g.getFontRenderContext(), text);

        // get the shape object
        Shape textShape = glyphVector.getOutline();
        AffineTransform at = new AffineTransform();
        at.setToTranslation(Math.round(x), Math.round(y));
        textShape = at.createTransformedShape(textShape);

        // activate anti aliasing for text rendering (if you want it to look nice)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g.setColor(outlineColor);
        g.draw(textShape); // draw outline

        g.setColor(textColor);
        g.fill(textShape); // fill the shape

        // reset to original settings after painting
        g.setColor(originalColor);
        g.setStroke(originalStroke);
        g.setRenderingHints(originalHints);
    }
}