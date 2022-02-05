package com.harium.etyl.gdx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.harium.etyl.commons.graphics.Graphics;
import com.harium.etyl.commons.layer.GeometricLayer;

public interface BridgeGraphics extends Graphics, GDXTextGraphics {

    SpriteBatch getBatch();

    void setBatchColor(Color color);

    void setBatchColor(int color);

    void setColor(Color color);

    void drawOval(GeometricLayer layer);

    void setProjectionMatrix(Matrix4 projection);

    void setLineWidth(float width);

    float getLineWidth();

    void setClip(int x, int y, int w, int h);

    void resetClip();

    void setOrthographicCamera(Camera orthographicCamera);

    Camera getOrthographicCamera();

    void flush();
}
