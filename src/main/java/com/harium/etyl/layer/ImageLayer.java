package com.harium.etyl.layer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.harium.etyl.core.graphics.Graphics;
import com.harium.etyl.core.loadstack.LayerLoadStack;

public class ImageLayer extends StaticLayer {

    protected int srcX; // Don't set values here
    protected int srcY; // Don't set values here
    protected int srcW; // Don't set values here
    protected int srcH; // Don't set values here

    public ImageLayer() {
        super();
    }

    public ImageLayer(int x, int y, String path) {
        super(path);
        setLocation(x, y);
    }

    public ImageLayer(String path) {
        super(path);
    }

    public ImageLayer(String path, boolean absolute) {
        super(path, absolute);
    }

    public ImageLayer(int x, int y) {
        super(x, y);
    }

    public ImageLayer(int x, int y, int w, int h) {
        super(x, y, w, h);
        this.srcW = w;
        this.srcH = h;
        setOriginCenter();
    }

    public ImageLayer(int x, int y, int w, int h, String path) {
        super(path);
        setBounds(x, y, w, h);
        this.srcW = w;
        this.srcH = h;
        setOriginCenter();
    }

    /**
     * @return srcX
     */
    public int getSrcX() {
        return srcX;
    }

    /**
     * @param srcX
     */
    public void setSrcX(int srcX) {
        this.srcX = srcX;
    }

    /**
     * @return srcY
     */
    public int getSrcY() {
        return srcY;
    }

    /**
     * @param srcY
     */
    public void setSrcY(int srcY) {
        this.srcY = srcY;
    }

    public int getSrcW() {
        return srcW;
    }

    public void setSrcW(int srcW) {
        this.srcW = srcW;
    }

    public int getSrcH() {
        return srcH;
    }

    public void setSrcH(int srcH) {
        this.srcH = srcH;
    }

    /**
     * @param srcX
     * @param srcY
     */
    public void setLocationSrc(int srcX, int srcY) {
        this.srcX = srcX;
        this.srcY = srcY;
    }

    /**
     * @param srcX
     * @param srcY
     */
    public void setSrc(int srcX, int srcY, int srcW, int srcH) {
        this.srcX = srcX;
        this.srcY = srcY;
        this.srcW = srcW;
        this.srcH = srcH;
    }

    public int getImageWidth() {
        return w;
    }

    public int getImageHeight() {
        return h;
    }

    // Src methods
    @Override
    public int getW() {
        return getSrcW();
    }

    @Override
    public int getH() {
        return getSrcH();
    }

    @Override
    public void setW(int w) {
        this.srcW = w;
    }

    @Override
    public void setH(int h) {
        this.srcH = h;
    }

    public Texture getTexture() {
        return texture;
    }

    @Override
    public void draw(Graphics g) {
        draw(g, 0, 0);
    }

    public void draw(Graphics g, int ox, int oy) {
        if (!visible || !loaded || opacity == 0)
            return;

        beginOpacity(g);
        SpriteBatch batch = g.getBatch();

        int realX = x + ox;
        int realY = g.getHeight() - y - getH() - oy;

        if (angle != 0 || scaleX != 1 || scaleY != 1) {
            float realOriginY = getSrcH() - originY;
            batch.draw(texture, realX, realY, originX, realOriginY, getW(), getH(), (float) scaleX,
                    (float) scaleY, (float) (360 - angle), srcX, srcY, getW(), getH(), false, false);
        } else {
            batch.draw(texture, realX, realY, srcX, srcY, getW(), getH());
        }
        endOpacity(g);
        // Is it needed?
        /*if (opacity != 0) {
            batch.setColor(1, 1, 1, 1);
        }*/
    }

    public void simpleDraw(Graphics g) {
        simpleDraw(g, x, y);
    }

    public void simpleDraw(Graphics g, int x, int y) {
        simpleDraw(g, x, y, getW(), getH());
    }

    public void simpleDraw(Graphics g, int x, int y, int w, int h) {
        if (!visible || !loaded || opacity == 0)
            return;

        int realX = x;
        int realY = g.getHeight() - y - getH();

        beginOpacity(g);
        SpriteBatch batch = g.getBatch();
        batch.draw(texture, realX, realY, srcX, srcY, w, h);
        endOpacity(g);
    }

    private void beginOpacity(Graphics g) {
        if (opacity == MAX_OPACITY) {
            return;
        }
        g.setOpacity(opacity);
    }

    private void endOpacity(Graphics g) {
        if (opacity == MAX_OPACITY) {
            return;
        }
        g.resetOpacity();
    }

    public int centralizeX(int startX, int endX) {
        if (loaded) {
            return super.centralizeX(startX, endX);
        } else {
            LayerLoadStack.centralizeX(this, startX, endX);
            return startX + endX / 2;
        }
    }

    public int centralizeY(int startY, int endY) {
        if (loaded) {
            return super.centralizeY(startY, endY);
        } else {
            LayerLoadStack.centralizeY(this, startY, endY);
            return startY + endY / 2;
        }
    }

    /**
     * @param layer
     */
    public void cloneLayer(StaticLayer layer) {
        if (layer.loaded) {
            super.cloneLayer(layer);
            onLoad(layer.texture);
        } else {
            LayerLoadStack.cloneLayer(this, layer);
        }
    }

    public void setOrigin(float originX, float originY) {
        this.originX = originX;
        this.originY = originY;
    }

    @Override
    public void onLoad(Texture texture) {
        this.texture = texture;
        this.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.w = texture.getWidth();
        this.h = texture.getHeight();
        if (srcW == 0) {
            this.srcW = w;
        }
        if (srcH == 0) {
            this.srcH = h;
        }
        setOriginCenter();
        this.loaded = true;

        notifyListeners();
    }

}