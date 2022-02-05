package com.harium.etyl.gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public interface GDXTextGraphics extends TextGraphics {

    void setFontColor(Color color);

    BitmapFont getFont();

}
