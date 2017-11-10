package com.harium.etyl.loader.async;

import com.harium.etyl.loader.Assets;

public class AsyncLoader {
    protected Assets assets;

    private String folder;
    private String path = "";

    public Assets getAssets() {
        return assets;
    }

    public void setAssets(Assets assets) {
        this.assets = assets;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String prefix) {
        this.path = prefix;
    }

    public String getFolder() {
        return path + folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
}
