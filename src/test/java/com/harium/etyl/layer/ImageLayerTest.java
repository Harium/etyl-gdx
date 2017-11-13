package com.harium.etyl.layer;

import com.badlogic.gdx.graphics.Texture;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImageLayerTest {

    private static final float EPSILON = 0.01f;

    @Test
    public void testOnLoad() {
        Texture texture = mock(Texture.class);
        when(texture.getWidth()).thenReturn(200);
        when(texture.getHeight()).thenReturn(100);

        ImageLayer layer = new ImageLayer();
        layer.onLoad(texture);

        Assert.assertEquals(100, layer.getOriginX(), EPSILON);
        Assert.assertEquals(50, layer.getOriginY(), EPSILON);
        Assert.assertEquals(200, layer.srcW, EPSILON);
        Assert.assertEquals(100, layer.srcH, EPSILON);
    }

}
