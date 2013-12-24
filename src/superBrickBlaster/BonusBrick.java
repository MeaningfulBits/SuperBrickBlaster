package superBrickBlaster;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * TODO
 * Brick that gives the paddle powers. (Unfinished)
 * @author Tyler Thomas
 *
 */
class BonusBrick extends Brick{
	BonusBrick(Brick B){
		setX(B.getX());
		setY(B.getY());
	}
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform temp = g2d.getTransform();
		g2d.transform(getTranslateAT());
		if(isSelected()){
			g2d.setColor(getColor());
			g2d.fillRect(0, 0, (int)getWidth(), (int)getHeight());
		}
		else{
			g2d.setColor(Color.MAGENTA);
			g2d.fillRect(0, 0, (int)getWidth(), (int)getHeight());
			g2d.setColor(Color.BLACK);
			g2d.drawRect(0, 0, (int)getWidth(), (int)getHeight());				
		}
		g2d.setTransform(temp);
	}
}
