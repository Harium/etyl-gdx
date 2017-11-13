package com.harium.etyl.core.loadstack.action;

import com.harium.etyl.layer.ImageLayer;
import com.harium.etyl.core.loadstack.LoadAction;
import com.harium.etyl.core.loadstack.LoadActionType;

public class LoadActionCentralizeY extends LoadAction {

    protected int start;
    protected int end;

    public LoadActionCentralizeY(ImageLayer layer, int start, int end) {
        super(layer, LoadActionType.CENTRALIZE_Y);
        this.start = start;
        this.end = end;
    }

    @Override
    public void apply() {
        layer.centralizeY(start, end);
    }
}
