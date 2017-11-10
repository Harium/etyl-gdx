package com.harium.etyl.linear;


/**
 * 
 * @author yuripourre
 * @license LGPLv3
 *
 */

public class PointInt2D {

	protected int x = 0;
	
	protected int y = 0;

	public PointInt2D() {
		super();
		setLocation(0, 0);
	}

	public PointInt2D(int x, int y) {
		super();
		setLocation(x, y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setLocation(int x, int y) {
		setX(x);
		setY(y);
	}

	public void setOffset(int x, int y) {
		setOffsetX(x);
		setOffsetY(y);
	}
	
	public void setOffsetX(int x) {
		this.x += x;
	}

	public void setOffsetY(int y) {
		this.y += y;
	}

	public static PointInt2D clone(PointInt2D point) {
		return new PointInt2D(point.getX(), point.getY());
	}
}
