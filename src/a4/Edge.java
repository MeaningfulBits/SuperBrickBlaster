package a4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * The walls that make up the playing field.
 * @author Tyler Thomas
 *
 */
class Edge extends GameObject implements IDrawable, ICollider{
	private float height;
	private float width;
	private float x,y;
	private AffineTransform translation = new AffineTransform();
	
	Edge(float x, float y, float width, float height){
		translation.translate((double)x-width/2, (double)y-height/2);
		setTranslateAT(translation);
		setHeight(height);
		setWidth(width);
		setX(x);
		setY(y);
	}
	
	private void setWidth(float j) {
		width = j;
	}

	private void setHeight(float i) {
		height = i;
	}

	@Override
	float getHeight() {
		return height;
	}

	@Override
	float getWidth() {
		return width;
	}

	@Override
	float getX() {
		return (x + width/2);
	}

	@Override
	float getY() {
		return (y + height/2);
	}
	
	@Override
	void setX(float i){
		if (i == 0){
			x = 0;
		}
		else{
			x = (i - width/2);	
		}
	}
	
	@Override
	void setY(float j){
		if (j == 0){
			y = 0;
		}
		else{
			y = (j - height/2);
		}
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform temp = g2d.getTransform();
		g2d.transform(getTranslateAT());
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, (int)width, (int)height);
		g2d.setTransform(temp);
	}

	//Edges are colliders but never check for collision and never handle a collision.
	@Override
	public boolean collidesWith(ICollider otherObject){
		return false;
	}

	@Override
	public void handleCollision(ICollider otherObject){}
}
