package com.harium.etyl.linear;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class Point2DTest {

    private Point2D point;

    @Before
    public void setUp() {
        point = new Point2D(900, 10);
    }

    @Test
    public void testCreatePoint() {
        Assert.assertEquals(900.0, point.getX());
        Assert.assertEquals(10.0, point.getY());
    }

}