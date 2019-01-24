package com.harium.etyl.loader.font;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class FontManagerTest {

    @Test
    public void testGetSystemFontFiles() {
        List<File> files = FontManager.getSystemFontFiles();
        Assert.assertFalse(files.isEmpty());
    }

}
