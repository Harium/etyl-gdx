package com.harium.etyl.loader.async;

public interface AsyncResource<T> {
    void onLoad(T t);
    Class<T> resourceClass();
}
