package a4;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Body {
	private float myRadius;
	private Color myColor;
	private AffineTransform myTranslation;
	private AffineTransform myRotation;
	private AffineTransform myScale;

	Body(float radius, Color c){
		myColor = c;
		myRadius = radius;
		myTranslation = new AffineTransform();
		myRotation = new AffineTransform();
		myScale = new AffineTransform();
		myTranslation.translate((double)-radius, (double)-radius);
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

	public void draw(Graphics2D g2d){
		AffineTransform saveAT = g2d.getTransform();

		g2d.transform(myTranslation);
		g2d.transform(myRotation);
		g2d.transform(myScale);
		//Drawn in Local space (0, 0).
		g2d.setColor(myColor);
		g2d.fillOval(0, 0, (int)myRadius*2, (int)myRadius*2);
		g2d.setColor(Color.BLACK);
		g2d.drawOval(0, 0, (int)myRadius*2, (int)myRadius*2);
		//Restore the old graphics transform (remove this shape's transform)
		g2d.setTransform(saveAT);
	}
}
