package com.harium.etyl.core.loadstack.action;

import com.harium.etyl.layer.ImageLayer;
import com.harium.etyl.core.loadstack.LoadAction;
import com.harium.etyl.core.loadstack.LoadActionType;

public class LoadActionCentralizeX extends LoadAction {

    protected int start;
    protected int end;

    public LoadActionCentralizeX(ImageLayer layer, int start, int end) {
        super(layer, LoadActionType.CENTRALIZE_X);
        this.start = start;
        this.end = end;
    }

    @Override
    public void apply() {
        layer.centralizeX(start, end);
    }
}
