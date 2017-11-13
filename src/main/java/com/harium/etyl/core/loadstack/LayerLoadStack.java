package com.harium.etyl.core.loadstack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.harium.etyl.layer.ImageLayer;
import com.harium.etyl.layer.StaticLayer;
import com.harium.etyl.core.loadstack.action.LoadActionCentralizeX;
import com.harium.etyl.core.loadstack.action.LoadActionCentralizeY;
import com.harium.etyl.core.loadstack.action.LoadActionClone;

public class LayerLoadStack {

    private static Map<StaticLayer, List<LoadAction>> loadStack = new HashMap<>();

    public static void cloneLayer(ImageLayer layer, StaticLayer clone) {
        // Inverted -> clone, layer
        addAction(clone, new LoadActionClone(clone, layer));
    }

    public static void centralizeX(ImageLayer layer, int x, int w) {
        addAction(layer, new LoadActionCentralizeX(layer, x, w));
    }

    public static void centralizeY(ImageLayer layer, int y, int h) {
        addAction(layer, new LoadActionCentralizeY(layer, y, h));
    }

    private static void addAction(StaticLayer layer, LoadAction loadAction) {
        List<LoadAction> actions = loadStack.get(layer);
        if (actions == null) {
            actions = new ArrayList<>();
            loadStack.put(layer, actions);
        }

        actions.add(loadAction);
    }

    public static void onLoad(StaticLayer layer) {
        if (!loadStack.containsKey(layer)) {
            return;
        }

        List<LoadAction> actions = loadStack.get(layer);
        for (LoadAction action : actions) {
            action.apply();
        }
        loadStack.remove(layer);
    }

    public static void dispose() {
        loadStack.clear();
    }

    public static int stackSize() {
        return loadStack.size();
    }

}
