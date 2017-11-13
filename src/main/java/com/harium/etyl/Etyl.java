package com.harium.etyl;


import com.harium.etyl.commons.context.Application;
import com.harium.etyl.core.Engine;
import com.harium.etyl.loader.FontLoader;
import com.harium.etyl.loader.MultimediaLoader;
import com.harium.etyl.loader.image.ImageLoader;
import com.harium.etyl.util.PathHelper;

public abstract class Etyl extends DesktopEngine implements Engine<Application> {

    private Application application;

    public Etyl(int w, int h) {
        super(w, h);
    }

    public void init() {
        initialSetup("");

        application = startApplication();
        core.setApplication(application);

        super.init();
    }

    protected void initialSetup(String suffix) {
        String path = PathHelper.currentDirectory() + "assets/" + suffix;

        ImageLoader.getInstance().setPath(path);
        FontLoader.getInstance().setPath(path);
        MultimediaLoader.getInstance().setPath(path);
    }

}
