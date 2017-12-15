package com.harium.etyl.core.graphics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.harium.etyl.commons.layer.GeometricLayer;

public interface Graphics {

    int getWidth();

    int getHeight();

    SpriteBatch getBatch();

    void setBatchColor(Color color);

    void setBatchColor(int color);

    void setColor(Color color);

    void setColor(int color);

    void setFontColor(Color color);

    void setFontColor(int color);

    void setFontSize(float size);

    void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle);

    void drawRect(GeometricLayer layer);

    void drawRect(int x, int y, int w, int h);

    void drawOval(int x, int y, int w, int h);

    void drawOval(GeometricLayer layer);

    void drawCircle(int cx, int cy, int radius);

    void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle);

    void fillRect(GeometricLayer layer);

    void fillRect(int x, int y, int w, int h);

    void fillOval(int x, int y, int w, int h);

    void fillOval(GeometricLayer layer);

    void fillCircle(int cx, int cy, int radius);

    int textWidth(String text);

    void drawString(String text, int x, int y);

    void drawString(String text, int x, int y, int w, int h);

    void drawString(String text, GeometricLayer layer);

    void drawString(String text, GeometricLayer layer, int offsetX, int offsetY);

    void drawStringX(String text, int y);

    void setProjectionMatrix(Matrix4 projection);

    void setFont(Font font);

    BitmapFont getFont();

    void setLineWidth(float width);

    void drawLine(int x1, int y1, int x2, int y2);

    void setAlpha(int alpha);

    void setOpacity(int opacity);

    void resetOpacity();

    void resetAlpha();

    float getLineWidth();

    void setClip(int x, int y, int w, int h);

    void resetClip();

    void setOrthographicCamera(Camera orthographicCamera);

    Camera getOrthographicCamera();

    void setColor(com.harium.etyl.commons.graphics.Color color);

    void flush();
}
