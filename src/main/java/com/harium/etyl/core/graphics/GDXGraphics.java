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
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.harium.etyl.commons.layer.GeometricLayer;
import com.harium.etyl.commons.layer.Layer;

public class GDXGraphics implements Graphics {
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
    Color currentColor = Color.BLACK;

    public GDXGraphics(int width, int height) {
        super();

        this.width = width;
        this.height = height;

        batch = new SpriteBatch();
        resetAlpha();

        shapeRenderer = new ShapeRenderer();
        font = new Font();
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

    public void setFontColor(int color) {
        font.getFont().setColor(buildColor(color));
    }

    public void setFontSize(float size) {
        if (font.getSize() == (int) size) {
            return;
        }

        font.getFont().getData().setScale(size / font.getOriginalSize());
        font.setSize((int) size);
        updateFontFix();
    }

    private void updateFontFix() {
        fontOffsetFix = -font.getSize() / 8;
    }

    private Color buildColor(int color) {
        return new Color((color << 8) + alpha);
    }

    @Override
    public void drawArc(int x, int y, int w, int h, int startAngle, int arcAngle) {
        int radius = w / 2 + 1;
        int cx = x + radius;
        int cy = height - y - h / 2 - 1;

        endBatch();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(currentColor);
        arc(cx, cy, radius, startAngle, arcAngle, ShapeRenderer.ShapeType.Line);
        shapeRenderer.end();
    }

    public void drawRect(GeometricLayer layer) {
        drawRect(layer.getX(), layer.getY(), layer.getW(), layer.getH());
    }

    public void drawRect(int x, int y, int w, int h) {
        endBatch();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(currentColor);
        shapeRenderer.rect(x, height - y - h, w, h);
        shapeRenderer.end();
    }

    public void drawOval(int x, int y, int w, int h) {
        endBatch();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(currentColor);
        shapeRenderer.ellipse(x, height - y - h, w, h);
        shapeRenderer.end();
    }

    public void drawOval(GeometricLayer layer) {
        drawOval(layer.getX(), layer.getY(), layer.getW(), layer.getH());
    }

    public void drawCircle(int cx, int cy, int radius) {
        drawOval(cx - radius, cy - radius, radius * 2, radius * 2);
    }

    @Override
    public void fillArc(int x, int y, int w, int h, int startAngle, int arcAngle) {
        int radius = w / 2 + 1;
        int cx = x + radius;
        int cy = height - y - h / 2 - 1;

        endBatch();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(currentColor);
        arc(cx, cy, radius, startAngle, arcAngle, ShapeRenderer.ShapeType.Filled);
        shapeRenderer.end();
    }

    public void fillRect(GeometricLayer layer) {
        fillRect(layer.getX(), layer.getY(), layer.getW(), layer.getH());
    }

    public void fillRect(int x, int y, int w, int h) {
        endBatch();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(currentColor);
        shapeRenderer.rect(x, height - y - h, w, h);
        shapeRenderer.end();
    }

    public void fillOval(int x, int y, int w, int h) {
        endBatch();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(currentColor);
        shapeRenderer.ellipse(x, height - y - h, w, h);
        shapeRenderer.end();
    }

    public void fillOval(GeometricLayer layer) {
        fillOval(layer.getX(), layer.getY(), layer.getW(), layer.getH());
    }

    public void fillCircle(int cx, int cy, int radius) {
        fillOval(cx - radius, cy - radius, radius * 2, radius * 2);
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
        beginBatch();
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font.getFont(), text);

        float cx = width / 2 - layout.width / 2;
        drawFont(text, cx, y);
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
        endBatch();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(currentColor);
        shapeRenderer.line(x1, height - y1, x2, height - y2);
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