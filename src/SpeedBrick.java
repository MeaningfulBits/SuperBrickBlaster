import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;



/**
 * SpeedBricks also have an attribute speedFactor, the factor 
 * by which the speed of the ball increases when the brick is hit.
 * SpeedBricks have a different color than regular bricks.
 * @author Tyler Thomas
 *
 */
public class SpeedBrick extends Brick{
	private Brick brick;
	private float speedFactor = 1.2f;
	private AffineTransform translation = new AffineTransform();
	
	SpeedBrick(float x, float y, float height, float width) {
		brick = new Brick(x, y, height, width);
		translation.translate((double)brick.getX()-getWidth()/2, (double)brick.getY()-getHeight()/2);
		setTranslateAT(translation);
	}
	
	float getSpeedFactor() {
		return speedFactor;
	}
	
	@Override
	protected float getPointsValue(){
		return brick.getPointsValue()*(1.25f);
	}
	
	@Override
	float getX(){
		return brick.getX();
	}
	
	@Override
	float getY(){
		return brick.getY();
	}
	
	@Override
	float getWidth(){
		return brick.getWidth();
	}
	
	float getHeight(){
		return brick.getHeight();
	}
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform temp = g2d.getTransform();
		g2d.transform(getTranslateAT());
		if(isSelected()){
			g.setColor(getColor());
			g.fillRect(0, 0, (int)getWidth(), (int)getHeight());
		}
		else{
			g.setColor(Color.GREEN);
			g.fillRect(0, 0, (int)brick.getWidth(), (int)brick.getHeight());
			g.setColor(Color.BLACK);
			//g.drawString("S", 0, 0);
			g.drawRect(0, 0, (int)brick.getWidth(), (int)brick.getHeight());
		}
		g2d.setTransform(temp);
	}
}
