package com.harium.etyl.layer.loadstack;

import com.harium.etyl.layer.StaticLayer;

/**
 * Created by death on 6/23/17.
 */

public abstract class LoadAction {
    protected StaticLayer layer;
    protected LoadActionType type;

    public LoadAction(StaticLayer layer, LoadActionType type) {
        this.layer = layer;
        this.type = type;
    }

    public abstract void apply();
}
