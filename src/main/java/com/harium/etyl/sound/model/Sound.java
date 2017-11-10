package com.harium.etyl.sound.model;

import com.harium.etyl.loader.MultimediaLoader;
import com.harium.etyl.loader.async.AsyncResource;

public class Sound implements AsyncResource<com.badlogic.gdx.audio.Sound> {

    private com.badlogic.gdx.audio.Sound sound;

    public Sound(String path) {
        MultimediaLoader.getInstance().loadSoundAsync(path, this);
    }

    public void play() {
        if (sound == null) {
            return;
        }
        sound.play();
    }

    @Override
    public void onLoad(com.badlogic.gdx.audio.Sound sound) {
        this.sound = sound;
    }

    @Override
    public Class<com.badlogic.gdx.audio.Sound> resourceClass() {
        return com.badlogic.gdx.audio.Sound.class;
    }
}
