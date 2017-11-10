package com.harium.etyl.gui;

import com.harium.etyl.commons.Drawable;
import com.harium.etyl.commons.event.MouseEvent;
import com.harium.etyl.commons.event.PointerEvent;
import com.harium.etyl.commons.graphics.Color;
import com.harium.etyl.commons.layer.GeometricLayer;
import com.harium.etyl.core.graphics.Graphics;

public class Button implements Drawable {

    private GeometricLayer layer;
    private String label;

    protected boolean clicked = false;
    protected boolean activated = false;

    public Button(int x, int y, int w, int h, String label) {
        super();
        this.layer = new GeometricLayer(x, y, w, h);

        this.label = label;
    }

    public void updateMouse(PointerEvent event) {
        if (event.isButtonDown(MouseEvent.MOUSE_BUTTON_LEFT) && !activated) {
            activated = true;
            if (layer.colideRectPoint(event.getX(), event.getY())) {
                press();
                event.consume();
            }
        } else if (event.isButtonUp(MouseEvent.MOUSE_BUTTON_LEFT)) {
            activated = false;
            release();
        }
    }

    protected void press() {
        clicked = true;
    }

    protected void release() {
        clicked = false;
    }

    @Override
    public void draw(Graphics g) {
        g.setAlpha(80);
        g.setColor(Color.BLACK);
        g.fillRect(layer);
        g.resetOpacity();
        g.setColor(Color.WHITE);
        g.drawString(label, layer);
    }

    public boolean isClicked() {
        return clicked;
    }

}
