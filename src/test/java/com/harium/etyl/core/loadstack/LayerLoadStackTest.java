package com.harium.etyl.core.loadstack;


import com.harium.etyl.layer.ImageLayer;
import junit.framework.Assert;

import org.junit.Test;

public class LayerLoadStackTest {

    @Test
    public void testAddAction() {
        ImageLayer layer = new ImageLayer(0, 0);

        //Clear the stack
        LayerLoadStack.dispose();

        //Add the action to a list
        LayerLoadStack.centralizeX(layer, 0, 100);
        Assert.assertEquals(1, LayerLoadStack.stackSize());

        //Add the action to the same list
        LayerLoadStack.centralizeY(layer, 100, 200);
        Assert.assertEquals(1, LayerLoadStack.stackSize());

        layer.setLoaded(true);
        LayerLoadStack.onLoad(layer);

        Assert.assertEquals(50, layer.getX());
        Assert.assertEquals(150, layer.getY());
    }

}
