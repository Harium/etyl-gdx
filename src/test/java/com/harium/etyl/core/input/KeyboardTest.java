package com.harium.etyl.core.input;

import com.badlogic.gdx.Input;
import com.harium.etyl.core.input.keyboard.Keyboard;
import org.junit.Assert;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class KeyboardTest {

    @Test
    public void testGetAWTKey(){
        Assert.assertEquals(KeyEvent.VK_A, Keyboard.getAwtKeyCode(Input.Keys.A));
        Assert.assertEquals(KeyEvent.VK_B, Keyboard.getAwtKeyCode(Input.Keys.B));
        Assert.assertEquals(KeyEvent.VK_C, Keyboard.getAwtKeyCode(Input.Keys.C));
        Assert.assertEquals(KeyEvent.VK_D, Keyboard.getAwtKeyCode(Input.Keys.D));
        Assert.assertEquals(KeyEvent.VK_E, Keyboard.getAwtKeyCode(Input.Keys.E));
        Assert.assertEquals(KeyEvent.VK_F, Keyboard.getAwtKeyCode(Input.Keys.F));
        Assert.assertEquals(KeyEvent.VK_G, Keyboard.getAwtKeyCode(Input.Keys.G));
        Assert.assertEquals(KeyEvent.VK_H, Keyboard.getAwtKeyCode(Input.Keys.H));
        Assert.assertEquals(KeyEvent.VK_I, Keyboard.getAwtKeyCode(Input.Keys.I));
        Assert.assertEquals(KeyEvent.VK_J, Keyboard.getAwtKeyCode(Input.Keys.J));
        Assert.assertEquals(KeyEvent.VK_K, Keyboard.getAwtKeyCode(Input.Keys.K));
        Assert.assertEquals(KeyEvent.VK_L, Keyboard.getAwtKeyCode(Input.Keys.L));
        Assert.assertEquals(KeyEvent.VK_M, Keyboard.getAwtKeyCode(Input.Keys.M));
        Assert.assertEquals(KeyEvent.VK_N, Keyboard.getAwtKeyCode(Input.Keys.N));
        Assert.assertEquals(KeyEvent.VK_O, Keyboard.getAwtKeyCode(Input.Keys.O));
        Assert.assertEquals(KeyEvent.VK_P, Keyboard.getAwtKeyCode(Input.Keys.P));
        Assert.assertEquals(KeyEvent.VK_Q, Keyboard.getAwtKeyCode(Input.Keys.Q));
        Assert.assertEquals(KeyEvent.VK_R, Keyboard.getAwtKeyCode(Input.Keys.R));
        Assert.assertEquals(KeyEvent.VK_S, Keyboard.getAwtKeyCode(Input.Keys.S));
        Assert.assertEquals(KeyEvent.VK_T, Keyboard.getAwtKeyCode(Input.Keys.T));
        Assert.assertEquals(KeyEvent.VK_U, Keyboard.getAwtKeyCode(Input.Keys.U));
        Assert.assertEquals(KeyEvent.VK_V, Keyboard.getAwtKeyCode(Input.Keys.V));
        Assert.assertEquals(KeyEvent.VK_W, Keyboard.getAwtKeyCode(Input.Keys.W));
        Assert.assertEquals(KeyEvent.VK_X, Keyboard.getAwtKeyCode(Input.Keys.X));
        Assert.assertEquals(KeyEvent.VK_Y, Keyboard.getAwtKeyCode(Input.Keys.Y));
        Assert.assertEquals(KeyEvent.VK_Z, Keyboard.getAwtKeyCode(Input.Keys.Z));
        Assert.assertEquals(KeyEvent.VK_NUMPAD0, Keyboard.getAwtKeyCode(Input.Keys.NUMPAD_0));
        Assert.assertEquals(KeyEvent.VK_NUMPAD1, Keyboard.getAwtKeyCode(Input.Keys.NUMPAD_1));
        Assert.assertEquals(KeyEvent.VK_NUMPAD2, Keyboard.getAwtKeyCode(Input.Keys.NUMPAD_2));
        Assert.assertEquals(KeyEvent.VK_NUMPAD3, Keyboard.getAwtKeyCode(Input.Keys.NUMPAD_3));
        Assert.assertEquals(KeyEvent.VK_NUMPAD4, Keyboard.getAwtKeyCode(Input.Keys.NUMPAD_4));
        Assert.assertEquals(KeyEvent.VK_NUMPAD5, Keyboard.getAwtKeyCode(Input.Keys.NUMPAD_5));
        Assert.assertEquals(KeyEvent.VK_NUMPAD6, Keyboard.getAwtKeyCode(Input.Keys.NUMPAD_6));
        Assert.assertEquals(KeyEvent.VK_NUMPAD7, Keyboard.getAwtKeyCode(Input.Keys.NUMPAD_7));
        Assert.assertEquals(KeyEvent.VK_NUMPAD8, Keyboard.getAwtKeyCode(Input.Keys.NUMPAD_8));
        Assert.assertEquals(KeyEvent.VK_NUMPAD9, Keyboard.getAwtKeyCode(Input.Keys.NUMPAD_9));
        Assert.assertEquals(KeyEvent.VK_0, Keyboard.getAwtKeyCode(Input.Keys.NUM_0));
        Assert.assertEquals(KeyEvent.VK_1, Keyboard.getAwtKeyCode(Input.Keys.NUM_1));
        Assert.assertEquals(KeyEvent.VK_2, Keyboard.getAwtKeyCode(Input.Keys.NUM_2));
        Assert.assertEquals(KeyEvent.VK_3, Keyboard.getAwtKeyCode(Input.Keys.NUM_3));
        Assert.assertEquals(KeyEvent.VK_4, Keyboard.getAwtKeyCode(Input.Keys.NUM_4));
        Assert.assertEquals(KeyEvent.VK_5, Keyboard.getAwtKeyCode(Input.Keys.NUM_5));
        Assert.assertEquals(KeyEvent.VK_6, Keyboard.getAwtKeyCode(Input.Keys.NUM_6));
        Assert.assertEquals(KeyEvent.VK_7, Keyboard.getAwtKeyCode(Input.Keys.NUM_7));
        Assert.assertEquals(KeyEvent.VK_8, Keyboard.getAwtKeyCode(Input.Keys.NUM_8));
        Assert.assertEquals(KeyEvent.VK_9, Keyboard.getAwtKeyCode(Input.Keys.NUM_9));
        Assert.assertEquals(KeyEvent.VK_AT, Keyboard.getAwtKeyCode(Input.Keys.AT));
    }

}
