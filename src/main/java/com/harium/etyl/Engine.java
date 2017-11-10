package com.harium.etyl;

import com.harium.etyl.commons.context.Context;

public interface Engine<T extends Context> {
    T startApplication();
}
