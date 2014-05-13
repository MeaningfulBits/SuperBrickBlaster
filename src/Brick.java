package superBrickBlaster;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

/**
 * Bricks have attributes width and height, along with pointValue.
 * Bricks can have there color changed but not their location.
 * @author Tyler Thomas
 */
class Brick extends GameObject implements IColorChangeable, IDrawable, ISelectable, ICollider{
	private boolean annihilated = false;
	private boolean selected = false;
	private float pointsValue = 10;
	private AffineTransform translation = new AffineTransform();
	private float width, height;
	private float x, y;
	
	Brick(){}
	
	Brick(float columnStart, float rowStart, float height, float width) {
		translation.translate((double)columnStart-width/2, (double)rowStart-height/2);
		setTranslateAT(translation);
		setWidth(width);
		setHeight(height);
		setX(columnStart);
		setY(rowStart);
	}

	@Override
	float getX() {
		return x + width/2;
	}

	@Override
	float getY() {
		return y + height/2;
	}
	
	protected float getPointsValue() {
		return pointsValue;
	}
	
	float getWidth() {
		return width;
	}
	
	float getHeight() {
		return height;
	}

	boolean isAnnihilated() {
		return annihilated;
	}

	private void setWidth(float f) {
		width = f;
	}

	private void setHeight(float f) {
		height = f;
	}
	
	@Override
	void setX(float f){
		x = f - width/2;
	}
	
	@Override
	void setY(float f){
		y = f - height/2;
	}
	
	@Override
	public void setColor(Color newColor) {
		super.setColor(newColor);
	}

	@Override
	public void setSelected(boolean b) {
		selected = b;		
	}
	
	@Override
	public boolean contains(Point p) {
		boolean temp = false;
		int r = (int)(getX() + width/2);
		int l = (int)(getX() - width/2);
		int b = (int)(getY() + height/2);
		int t = (int)(getY() - height/2);
		if(p.x < r && p.x > l && p.y > b && p.y < t){
			temp = true;	
	    }
		return temp;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform temp = g2d.getTransform();
		g2d.transform(getTranslateAT());
		if(isSelected()){
			g2d.setColor(Color.BLUE);
			g2d.fillRect(0, 0, (int)width, (int)height);
		}
		else{
			g2d.setColor(Color.RED);
			g2d.fillRect(0, 0, (int)width, (int)height);
			g2d.setColor(Color.BLACK);
			g2d.drawRect(0, 0, (int)width, (int)height);				
		}
		g2d.setTransform(temp);
	}

	@Override
	public boolean collidesWith(ICollider otherObj) {
		boolean temp = false;
	    if(otherObj instanceof Fireball){
	    	GameObject gObj = (GameObject) otherObj;
		    //This collider.
		    int r1 = (int) (getX() + (getWidth()/2));
		    int l1 = (int) (getX() - (getWidth()/2));
		    int t1 = (int) (getY() + (getHeight()/2));
		    int b1 = (int) (getY() - (getHeight()/2));

		    //The other collider.
		    int r2 = (int) (gObj.getX() + gObj.getWidth()/2);
		    int l2 = (int) (gObj.getX() - gObj.getWidth()/2);
		    int t2 = (int) (gObj.getY() + gObj.getHeight()/2);
		    int b2 = (int) (gObj.getY() - gObj.getHeight()/2);

		    temp = !(r1 < l2 || l1 > r2 || t1 < b2 || b1 > t2);	
	    }
	    return temp;
	}

	@Override
	public void handleCollision(ICollider otherObject) {
		System.out.println("A brick was hit!!");
		annihilated = true;
	}
}
