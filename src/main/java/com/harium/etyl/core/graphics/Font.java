package com.harium.etyl.core.graphics;


import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Font {

    public static final int PLAIN = 0;
    public static final int BOLD = 1;
    public static final int ITALIC = 2;

    String path;
    boolean absolute = false;

    private float originalSize;
    private float size;
    private int style;

    private BitmapFont font;

    public Font() {

    }

    public Font(String path, BitmapFont font, int size) {
        this.path = path;
        this.font = font;
        this.originalSize = size;
        this.size = size;
    }

    public BitmapFont getFont() {
        return font;
    }

    public float getSize() {
        return size;
    }

    public float getOriginalSize() {
        return originalSize;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }

    public void dispose() {
        font.dispose();
    }

    public String getPath() {
        return path;
    }

    public boolean isAbsolute() {
        return absolute;
    }

    public void setAbsolute(boolean absolute) {
        this.absolute = absolute;
    }

    public void setStyle(int style) {
        this.style = style;
    }
}
