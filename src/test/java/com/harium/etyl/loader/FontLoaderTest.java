package com.harium.etyl.loader;

import com.harium.etyl.core.graphics.Font;
import com.harium.etyl.util.PathHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import test.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class FontLoaderTest {

    public static final String LOCAL_FONT = "04B_03.ttf";

    @Before
    public void setUp() {
        FontLoader.getInstance().dispose();
        Assets assetManager = new Assets();
        FontLoader.getInstance().setAssets(assetManager);
        FontLoader.getInstance().setUrl(PathHelper.currentDirectory() + "assets/");
    }

    @Test
    public void testLoadInternalFont() {
        Font font = FontLoader.getInstance().loadFont(LOCAL_FONT, 20);
        Assert.assertNotNull(font);
        Assert.assertEquals(LOCAL_FONT, font.getPath());
    }

    @Test
    public void testLoadInvalidFont() {
        Font font = FontLoader.getInstance().loadFont("", 20);
        Assert.assertNotNull(font);
    }

    @Test
    public void testReloadFonts() {
        FontLoader.getInstance().loadFont(LOCAL_FONT, 20);
        Assert.assertEquals(2, FontLoader.fonts.size());
        FontLoader.getInstance().reload();
        Assert.assertFalse(FontLoader.fonts.isEmpty());
    }
}
