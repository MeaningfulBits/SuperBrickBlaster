import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.util.Random;

/**
 * Generic game object everything in the game is an GameObject.
 * @author Tyler Thomas
 *
 */
abstract class GameObject implements IDrawable{
	private Random randomGenerator = new Random();
	private AffineTransform scaleAT = new AffineTransform();
	private AffineTransform rotateAT = new AffineTransform();
	private AffineTransform translateAT = new AffineTransform();
	private Color color = new Color(randomGenerator.nextInt(256), randomGenerator.nextInt(256), 
			randomGenerator.nextInt(256));
	
	GameObject(){
		setX(0);
		setY(0);
	}
	
	abstract float getWidth();
	abstract float getHeight();
	
	abstract float getX();
	abstract float getY();
	
	abstract void setX(float x);
	abstract void setY(float y);
	
	Color getColor(){
		return color;
	}
	void setColor(Color color) {
		this.color = color;
	}
	
	void setTranslateAT(AffineTransform AT) {
		translateAT = AT;
	}
	AffineTransform getTranslateAT() {
		return translateAT;
	}
	void setRotateAT(AffineTransform AT) {
		rotateAT = AT;
	}
	AffineTransform getRotateAT() {
		return rotateAT;
	}
	void setScaleAT(AffineTransform AT) {
		scaleAT = AT;
	}
	AffineTransform getScaleAT() {
		return scaleAT;
	}
	
	public abstract void draw(Graphics g);
}

interface Collection{
	public void add(GameObject gObj);
	public void remove(GameObject gObj);
	public Iterator getIterator();
}

interface Iterator{
	public boolean hasNext();
	public GameObject getNext();
}