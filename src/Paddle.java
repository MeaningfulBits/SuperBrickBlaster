import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Paddles have attributes width and height, along with values 
 * minX and maxX representing the leftmost and rightmost allowable 
 * positions for the paddle. Paddles also have a mode attribute: 
 * they are either in normal mode or random mode. Paddles are 
 * positionable and have changeable color.
 * @author Tyler Thomas
 *
 */
public class Paddle extends GameObject implements IPositionable, IColorChangeable, IDrawable, ICollider{
	private AffineTransform translation = new AffineTransform();
	private float width, height;
	private float maxX, minX;
	private float x, y;
	private boolean mode = false;
	
	Paddle(float x, float y, float width, float height, float minX, float maxX) {
		translation.translate((double)x-width/2, (double)y-height/2);
		setTranslateAT(translation);
		setMaxX(maxX);
		setMinX(minX);
		setWidth(width);
		setHeight(height);
		setX(x);
		setY(y);
	}

	float getMaxX() {
		return maxX;
	}

	float getMinX() {
		return minX;
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
		return x + width/2;
	}

	@Override
	float getY() {
		return y + height/2;
	}

	boolean isMode() {
		return mode;
	}
	
	private void setHeight(float f) {
		height = f;
	}

	private void setWidth(float f) {
		width = f;
	}
	
	void setMode(boolean b) {
		mode = b;
	}
	
	void setMaxX(float f) {
		maxX = f;
	}
	
	void setMinX(float f) {
		minX = f;
	}
	
	@Override
	void setX(float f) {
		x = f - width/2;
	}

	@Override
	void setY(float f) {
		y = f - height/2;
	}
	
	@Override
	public void setColor(Color newColor) {
		super.setColor(newColor);	
	}
	
	@Override
	public void setLocation(float newX, float newY) {
		setX(newX);
		setY(newY);
		translation.setToIdentity();
		translation.translate((double)newX-width/2, (double)newY-height/2);
	}
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform temp = g2d.getTransform();
		g2d.transform(getTranslateAT());
		g2d.fillOval(0, 0, (int)width, (int)height);
		g2d.setColor(getColor());
		g2d.drawOval(0, 0, (int)width, (int)height);
		g2d.setTransform(temp);
	}

	@Override
	public boolean collidesWith(ICollider otherObject) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleCollision(ICollider otherObject) {
		// TODO Auto-generated method stub	
	}
}
