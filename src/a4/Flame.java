package a4;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

/**
 * Flame object that surrounds the ball.
 * @author Tyler Thomas
 *
 */
class Flame {
	private Point top, bottomLeft, bottomRight;
	private Color myColor;
	private AffineTransform myTranslation, myRotation, myScale;

	Flame (){
		//define a default flame with base 20, height 40 and origin in the center.
		top = new Point(0,10);
		bottomLeft = new Point(-5, -10);
		bottomRight = new Point(5, -10);

		//Initialize the transformations applied to the flame
		myTranslation = new AffineTransform();
		myRotation = new AffineTransform();
		myScale = new AffineTransform();
	}

	void rotate (double degrees){
		myRotation.rotate(Math.toRadians(degrees));
	}

	void scale (double sx, double sy){
		myScale.scale(sx, sy);
	}

	void translate (double dx, double dy){
		myTranslation.translate(dx, dy);
	}

	public void draw (Graphics2D g2d){
		AffineTransform saveAT = g2d.getTransform();

		g2d.transform(myRotation);
		g2d.transform(myScale);
		g2d.transform(myTranslation);

		g2d.setColor(myColor);
		g2d.drawLine(top.x, top.y, bottomLeft.x, bottomLeft.y);
		g2d.drawLine(bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y);
		g2d.drawLine(bottomRight.x, bottomRight.y, top.x, top.y);
		//Restore the old graphics transform (remove this shape's transform)
		g2d.setTransform(saveAT);
	}

	public void setColor(Color c) {
		myColor = c;
	}
}
