package com.harium.etyl.layer;

import com.harium.etyl.commons.graphics.Color;
import com.harium.etyl.commons.layer.Layer;
import com.harium.etyl.core.graphics.Font;
import com.harium.etyl.core.graphics.Graphics;

public class TextLayer extends Layer {

    private String text;
    private float size = 16;
    private Font font = null;
    private String fontName = "";

    private com.harium.etyl.commons.graphics.Color color = com.harium.etyl.commons.graphics.Color.BLACK;

    private com.harium.etyl.commons.graphics.Color borderColor = Color.GRAY;

    private boolean border = false;

    private float borderWidth = 4f;
    private boolean antiAliased = true;
    private boolean fractionalMetrics = false;

    public TextLayer() {
        super();
    }

    public TextLayer(String text) {
        super(0, 0);
        this.text = text;
    }

    public TextLayer(int x, int y, String text) {
        super(x, y);
        this.text = text;
    }

    public void simpleDraw(Graphics g, int x, int y) {
        // Update Size
        g.setFont(font);

        if (!border) {
            g.setColor(color);
            g.drawString(text, x, y);
        } else {
            // Draw text with border
            g.setColor(color);
            g.drawString(text, x, y);
        }
    }

    @Override
    public void draw(Graphics g) {
        simpleDraw(g, x, y);
    }
}