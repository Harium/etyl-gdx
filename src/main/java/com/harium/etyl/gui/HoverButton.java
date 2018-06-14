package com.harium.etyl.gui;

import com.harium.etyl.commons.event.MouseEvent;
import com.harium.etyl.commons.event.PointerEvent;
import com.harium.etyl.commons.graphics.Color;
import com.harium.etyl.core.graphics.Graphics;

public class HoverButton extends Button {

    protected boolean hover = false;

    public HoverButton(int x, int y, int w, int h, String label) {
        super(x, y, w, h, label);
    }

    public void updateMouse(PointerEvent event) {
        if (layer.colideRectPoint(event.getX(), event.getY())) {
            hover = true;
        } else {
            hover = false;
        }

        if (event.isButtonDown(MouseEvent.MOUSE_BUTTON_LEFT) && !activated) {
            activated = true;
            if (hover) {
                press();
                event.consume();
            }
        } else if (event.isButtonUp(MouseEvent.MOUSE_BUTTON_LEFT)) {
            activated = false;
            release();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setAlpha(80);
        if (!hover) {
            g.setColor(Color.BLACK);
        } else {
            g.setColor(Color.GRAY);
        }
        g.fillRect(layer);
        g.resetOpacity();
        g.setColor(Color.WHITE);
        g.drawString(label, layer);
    }

}
