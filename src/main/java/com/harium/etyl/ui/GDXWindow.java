package com.harium.etyl.ui;

import com.badlogic.gdx.Gdx;
import com.harium.etyl.commons.ui.Window;

public class GDXWindow implements Window {

    public void close() {
        Gdx.app.exit();
    }

}
