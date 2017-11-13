package com.harium.etyl.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashSet;
import java.util.Set;

import com.harium.etyl.core.graphics.Font;

public class FontLoader extends Loader {

    private static FontLoader instance = null;

    private static boolean disposed = false;
    private static final int SIZE = 20;

    private static Set<Font> fonts = new HashSet<>();

    private FontLoader() {
        super();
        setFolder("fonts/");
    }

    public static FontLoader getInstance() {
        if (instance == null) {
            instance = new FontLoader();
        }
        return instance;
    }

    public Font loadFont(String path) {
        return loadFont(path, SIZE);
    }

    public Font loadFont(String path, int size) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;

        return loadFont(path, parameter);
    }

    public Font loadFont(String path, FreeTypeFontGenerator.FreeTypeFontParameter parameter) {
        BitmapFont font = loadBitmapFont(path, parameter);

        Font f = new Font(path, font, parameter.size);
        fonts.add(f);
        return f;
    }

    private BitmapFont loadBitmapFont(String path, int size) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;

        return loadBitmapFont(path, parameter);
    }

    private BitmapFont loadBitmapFont(String path, FreeTypeFontGenerator.FreeTypeFontParameter parameter) {
        FileHandle fontFile = Gdx.files.internal(getFolder() + path);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        BitmapFont font = generator.generateFont(parameter);
        antialias(font);
        generator.dispose();

        return font;
    }

    private void antialias(BitmapFont font) {
        font.getRegion().getTexture().setFilter(
                Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
    }

    public void dispose(Font font) {
        font.dispose();
        fonts.remove(font);
    }

    public void dispose() {
        if (disposed) {
            return;
        }
        for (Font font : fonts) {
            font.dispose();
        }
        disposed = true;
    }

    public void reload() {
        if (!disposed) {
            return;
        }
        for (Font font : fonts) {
            BitmapFont bitmapFont = loadBitmapFont(font.getPath(), (int) font.getOriginalSize());
            font.setFont(bitmapFont);
            font.setSize((int) font.getOriginalSize());
        }
        disposed = false;
    }

}
