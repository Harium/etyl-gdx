package com.harium.etyl;


import com.harium.etyl.commons.context.Application;
import com.harium.etyl.core.Core;
import com.harium.etyl.core.Engine;
import com.harium.etyl.loader.FontLoader;
import com.harium.etyl.loader.MultimediaLoader;
import com.harium.etyl.loader.image.ImageLoader;
import com.harium.etyl.ui.GDXWindow;
import com.harium.etyl.util.PathHelper;

public abstract class Etyl extends DesktopEngine<Core> implements Engine<Application> {

    public static final String WINDOW = "window";
    private Application application;
    protected Core core;

    public Etyl(int w, int h) {
        super(w, h);
    }

    public void init() {
        core = initCore();

        initialSetup("");
        core.getSession().put(Etyl.WINDOW, new GDXWindow());

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
