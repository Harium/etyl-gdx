package com.harium.etyl.loader.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.harium.etyl.layer.StaticLayer;
import com.harium.etyl.loader.Loader;
import com.harium.etyl.loader.async.AsyncResource;
import com.harium.etyl.util.io.IOHelper;

import java.io.InputStream;

public class ImageLoader extends Loader {

    private static ImageLoader instance = null;

    private ImageLoader() {
        super();
        setFolder("images/");
    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }

        return instance;
    }

    public Texture loadTexture(String path) {
        return new Texture(fullPath() + path);
    }

    public Texture loadTexture(String path, boolean absolute) {
        if (absolute) {
            return new Texture(path);
        } else {
            return loadTexture(path);
        }
    }

    public void loadTextureAsync(String path, boolean absolute, AsyncResource<Texture> resource) {
        String realPath = path;
        if (!path.startsWith(IOHelper.STREAM_PREFIX)) {
            if (!absolute) {
                realPath = fullPath() + path;
            }
        } else {
            //Stream file
            realPath = path.substring(IOHelper.STREAM_PREFIX.length());
            if (!absolute) {
                realPath = fullPath() + realPath;
            }
        }
        assets.load(realPath, Texture.class);
        assets.addResource(realPath, resource);
    }

    public boolean isLoaded(String path) {
        return isLoaded(path, false);
    }

    public boolean isLoaded(String path, boolean absolute) {
        if (!absolute) {
            return assets.isLoaded(fullPath() + path, Texture.class);
        }
        return assets.isLoaded(path, Texture.class);
    }

    public Texture get(String path) {
        return get(path, false);
    }

    public Texture get(String path, boolean absolute) {
        if (!absolute) {
            return assets.get(fullPath() + path, Texture.class);
        } else {
            return assets.get(path, Texture.class);
        }
    }

    public void dispose() {
        assets.clear();
    }

    public String fullPath(String path) {
        return Gdx.files.internal(fullPath() + path).path();
    }

    public StaticLayer loadImage(InputStream stream, String path) {
        return new StaticLayer(IOHelper.STREAM_PREFIX + getPath() + path, true);
    }

}
