package com.harium.etyl.loader.async;

import java.util.ArrayList;
import java.util.List;

import com.harium.etyl.loader.LoadListener;

public abstract class AsyncResourceNotifier<T> implements AsyncResource<T> {

    private List<LoadListener> listeners;

    protected void notifyListeners() {
        if (listeners != null && listeners.isEmpty()) {
            for (LoadListener listener : listeners) {
                listener.onLoad();
            }
        }
    }

    public void addLoadListener(LoadListener loadListener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(loadListener);
    }
}
