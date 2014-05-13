import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
//import java.io.File;
import java.util.Random;

/**
 * Balls have an attribute diameter Balls are movable and do not 
 * allow their color to be changed.
 * @author Tyler Thomas
 *
 */
class Fireball extends GameObject implements IMovable, IDrawable, ICollider, IAudible{
	private float lastMoved = 0;
	private int numberOfBalls = 3;
	private Random randomGenerator = new Random();
	private float x, y;
	private float xSpeed = 1;
	private float ySpeed = 1;
	private final float DIAMETER = 15;
	private Body myBody = new Body(DIAMETER/2, getColor());
	private Flame [] flames = new Flame[4];
	private AffineTransform myTranslation;
	private AffineTransform myRotation;
	private AffineTransform myScale;
	//private String slash = File.separator;
	boolean isMuted = false;
	//private String soundDir = "." + slash + "sounds" + slash;
	//private Sound PaddleHit = new Sound(soundDir + "PaddleHit.wav");
	//private Sound WallHit = new Sound(soundDir + "BrickHit.wav");
	

	Fireball(float x, float y){
		myTranslation = new AffineTransform();
		myRotation = new AffineTransform();
		myScale = new AffineTransform();
		setX(x);
		setY(y);

		Flame f0 = new Flame(); f0.translate(0, 15); f0.rotate(0); f0.setColor(Color.RED);
		flames[0] = f0;
		Flame f1 = new Flame(); f1.translate(0, 15); f1.rotate(180); f1.setColor(Color.RED);
		flames[1] = f1;
		Flame f2 = new Flame(); f2.translate(0, 15); f2.rotate(-90); f2.setColor(Color.YELLOW);
		flames[2] = f2;
		Flame f3 = new Flame(); f3.translate(0, 15); f3.rotate(90); f3.setColor(Color.YELLOW);
		flames[3] = f3;
	}

	int getNumberOfBalls(){
		return numberOfBalls;
	}
	
	float getXSpeed() {
		return xSpeed;
	}

	float getYSpeed() {
		return ySpeed;
	}

	void setXSpeed(float f) {
		if(f < 10 && f > -10){
			xSpeed = f;	
		}
	}

	void setYSpeed(float f) {
		if(f < 10 && f > -10){
			ySpeed = f;
		}
	}

	@Override
	void setX(float f) {
		x = f - DIAMETER/2;
	}

	@Override
	void setY(float f) {
		y = f - DIAMETER/2;
	}

	@Override
	float getHeight() {
		return DIAMETER;
	}

	@Override
	float getWidth() {
		return DIAMETER;
	}

	@Override
	float getX() {
		return x + DIAMETER/2;
	}

	@Override
	float getY() {
		return y + DIAMETER/2;
	}

	public boolean collidesWith(ICollider otherObj) {
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

		return !(r1 < l2 || l1 > r2 || t1 < b2 || b1 > t2);
	}

	public void handleCollision(ICollider otherObj) {
		if((otherObj instanceof Brick)){
			setYSpeed(-(getYSpeed()));
			if((otherObj instanceof SpeedBrick)){
				setYSpeed(((SpeedBrick)otherObj).getSpeedFactor()*getYSpeed());
				setXSpeed(((SpeedBrick)otherObj).getSpeedFactor()*getXSpeed());
			}
		}
		if(otherObj instanceof Paddle){
			bounceVertical();

		}
		if((x-(DIAMETER/2)) < 5 || (x+(DIAMETER/2)) > 595){
			bounceHorizontal();
		}
		if((y+(DIAMETER/2)) > 495){
			bounceVertical();
		}
		if((y-(DIAMETER/2)) < 0){
			subtractABall();
			resetBall();
			System.out.println("You have " + getNumberOfBalls() + " left.\n");
		}
	}
	
	public void toggleSound() {
		isMuted = !isMuted;
	}
	
	public boolean isMuted(){
		return isMuted;
	}

	/**
	 * alterCourse(), which instructs the object to make a small random change in its movement direction.
	 */
	public void alterCourse() {
		setXSpeed(randomGenerator.nextInt(3)+xSpeed);
		setYSpeed(randomGenerator.nextInt(3)+ySpeed);
	}

	/**
	 * bounceHorizontal() instructs the object to reverse its x movement direction.
	 */
	public void bounceHorizontal() {
		setXSpeed(-getXSpeed());	
	}

	/**
	 * bounceVertical() instructs the object to reverse its y movement direction.
	 */
	public void bounceVertical() {
		setYSpeed(-getYSpeed());
	}

	/**
	 * move() causes the object to update its location based on its current speed and direction.
	 */
	public void move(float elapsedTime) {
		//Clear the Translation AffineTransform
		myTranslation.setToIdentity();
		
		//Calculate the distance and direction that the ball has moved in.
		float timeSinceLastMoved = elapsedTime -lastMoved;
		setX(getX()+xSpeed*timeSinceLastMoved);
		setY(getY()+ySpeed*timeSinceLastMoved);
		
		//Store the new position in the myTranlation AffineTransform
		myTranslation.translate((double)getX()-DIAMETER/2, (double)getY()-DIAMETER/2);
		setTranslateAT(myTranslation);
		
		//Rotate the Fireball by (45/2) degrees.
		myRotation.rotate(45/2);
		setRotateAT(myRotation);
		
		//save the new time.
		lastMoved = elapsedTime;
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

	@Override
	public void draw(Graphics g) {
		//convert the incoming Graphics to a Graphics2D
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform saveAT = g2d.getTransform();
		
		g2d.transform(getTranslateAT());
		g2d.transform(getRotateAT());
		myBody.draw(g2d);
		for(Flame f: flames){
			f.draw(g2d);
		}
		//Restore the old graphics transform
		g2d.setTransform(saveAT);
	}

	void subtractABall() {
		numberOfBalls--;
	}

	void resetBall() {
		setXSpeed(1);
		setYSpeed(1);
		setY(100);
		setX(randomGenerator.nextInt(590)+5);
	}

}
