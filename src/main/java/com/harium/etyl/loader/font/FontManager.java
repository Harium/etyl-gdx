package com.harium.etyl.loader.font;

import com.harium.util.SystemUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Code from: https://github.com/UnderwaterApps/overlap2d/blob/master/overlap2d/src/com/uwsoft/editor/proxy/FontManager.java
 */
public class FontManager {

    public static String[] getSystemFontsPaths() {
        String[] result;
        if (SystemUtils.IS_OS_WINDOWS) {
            result = new String[1];
            String path = System.getenv("WINDIR");
            result[0] = path + "\\" + "Fonts";
            return result;
        } else if (SystemUtils.IS_OS_MAC) {
            result = new String[3];
            result[0] = System.getProperty("user.home") + File.separator + "Library/Fonts";
            result[1] = "/Library/Fonts";
            result[2] = "/System/Library/Fonts";
            return result;
        } else if (SystemUtils.IS_OS_LINUX) {
            String[] pathsToCheck = {
                    System.getProperty("user.home") + File.separator + ".fonts",
                    "/usr/share/fonts/truetype",
                    "/usr/share/fonts/TTF",
                    "/usr/share/fonts"
            };
            ArrayList<String> resultList = new ArrayList<>();

            for (int i = pathsToCheck.length - 1; i >= 0; i--) {
                String path = pathsToCheck[i];
                File tmp = new File(path);
                if (tmp.exists() && tmp.isDirectory() && tmp.canRead()) {
                    resultList.add(path);
                }
            }

            if (resultList.isEmpty()) {
                result = new String[0];
            } else {
                result = new String[resultList.size()];
                result = resultList.toArray(result);
            }

            return result;
        }

        return null;
    }

    public static List<File> getSystemFontFiles() {
        // only retrieving ttf files
        String[] extensions = new String[]{"ttf", "TTF"};
        String[] paths = getSystemFontsPaths();

        if (paths.length == 0) {
            return new ArrayList<>();
        }

        List<File> files = new ArrayList<>();

        for (int i = 0; i < paths.length; i++) {
            File fontDirectory = new File(paths[i]);
            if (!fontDirectory.exists()) {
                break;
            }
            files.addAll(listFiles(fontDirectory, extensions));
        }

        return files;
    }

    private static List<File> listFiles(File path, String[] extensions) {
        List<File> files = new ArrayList<>();
        for (File file : path.listFiles()) {
            if (!file.exists()) {
                continue;
            }
            if (file.isDirectory()) {
                files.addAll(listFiles(file, extensions));
            }
            for (String extension : extensions) {
                if (file.isFile() && file.getName().endsWith(extension)) {
                    files.add(file);
                }
            }
        }

        return files;
    }
}
