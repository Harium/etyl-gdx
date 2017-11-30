package com.harium.etyl;

import com.badlogic.gdx.backends.jogamp.JoglAwtApplication;
import com.badlogic.gdx.backends.jogamp.JoglAwtApplicationConfiguration;
import com.harium.etyl.core.Core;
import com.harium.etyl.ui.GDXWindow;

public abstract class DesktopEngine {

    protected int w;
    protected int h;
    protected String icon;

    protected Core core;
    private JoglAwtApplicationConfiguration configuration;

    public DesktopEngine(int w, int h) {
        super();

        this.w = w;
        this.h = h;

        configuration = buildConfiguration();
        core = initCore();
        core.getSession().put(Etyl.WINDOW, new GDXWindow());
    }

    public void init() {
        configuration.width = w;
        configuration.height = h;

        new JoglAwtApplication(core, configuration);
    }

    protected Core initCore() {
        return new Core(w, h);
    }

    public Core getCore() {
        return core;
    }

    protected JoglAwtApplicationConfiguration buildConfiguration() {
        return new JoglAwtApplicationConfiguration();
    }

    public void setTitle(String title) {
        configuration.title = title;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void enableFullScreen() {
        configuration.fullscreen = true;
    }

    public void disableFullScreen() {
        configuration.fullscreen = false;
    }
}
