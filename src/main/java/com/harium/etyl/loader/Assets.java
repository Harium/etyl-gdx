package com.harium.etyl.loader;

import com.badlogic.gdx.assets.AssetManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.harium.etyl.loader.async.AsyncResource;

public class Assets extends AssetManager {

    Map<String, List<AsyncResource>> resources = new HashMap<>();

    public void addResource(String path, AsyncResource resource) {
        List<AsyncResource> list = resources.get(path);

        if (list == null) {
            list = new ArrayList<>();
            resources.put(path, list);
        }

        list.add(resource);
    }

    public void checkResources() {
        for (Map.Entry<String,List<AsyncResource>> entry : resources.entrySet()) {
            String path = entry.getKey();
            // Check if asset is in the queue to be loaded
            if (isLoaded(path)) {
                for (AsyncResource resource : entry.getValue()) {
                    resource.onLoad(get(path, resource.resourceClass()));
                }
                //Stop checking loaded resources
                entry.getValue().clear();
            }
        }
    }



}
