package com.harium.etyl.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.harium.etyl.core.graphics.Font;
import com.harium.etyl.loader.font.FontManager;
import com.harium.util.SystemUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FontLoader extends Loader {

    private static FontLoader instance = null;

    private static boolean disposed = false;
    private static final int SIZE = 20;

    static Map<String, Font> fonts = new HashMap<>();

    private static String DEFAULT_FONT = "";

    private FontLoader() {
        super();
        setFolder("fonts/");
    }

    public void loadDefaultFont() {
        DEFAULT_FONT = getDefaultFont();

        switch (Gdx.app.getType()) {
            case Android:
                loadAndroidSystemFont(DEFAULT_FONT, SIZE);
                break;
            case Desktop:
            case Applet:
                loadDesktopSystemFont(DEFAULT_FONT, SIZE);
                break;
            default:
                break;
        }
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
        return loadFont(path, size, false);
    }

    public Font loadFont(String path, int size, boolean absolute) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;

        return loadFont(path, parameter, absolute);
    }

    public Font loadFont(String path, FreeTypeFontGenerator.FreeTypeFontParameter parameter, boolean absolute) {
        File file = new File(path);
        BitmapFont font = loadBitmapFont(path, parameter, absolute);

        if (font == null) {
            return defaultFont();
        }

        Font f = new Font(path, font, parameter.size);
        f.setAbsolute(absolute);
        fonts.put(file.getName(), f);

        return f;
    }

    public Font defaultFont() {
        return fonts.get(DEFAULT_FONT);
    }

    private BitmapFont loadBitmapFont(String path, int size, boolean absolute) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;

        return loadBitmapFont(path, parameter, absolute);
    }

    private BitmapFont loadBitmapFont(String path, int size) {
        return loadBitmapFont(path, size, false);
    }

    private BitmapFont loadBitmapFont(String path, FreeTypeFontGenerator.FreeTypeFontParameter parameter, boolean absolute) {
        FileHandle fontFile;
        if (!absolute) {
            fontFile = Gdx.files.internal(fullPath() + path);
        } else {
            fontFile = Gdx.files.absolute(path);
        }

        if (!fontFile.exists() || fontFile.isDirectory()) {
            return null;
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        BitmapFont font = generator.generateFont(parameter);
        antialias(font);
        generator.dispose();

        return font;
    }

    private Font loadAndroidSystemFont(String path, int size) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;

        String fullPath = "/system/fonts/" + path;
        File file = new File(fullPath);
        if (file.exists()) {
            return loadFont(file.getAbsolutePath(), parameter, true);
        } else {
            File parent = new File(file.getParent());
            File files[] = parent.listFiles();
            for (File f : files) {
                // Fallback Font
                if (f.getName().endsWith("Mono.ttf")) {
                    return loadFont(f.getAbsolutePath(), parameter, true);
                }
            }

            return null;
        }
    }

    private Font loadDesktopSystemFont(String path, int size) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;

        List<File> files = FontManager.getSystemFontFiles();
        for (File file : files) {
            if (path.equals(file.getName())) {
                return loadFont(file.getAbsolutePath(), parameter, true);
            }
        }

        return null;
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
        for (Font font : fonts.values()) {
            font.dispose();
        }
        disposed = true;
        fonts.clear();
        instance = null;
    }

    public void reload() {
        if (!disposed) {
            return;
        }
        for (Font font : fonts.values()) {
            BitmapFont bitmapFont = loadBitmapFont(font.getPath(), (int) font.getOriginalSize(), font.isAbsolute());
            font.setFont(bitmapFont);
            font.setSize((int) font.getOriginalSize());
        }
        disposed = false;
    }

    private static String getDefaultFont() {
        switch (Gdx.app.getType()) {
            case Applet:
            case Desktop:
                if (SystemUtils.IS_OS_LINUX) {
                    return "DejaVuSans.ttf";
                } else {
                    return "Arial.ttf";
                }
            case Android:
                return "Roboto.ttf";
            default:
                return "Arial.ttf";
        }
    }

}
