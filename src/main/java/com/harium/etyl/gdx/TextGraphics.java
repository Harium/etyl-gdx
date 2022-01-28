package com.harium.etyl.gdx;

import com.harium.etyl.commons.graphics.Color;
import com.harium.etyl.commons.layer.GeometricLayer;
import com.harium.etyl.core.graphics.Font;

public interface TextGraphics {

    void setFontColor(Color color);

    void setFontColor(int color);

    void setFontSize(float size);

    void setFontStyle(int style);

    int textWidth(String text);

    void drawString(String text, int x, int y);

    void drawString(String text, int x, int y, int w, int h);

    void drawString(String text, GeometricLayer layer);

    void drawString(String text, GeometricLayer layer, int offsetX, int offsetY);

    void drawStringX(String text, int y);

    void drawStringX(String text, int offsetX, int y);

    void setFont(Font font);

}
