package com.harium.etyl.layer;

import com.harium.etyl.commons.graphics.Color;
import com.harium.etyl.commons.graphics.Graphics;
import com.harium.etyl.commons.layer.Layer;
import com.harium.etyl.core.graphics.Font;
import com.harium.etyl.gdx.TextGraphics;

public class TextLayer extends Layer {

    private String text;
    private int style = Font.PLAIN;
    private float size = 16;

    private Font font = null;
    private String fontName = "";

    private com.harium.etyl.commons.graphics.Color color = com.harium.etyl.commons.graphics.Color.BLACK;

    private com.harium.etyl.commons.graphics.Color borderColor = Color.GRAY;

    private boolean border = false;

    private float borderWidth = 4f;
    private boolean antiAliased = true;
    private boolean fractionalMetrics = false;

    public TextLayer() {
        super();
    }

    public TextLayer(String text) {
        super(0, 0);
        this.text = text;
    }

    public TextLayer(int x, int y, String text) {
        super(x, y);
        this.text = text;
    }

    public void simpleDraw(Graphics g, int x, int y) {
        // Update Size
        ((TextGraphics) g).setFont(font);

        if (!border) {
            g.setColor(color);
            ((TextGraphics) g).drawString(text, x, y);
        } else {
            // Draw text with border
            g.setColor(color);
            ((TextGraphics) g).drawString(text, x, y);
        }
    }

    @Override
    public void draw(Graphics g) {
        simpleDraw(g, x, y);
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public boolean isBorder() {
        return border;
    }

    public void setBorder(boolean border) {
        this.border = border;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }
}