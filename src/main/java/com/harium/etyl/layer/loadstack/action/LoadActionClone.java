package com.harium.etyl.layer.loadstack.action;

import com.harium.etyl.layer.ImageLayer;
import com.harium.etyl.layer.StaticLayer;
import com.harium.etyl.layer.loadstack.LoadAction;
import com.harium.etyl.layer.loadstack.LoadActionType;

public class LoadActionClone extends LoadAction {

    private final ImageLayer source;

    public LoadActionClone(StaticLayer clone, ImageLayer source) {
        super(clone, LoadActionType.CLONE_LAYER);
        this.source = source;
    }

    @Override
    public void apply() {
        source.setLoaded(true);
        source.cloneLayer(layer);
        source.setSrcW(layer.getW());
        source.setSrcH(layer.getH());
        // Simulate load (useful if more actions are pendent)
        source.onLoad(source.getTexture());
    }
}
