package com.harium.etyl.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import com.harium.etyl.loader.async.AsyncResource;

public class MultimediaLoader extends Loader {

    private static MultimediaLoader instance = null;

    private MultimediaLoader() {
        super();
        setFolder("sounds/");
    }

    public static MultimediaLoader getInstance() {
        if (instance == null) {
            instance = new MultimediaLoader();
        }
        return instance;
    }

    public Sound loadSound(String path) {
        return Gdx.audio.newSound(Gdx.files.internal((fullPath() + path)));
    }

    public void loadSoundAsync(String path, AsyncResource<Sound> resource) {
        assets.load(fullPath() + path, Sound.class);
        assets.addResource(fullPath() + path, resource);
    }

    public Music loadMusic(String path) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal((fullPath() + path)));
        music.setLooping(true);
        return music;
    }

    public void loadMusicAsync(String path, AsyncResource<Music> resource) {
        assets.load(fullPath() + path, Music.class);
        assets.addResource(fullPath() + path, resource);
    }
}
