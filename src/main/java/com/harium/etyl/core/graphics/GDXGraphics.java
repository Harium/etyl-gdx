package com.harium.etyl.core.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.harium.etyl.commons.layer.GeometricLayer;
import com.harium.etyl.commons.layer.Layer;
import com.harium.etyl.gdx.BridgeGraphics;
import com.harium.etyl.geometry.Line2;
import com.harium.etyl.geometry.Point2D;
import com.harium.etyl.geometry.math.Vector2i;

public class GDXGraphics implements BridgeGraphics {
    private float fontOffsetFix = 2;

    private int width = 0;
    private int height = 0;

    private boolean spriteBatchOpen = false;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Font font;

    boolean alphaEnabled = false;
    boolean blendMode = false;
    boolean colorHasAlpha = false;

    private float lineWidth = 0;
    private int alpha = 0xff;
    private Camera orthographicCamera;

    private boolean definedFont = false;

    private Color currentColor = new Color(Color.BLACK);
    private Color background = new Color(Color.WHITE);
    private Color shadowColor = new Color(Color.BLACK);

    public GDXGraphics(int width, int height) {
        super();

        this.width = width;
        this.height = height;

        batch = new SpriteBatch();
        resetAlpha();

        shapeRenderer = new ShapeRenderer();
        font = new Font();
    }

    @Override
    public void setFps(float fps) {
        // Do nothing, GDX takes care of it
    }

    @Override
    public void resetTransform() {
        batch.getTransformMatrix().idt();
    }

    @Override
    public float getFps() {
        return Gdx.graphics.getFramesPerSecond();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setBatchColor(Color color) {
        if (color.a != 1) {
            alpha = (int) (color.a * Layer.MAX_OPACITY);
            colorHasAlpha = true;
            // If alpha is enabled or don't, keep color's alpha
        } else {
            // If color is opaque but alpha is enabled
            if (alphaEnabled) {
                // Keep currentColor's alpha
                color.a = currentColor.a;
            } else if (colorHasAlpha) {
                // If alpha is disabled, ignore color's alpha
                colorHasAlpha = false;
            }
        }

        this.currentColor = color;
    }

    public void setBatchColor(int color) {
        setBatchColor(buildColor(color));
    }

    public void setColor(Color color) {
        setBatchColor(color);
        if (definedFont) {
            font.getFont().setColor(color);
        }
    }

    public void setColor(int color) {
        setColor(buildColor(color));
    }

    public void setFontColor(Color color) {
        font.getFont().setColor(color);
    }

    @Override
    public void setFontColor(com.harium.etyl.commons.graphics.Color color) {
        font.getFont().setColor(new Color(color.getRGB()));
    }

    public void setFontColor(int color) {
        font.getFont().setColor(buildColor(color));
    }

    @Override
    public void setShadowColor(com.harium.etyl.commons.graphics.Color shadowColor) {
        this.shadowColor.set(shadowColor.getRGB());
    }

    public void setFontSize(float size) {
        if (font.getSize() == (int) size) {
            return;
        }

        font.getFont().getData().setScale(size / font.getOriginalSize());
        font.setSize((int) size);
        updateFontFix();
    }

    @Override
    public void setFontStyle(int style) {
        font.setStyle(style);
    }

    private void updateFontFix() {
        fontOffsetFix = -font.getSize() / 8;
    }

    private Color buildColor(int color) {
        return new Color((color << 8) + alpha);
    }

    @Override
    public void beginImageBatch() {
        batch.begin();
    }

    @Override
    public void endImageBatch() {
        batch.end();
    }

    @Override
    public void drawArc(int x, int y, int w, int h, int startAngle, int arcAngle) {
        drawArc((float) x, (float) y, (float) w, (float) h, startAngle, arcAngle);
    }

    @Override
    public void drawArc(float x, float y, float w, float h, int startAngle, int arcAngle) {
        float radius = w / 2 + 1;
        float cx = x + radius;
        float cy = height - y - h / 2 - 1;

        endBatch();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(currentColor);
        arc(cx, cy, radius, startAngle, arcAngle, ShapeRenderer.ShapeType.Line);
        shapeRenderer.end();
    }

    public void drawRect(GeometricLayer layer) {
        drawRect(layer.getX(), layer.getY(), layer.getW(), layer.getH());
    }

    @Override
    public void drawRect(Layer layer) {
        drawRect(layer.getX(), layer.getY(), layer.getW(), layer.getH());
    }

    @Override
    public void drawRect(int x, int y, int w, int h) {
        drawRect((float) x, (float) y, (float) w, (float) h);
    }

    @Override
    public void drawRect(double x, double y, double w, double h) {
        drawRect((float) x, (float) y, (float) w, (float) h);
    }

    public void drawRect(float x, float y, float w, float h) {
        endBatch();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(currentColor);
        shapeRenderer.rect(x, height - y - h, w, h);
        shapeRenderer.end();
    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        int radius = arcWidth;

        endBatch();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(currentColor);
        // Central rectangle
        drawRect(x + radius, y + radius, width - 2 * radius, height - 2 * radius);

        // Four side rectangles, in clockwise order
        drawRect(x + radius, y, width - 2 * radius, radius);
        drawRect(x + width - radius, y + radius, radius, height - 2 * radius);
        drawRect(x + radius, y + height - radius, width - 2 * radius, radius);
        drawRect(x, y + radius, radius, height - 2 * radius);

        // Four arches, clockwise too
        drawArc(x + radius, y + radius, radius, radius, (int) 180f, (int) 90f);
        drawArc(x + width - radius, y + radius, radius, radius, (int) 270f, (int) 90f);
        drawArc(x + width - radius, y + height - radius, radius, radius, (int) 0f, (int) 90f);
        drawArc(x + radius, y + height - radius, radius, radius, (int) 90f, (int) 90f);
        shapeRenderer.end();
    }

    @Override
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        int radius = arcWidth;

        endBatch();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(currentColor);
        // Central rectangle
        drawRect(x + radius, y + radius, width - 2 * radius, height - 2 * radius);

        // Four side rectangles, in clockwise order
        drawRect(x + radius, y, width - 2 * radius, radius);
        drawRect(x + width - radius, y + radius, radius, height - 2 * radius);
        drawRect(x + radius, y + height - radius, width - 2 * radius, radius);
        drawRect(x, y + radius, radius, height - 2 * radius);

        // Four arches, clockwise too
        drawArc(x + radius, y + radius, radius, radius, (int) 180f, (int) 90f);
        drawArc(x + width - radius, y + radius, radius, radius, (int) 270f, (int) 90f);
        drawArc(x + width - radius, y + height - radius, radius, radius, (int) 0f, (int) 90f);
        drawArc(x + radius, y + height - radius, radius, radius, (int) 90f, (int) 90f);
        shapeRenderer.end();
    }

    @Override
    public void drawOval(int x, int y, int w, int h) {
        drawOval((float) x,(float)  y,(float)  w,(float)  h);
    }

    @Override
    public void drawOval(float x, float y, float w, float h) {
        endBatch();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(currentColor);
        shapeRenderer.ellipse(x, height - y - h, w, h);
        shapeRenderer.end();
    }

    @Override
    public void drawOval(GeometricLayer layer) {
        drawOval(layer.getX(), layer.getY(), layer.getW(), layer.getH());
    }

    @Override
    public void drawCircle(int cx, int cy, int radius) {
        drawCircle((float) cx, (float) cy, (float) radius);
    }

    @Override
    public void drawCircle(double cx, double cy, double radius) {
        drawCircle((float) cx, (float) cy, (float) radius);
    }

    @Override
    public void drawCircle(Point2D point, int radius) {
        drawCircle(point.x, point.y, radius);
    }

    @Override
    public void drawCircle(float cx, float cy, float radius) {
        drawOval(cx - radius, cy - radius, radius * 2, radius * 2);
    }

    @Override
    public void fillArc(int x, int y, int w, int h, int startAngle, int arcAngle) {
        fillArc((float) x, (float) y, (float) w, (float) h, startAngle, arcAngle);
    }

    @Override
    public void fillArc(float x, float y, float w, float h, int startAngle, int arcAngle) {
        float radius = w / 2 + 1;
        float cx = x + radius;
        float cy = height - y - h / 2 - 1;

        endBatch();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(currentColor);
        arc(cx, cy, radius, startAngle, arcAngle, ShapeRenderer.ShapeType.Filled);
        shapeRenderer.end();
    }

    @Override
    public void fillArc(GeometricLayer layer, int startAngle, int arcAngle) {
        fillArc((float) layer.getX(), (float) layer.getY(), (float) layer.getW(), (float) layer.getH(), startAngle, arcAngle);
    }

    public void fillRect(GeometricLayer layer) {
        fillRect(layer.getX(), layer.getY(), layer.getW(), layer.getH());
    }

    @Override
    public void fillRect(Layer layer) {
        fillRect(layer.getX(), layer.getY(), layer.getW(), layer.getH());
    }

    public void fillRect(int x, int y, int w, int h) {
        fillRect((float) x,(float)  y,(float)  w,(float) h);
    }

    @Override
    public void fill3DRect(int x, int y, int w, int h, boolean raised) {
        fill3DRect((float) x, (float) y, (float) w, (float) h, raised);
    }

    @Override
    public void fillRect(float x, float y, float w, float h) {
        endBatch();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(currentColor);
        shapeRenderer.rect(x, height - y - h, w, h);
        shapeRenderer.end();
    }

    @Override
    public void fill3DRect(float x, float y, float w, float h, boolean raised) {
        fillRect(x, y, w, h);

        Color high = Color.WHITE;
        Color low = Color.BLACK;

        if (!raised) {
            low = Color.WHITE;
            high = Color.BLACK;
        }

        setColor(high);
        drawLine(x, y, x + w, y);
        drawLine(x, y + 1, x, y + 1 + h - 1);
        setColor(low);
        drawLine(x, y + h, x + w, y + h);
        drawLine(x + w, y + 1, x + w, y + 1 + h);
    }

    public void fillOval(int x, int y, int w, int h) {
        fillOval((float) x, (float) y, (float) w, (float) h);
    }

    @Override
    public void fillOval(float x, float y, float w, float h) {
        endBatch();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(currentColor);
        shapeRenderer.ellipse(x, height - y - h, w, h);
        shapeRenderer.end();
    }

    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        float[] vertices = new float[nPoints * 2];
        for (int i = 0; i < nPoints; i++) {
            vertices[i * 2] = xPoints[i];
            vertices[i * 2 + 1] = yPoints[i];
        }

        endBatch();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(currentColor);
        shapeRenderer.polygon(vertices);
        shapeRenderer.end();
    }

    @Override
    public void translate(int x, int y) {
        batch.getTransformMatrix().translate(x, y, 0);
        shapeRenderer.getProjectionMatrix().translate(x, y, 0);
    }

    @Override
    public void translate(double x, double y) {
        batch.getTransformMatrix().translate((float) x, (float) y, 0);
        shapeRenderer.getProjectionMatrix().translate((float) x, (float) y, 0);
    }

    @Override
    public void rotate(double angle) {
        batch.getTransformMatrix().rotate(Vector3.Z, (float) angle);
        shapeRenderer.getProjectionMatrix().rotate(Vector3.Z, (float) angle);
    }

    @Override
    public void setBackground(com.harium.etyl.commons.graphics.Color color) {
        background.set(color.getRGB());
    }

    @Override
    public void clearRect(int x, int y, int width, int height) {
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        if (font != null) {
            font.dispose();
        }
    }

    @Override
    public void drawArrow(Point2D p, Point2D q) {
        drawArrow(p, q, 25);
    }

    @Override
    public void drawArrow(Point2D p, Point2D q, int arrowSize) {
        double pq = p.distance(q);

        int arrowAngle = 30;

        Point2D p1 = p.distantPoint(q, pq + arrowSize);
        Point2D p2 = new Point2D(p1.x, p1.y);

        p1.rotate(q, 180 - arrowAngle);
        p2.rotate(q, 180 + arrowAngle);

        drawLine(p, q);
        drawLine(q, p1);
        drawLine(q, p2);
    }

    public void fillOval(GeometricLayer layer) {
        fillOval(layer.getX(), layer.getY(), layer.getW(), layer.getH());
    }

    @Override
    public void fillCircle(float cx, float cy, float radius) {
        fillOval(cx - radius, cy - radius, radius * 2, radius * 2);
    }

    public void fillCircle(int cx, int cy, int radius) {
        fillCircle((float) cx, (float) cy, (float) radius);
    }

    @Override
    public void fillCircle(double cx, double cy, double radius) {
        fillCircle((float) cx, (float) cy, (float) radius);
    }

    @Override
    public void fillCircle(Point2D point, int radius) {
        fillCircle(point.x, point.y, radius);
    }

    @Override
    public void fillCircle(Vector2i point, int radius) {
        fillCircle(point.x, point.y, radius);
    }

    @Override
    public int textWidth(String text) {
        if (!definedFont) {
            return 0;
        }
        GlyphLayout layout = new GlyphLayout(font.getFont(), text);
        return (int) layout.width;
    }

    public void drawString(String text, int x, int y) {
        beginBatch();
        drawFont(text, x, y - font.getFont().getCapHeight());
    }

    public void drawString(String text, int x, int y, int w, int h) {
        beginBatch();

        GlyphLayout layout = new GlyphLayout();
        layout.setText(font.getFont(), text);

        float cx = w / 2 - layout.width / 2;
        float cy = h / 2 - layout.height / 2;

        drawFont(text, x + cx, y + cy);
    }

    public void drawString(String text, GeometricLayer layer) {
        drawString(text, layer.getX(), layer.getY(), layer.getW(), layer.getH());
    }

    public void drawString(String text, GeometricLayer layer, int offsetX, int offsetY) {
        drawString(text, layer.getX() + offsetX, layer.getY() + offsetY, layer.getW(), layer.getH());
    }

    public void drawStringX(String text, int y) {
        drawStringX(text, 0, y);
    }

    public void drawStringX(String text, int offsetX, int y) {
        beginBatch();
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font.getFont(), text);

        float cx = width / 2 - layout.width / 2;
        drawFont(text, cx + offsetX, y);
    }

    @Override
    public void putPixel(int x, int y) {
        endBatch();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Point);
        shapeRenderer.setColor(currentColor);
        shapeRenderer.point(x, height - y, 0);
        shapeRenderer.end();
    }

    private void drawFont(String text, float x, float y) {
        font.getFont().draw(batch, text, x, height - y - fontOffsetFix);
    }

    public void setProjectionMatrix(Matrix4 projection) {
        shapeRenderer.setProjectionMatrix(projection);
        batch.setProjectionMatrix(projection);
    }

    public void setFont(Font font) {
        this.font = font;
        definedFont = true;
        updateFontFix();
        // Set color if it was defined before the font was setted
        font.getFont().setColor(currentColor);
    }

    public BitmapFont getFont() {
        return font.getFont();
    }

    public void setLineWidth(float width) {
        this.lineWidth = width;
        Gdx.gl20.glLineWidth(width);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        drawLine((float) x1, y1, x2, y2);
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2) {
        endBatch();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(currentColor);
        shapeRenderer.line(x1, height - y1, x2, height - y2);
        shapeRenderer.end();
    }

    @Override
    public void drawLine(Point2D p, Point2D q) {
        drawLine((float) p.x, (float) p.y, (float) q.x, (float) q.y);
    }

    @Override
    public void drawLine(Line2 line) {
        drawLine(line.getP1().x, line.getP1().y, line.getP2().x, line.getP2().y);
    }

    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        float[] vertices = new float[nPoints * 2];
        for (int i = 0; i < nPoints; i++) {
            vertices[i * 2] = xPoints[i];
            vertices[i * 2 + 1] = yPoints[i];
        }

        endBatch();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(currentColor);
        shapeRenderer.polygon(vertices);
        shapeRenderer.end();
    }

    public void setAlpha(int alpha) {
        setOpacity((alpha * 0xff) / 100);
    }

    public void setOpacity(int opacity) {
        endBatch();
        if (!alphaEnabled) {
            alphaEnabled = true;
        }
        beginBatch();
        this.alpha = opacity;

        currentColor.a = this.alpha / (float) Layer.MAX_OPACITY;
        batch.setColor(1, 1, 1, currentColor.a);
    }

    public void resetOpacity() {
        resetAlpha();
    }

    public void resetAlpha() {
        alphaEnabled = false;
        alpha = 0xff;
        currentColor.a = this.alpha / (float) Layer.MAX_OPACITY;
        batch.setColor(1, 1, 1, currentColor.a);
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setClip(int x, int y, int w, int h) {
        Rectangle scissors = new Rectangle();
        Rectangle clipBounds = new Rectangle(x, y, w, h);
        ScissorStack.calculateScissors(orthographicCamera, batch.getTransformMatrix(), clipBounds, scissors);
        ScissorStack.pushScissors(scissors);
    }

    public void resetClip() {
        ScissorStack.popScissors();
    }

    public void setOrthographicCamera(Camera orthographicCamera) {
        this.orthographicCamera = orthographicCamera;
    }

    public Camera getOrthographicCamera() {
        return orthographicCamera;
    }

    public void setColor(com.harium.etyl.commons.graphics.Color color) {
        float r = (float) color.getRed() / 255;
        float g = (float) color.getGreen() / 255;
        float b = (float) color.getBlue() / 255;

        float a = (float) color.getAlpha() / 255;

        setColor(new Color(r, g, b, a));
    }

    @Override
    public SpriteBatch getBatch() {
        return beginBatch();
    }

    /**
     * Helper method to begin the SpriteBatch,
     * it handles opacity based on previous configurations
     *
     * @return SpriteBatch
     */
    public SpriteBatch beginBatch() {
        if (blendMode && !alphaEnabled && !colorHasAlpha) {
            Gdx.gl.glDisable(GL20.GL_BLEND);
            blendMode = false;
        }
        if (!spriteBatchOpen) {
            batch.begin();
            spriteBatchOpen = true;
        }
        return batch;
    }

    /**
     * Helper method to end the SpriteBatch
     *
     * @return SpriteBatch
     */
    public SpriteBatch endBatch() {
        if (spriteBatchOpen) {
            batch.end();
            spriteBatchOpen = false;
        }
        if (alphaEnabled || colorHasAlpha) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
            blendMode = true;
        }
        return batch;
    }

    public void flush() {
        endBatch();
    }

    // Extracted from ShapeRenderer
    private void arc(float x, float y, float radius, float start, float degrees, ShapeRenderer.ShapeType shapeType) {
        arc(x, y, radius, start, degrees, Math.max(1, (int) (6 * (float) Math.cbrt(radius) * (degrees / 360.0f))), shapeType);
    }

    private void arc(float x, float y, float radius, float start, float degrees, int segments, ShapeRenderer.ShapeType shapeType) {
        if (segments <= 0) throw new IllegalArgumentException("segments must be > 0.");
        float colorBits = shapeRenderer.getColor().toFloatBits();
        float theta = (2 * MathUtils.PI * (degrees / 360.0f)) / segments;
        float cos = MathUtils.cos(theta);
        float sin = MathUtils.sin(theta);
        float cx = radius * MathUtils.cos(start * MathUtils.degreesToRadians);
        float cy = radius * MathUtils.sin(start * MathUtils.degreesToRadians);

        if (shapeType == ShapeRenderer.ShapeType.Line) {
            for (int i = 0; i < segments; i++) {
                shapeRenderer.getRenderer().color(colorBits);
                shapeRenderer.getRenderer().vertex(x + cx, y + cy, 0);
                float temp = cx;
                cx = cos * cx - sin * cy;
                cy = sin * temp + cos * cy;
                shapeRenderer.getRenderer().color(colorBits);
                shapeRenderer.getRenderer().vertex(x + cx, y + cy, 0);
            }
        } else {
            for (int i = 0; i < segments; i++) {
                shapeRenderer.getRenderer().color(colorBits);
                shapeRenderer.getRenderer().vertex(x, y, 0);
                shapeRenderer.getRenderer().color(colorBits);
                shapeRenderer.getRenderer().vertex(x + cx, y + cy, 0);
                float temp = cx;
                cx = cos * cx - sin * cy;
                cy = sin * temp + cos * cy;
                shapeRenderer.getRenderer().color(colorBits);
                shapeRenderer.getRenderer().vertex(x + cx, y + cy, 0);
            }
            shapeRenderer.getRenderer().color(colorBits);
            shapeRenderer.getRenderer().vertex(x, y, 0);
            shapeRenderer.getRenderer().color(colorBits);
            shapeRenderer.getRenderer().vertex(x + cx, y + cy, 0);
        }

        cx = 0;
        cy = 0;
        shapeRenderer.getRenderer().color(colorBits);
        shapeRenderer.getRenderer().vertex(x + cx, y + cy, 0);
    }

}