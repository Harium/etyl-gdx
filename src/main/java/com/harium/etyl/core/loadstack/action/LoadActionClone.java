package com.harium.etyl.core.loadstack.action;

import com.harium.etyl.core.loadstack.LoadAction;
import com.harium.etyl.core.loadstack.LoadActionType;
import com.harium.etyl.layer.ImageLayer;
import com.harium.etyl.layer.StaticLayer;

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

        int originalSrcW = source.getSrcW();
        int originalSrcH = source.getSrcH();

        // It is probably not needed
        source.setSrcW(layer.getW());
        source.setSrcH(layer.getH());

        // Simulate load (useful if more actions are pendent)
        source.onLoad(source.getTexture());

        if (originalSrcW != source.getSrcW()) {
            source.setSrcW(originalSrcW);
        }
        if (originalSrcH != source.getSrcH()) {
            source.setSrcH(originalSrcH);
        }
    }
}
