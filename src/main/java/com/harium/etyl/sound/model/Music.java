package com.harium.etyl.sound.model;

import com.harium.etyl.loader.MultimediaLoader;
import com.harium.etyl.loader.async.AsyncResource;

public class Music implements AsyncResource<com.badlogic.gdx.audio.Music> {

    private boolean playOnLoad = false;
    private boolean loaded = false;
    private boolean loop = true;
    private com.badlogic.gdx.audio.Music music;

    public Music(String path) {
        MultimediaLoader.getInstance().loadMusicAsync(path, this);
    }

    public void play() {
        if (!loaded) {
            playOnLoad = true;
        }
        if (music == null || music.isPlaying()) {
            return;
        }
        music.play();
    }

    public boolean isPlaying() {
        if (music == null) {
            return false;
        }
        return music.isPlaying();
    }

    public void stop() {
        music.stop();
    }

    public void dispose() {
        music.dispose();
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
        if (loaded) {
            this.music.setLooping(loop);
        }
    }

    @Override
    public void onLoad(com.badlogic.gdx.audio.Music music) {
        this.music = music;
        this.loaded = true;
        this.music.setLooping(loop);

        if (playOnLoad) {
            play();
        }
    }

    @Override
    public Class<com.badlogic.gdx.audio.Music> resourceClass() {
        return com.badlogic.gdx.audio.Music.class;
    }
}
