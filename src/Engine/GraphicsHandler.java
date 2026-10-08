package Engine;

import GameObject.ImageEffect;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.stream.IntStream;


/*
 * Sprites and tiles are drawn to an internal pixel grid (1 art pixel = 1 buffer pixel), which is flushed to the
 * window with a sharp-bilinear upscale before any other draw call and at the end of the frame.
 */
public class GraphicsHandler {
    private Graphics2D g;

    // device pixels per logical pixel (above 1 on scaled/HiDPI displays)
    private static double deviceScaleX = 1, deviceScaleY = 1;

    private BufferedImage pixelBuffer;
    private Graphics2D pixelGraphics;
    private int[] pixelData;
    private boolean pixelLayerDirty = false;
    private boolean pixelGridEnabled = true;

    // output at device resolution, built from the pixel grid
    private BufferedImage outBuffer;
    private int[] outData;
    private int[] hData; // grid rows resampled horizontally
    private int[] xI0, xI1, xW, yI0, yI1, yW;

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
        AffineTransform t = g.getTransform();
        deviceScaleX = Math.max(1, Math.hypot(t.getScaleX(), t.getShearY()));
        deviceScaleY = Math.max(1, Math.hypot(t.getScaleY(), t.getShearX()));
    }

    private void ensurePixelBuffer() {
        int outW = (int) Math.round(ScreenManager.getScreenWidth() * deviceScaleX);
        int outH = (int) Math.round(ScreenManager.getScreenHeight() * deviceScaleY);
        float scaleX = (float) (Config.PIXEL_SCALE * deviceScaleX);
        float scaleY = (float) (Config.PIXEL_SCALE * deviceScaleY);
        int width = (int) Math.ceil(outW / scaleX) + 1;
        int height = (int) Math.ceil(outH / scaleY) + 1;
        if (pixelBuffer == null || pixelBuffer.getWidth() != width || pixelBuffer.getHeight() != height
                || outBuffer.getWidth() != outW || outBuffer.getHeight() != outH) {
            if (pixelGraphics != null) {
                pixelGraphics.dispose();
            }
            pixelBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
            pixelGraphics = pixelBuffer.createGraphics();
            pixelGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            pixelData = ((DataBufferInt) pixelBuffer.getRaster().getDataBuffer()).getData();
            outBuffer = new BufferedImage(outW, outH, BufferedImage.TYPE_INT_ARGB_PRE);
            outData = ((DataBufferInt) outBuffer.getRaster().getDataBuffer()).getData();
            hData = new int[outW * height];

            xI0 = new int[outW]; xI1 = new int[outW]; xW = new int[outW];
            yI0 = new int[outH]; yI1 = new int[outH]; yW = new int[outH];
            buildSampleTable(outW, width, scaleX, xI0, xI1, xW);
            buildSampleTable(outH, height, scaleY, yI0, yI1, yW);
        }
    }

    // for each output pixel: the art pixel it lands in, the neighbouring art pixel to blend with, and the blend
    // weight (0-128). only output pixels touching the border between two art pixels blend, by their overlap, so
    // every art pixel keeps the same size and crisp edges at any window scale.
    private static void buildSampleTable(int outSize, int artSize, float scale, int[] i0, int[] i1, int[] weight) {
        for (int d = 0; d < outSize; d++) {
            float p = (d + 0.5f) / scale;
            int a = Math.min((int) p, artSize - 1);
            float t = (p - a) * scale;          // output pixels from the art pixel's start edge
            float tEnd = (a + 1 - p) * scale;   // output pixels to the art pixel's end edge
            int neighbour = a;
            float w = 0;
            if (t < 0.5f && a > 0) {
                neighbour = a - 1;
                w = 0.5f - t;
            } else if (tEnd < 0.5f && a < artSize - 1) {
                neighbour = a + 1;
                w = 0.5f - tEnd;
            }
            i0[d] = a;
            i1[d] = neighbour;
            weight[d] = Math.round(w * 256f);
        }
    }

    private static final boolean MULTI_CORE = Runtime.getRuntime().availableProcessors() > 1;

    private static IntStream rows(int count) {
        IntStream rows = IntStream.range(0, count);
        return MULTI_CORE ? rows.parallel() : rows;
    }

    private static int lerp(int p, int q, int w) {
        if (w == 0 || p == q) return p;
        int iw = 256 - w;
        int rb = (((p & 0xFF00FF) * iw + (q & 0xFF00FF) * w) >>> 8) & 0xFF00FF;
        int ag = (((p >>> 8) & 0xFF00FF) * iw + ((q >>> 8) & 0xFF00FF) * w) & 0xFF00FF00;
        return rb | ag;
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

    // draws the pixel grid to the window at device resolution and clears it
    public void flushPixelLayer() {
        if (!pixelLayerDirty || g == null) {
            return;
        }
        pixelLayerDirty = false;

        int outW = outBuffer.getWidth();
        int outH = outBuffer.getHeight();
        int srcW = pixelBuffer.getWidth();
        int srcH = pixelBuffer.getHeight();

        // pass 1: resample each grid row horizontally (once per row, however many output rows use it)
        rows(srcH).forEach(sy -> {
            int src = sy * srcW;
            int dst = sy * outW;
            for (int x = 0; x < outW; x++) {
                int w = xW[x];
                int p = pixelData[src + xI0[x]];
                hData[dst + x] = w == 0 ? p : lerp(p, pixelData[src + xI1[x]], w);
            }
        });

        // pass 2: blend vertically only on rows that touch an art pixel border, plain copies elsewhere
        rows(outH).forEach(y -> {
            int wy = yW[y];
            int row0 = yI0[y] * outW;
            int out = y * outW;
            if (wy == 0) {
                System.arraycopy(hData, row0, outData, out, outW);
            } else {
                int row1 = yI1[y] * outW;
                for (int x = 0; x < outW; x++) {
                    outData[out + x] = lerp(hData[row0 + x], hData[row1 + x], wy);
                }
            }
        });

        // draw 1:1 in device pixels so the window system never rescales it
        Graphics2D device = (Graphics2D) g.create();
        AffineTransform t = g.getTransform();
        device.setTransform(new AffineTransform(1, 0, 0, 1, t.getTranslateX(), t.getTranslateY()));
        device.drawImage(outBuffer, 0, 0, null);
        device.dispose();

        java.util.Arrays.fill(pixelData, 0);
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

    public static double[] getDeviceScale (){
        return new double[]{deviceScaleX, deviceScaleY};
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