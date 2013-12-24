package superBrickBlaster;

//import java.io.File;
import java.util.Observable;
import java.util.Random;
import javax.swing.JOptionPane;

/** 
 * Interface Code to instantiate, holds, and manipulates the collection of GameObjects 
 * (GameObjectCollection) and other related game state data.
 * @author Tyler Thomas
 */

interface IGameWorld {
	void moveRight();
	void moveLeft();
	void increaseSpeed(float percentage);
	void annihilate();
	void changePaddleMode();
	void nextBall();
	void tickClock();
	void toggleSound();
	void quit();
	String display();
	GameObjectCollection returnList();
}

/**
 * Proxy GameWorld that is sent with disable methods. Every method that can change something in the GameWorld is 
 * disabled. This method implements the IGameWorld interface.
 * @author Tyler Thomas
 *
 */
class GameWorldProxy extends Observable implements IGameWorld {
	private GameWorld realGameWorld;

	GameWorldProxy (GameWorld gw) {
		realGameWorld = gw;
	}
	public void moveRight() {}
	public void moveLeft() {}
	public void increaseSpeed(float percentage) {}
	public void annihilate() {}
	public void changePaddleMode() {}
	public void nextBall() {}
	public void tickClock() {}
	public void toggleSound() {}
	public void quit() {}
	public String display() {
		return realGameWorld.display();
	}
	public GameObjectCollection returnList() {
		return realGameWorld.returnList();
	}
}

/**
 * Code to instantiate, holds, and manipulates the collection of GameObjects (GameObjectCollection) and other related 
 * game state data. Implements the IGameWorld interface.
 * @author Tyler Thomas
 *
 */
class GameWorld extends Observable implements IGameWorld, IAudible {
	private final int ROWS = 3; //total number of rows
	private final int COLUMN = 12; // total number of columns
	private final int WORLDWIDTH = 595;
	private final int WORLDHEIGHT = 495;
	private final int BRICKHEIGHT = 33;
	private final int BRICKWIDTH = (WORLDWIDTH/COLUMN);
	private final int PADDLEWIDTH = 100;
	private final int PADDLEHEIGHT = (int)16.5;
	//instantiate each object.
	private boolean isMuted = false; //Sound is either on or off; On by default.
	private int gameClock = 0;
	private int score = 0;
	private boolean gamePaused;
	private Random randomGenerator = new Random();
	private GameObjectCollection listOfGameObjects = new GameObjectCollection(); //Collection of Game Object
	private Paddle gamePaddle = new Paddle(WORLDWIDTH/2f, 15f, (float)(PADDLEWIDTH), (float)(PADDLEHEIGHT), 5, 590);
	private Fireball gameBall = new Fireball(randomGenerator.nextInt(590)+5, 100);
	//595x495
	private Edge bottomEdge = new Edge((WORLDWIDTH/2), 0, WORLDWIDTH, 0);
	private Edge topEdge = new Edge((WORLDWIDTH/2), (int)(WORLDHEIGHT-(2.5)), WORLDWIDTH, 5);
	private Edge leftEdge = new Edge((int)(2.5f), (WORLDHEIGHT/2), 5, WORLDHEIGHT);
	private Edge rightEdge = new Edge((int)(WORLDWIDTH+(2.5f)), (WORLDHEIGHT/2), 5, WORLDHEIGHT);
	//private String slash = File.separator;
	//private String soundDir = "." + slash + "sounds" + slash;
	//private Sound MusicLoop = new Sound(soundDir + "MusicLoop.wav");
	//private Sound gameOverSound = new Sound(soundDir + "GameOver.wav");

	//GameWorld constructor which holds each object in an ArrayList
	GameWorld() {
		listOfGameObjects.add(gamePaddle);
		listOfGameObjects.add(gameBall);
		listOfGameObjects.add(topEdge);
		listOfGameObjects.add(bottomEdge);
		listOfGameObjects.add(leftEdge);
		listOfGameObjects.add(rightEdge);
		//construct the bricks
		int rows = ROWS;
		float rowStart = 450+(BRICKHEIGHT/2);
		for (int i= 0; i < rows; i++){
			int column = COLUMN;
			float columnStart = 5.5f + (BRICKWIDTH/2);
			for (int j = 0; j < column; j++){
				if(randomGenerator.nextBoolean()){
					listOfGameObjects.add(new Brick(columnStart, rowStart, BRICKHEIGHT, BRICKWIDTH));
				}
				else{
					listOfGameObjects.add(new SpeedBrick(columnStart, rowStart, BRICKHEIGHT, BRICKWIDTH));
				}
				columnStart = (int)(columnStart+BRICKWIDTH);
			}
			rowStart = rowStart-(BRICKHEIGHT);
		}
	}

	/**
	 * Move (change the position of) the paddle to the right by one unit.
	 */
	public void moveRight() {
		if (!gamePaused){
			Iterator theElements = listOfGameObjects.getIterator();
			while (theElements.hasNext()){
				GameObject gObj = (GameObject) theElements.getNext();
				if (gObj instanceof Paddle){
					Paddle pObj = (Paddle) gObj;
					if(pObj.getX()+(PADDLEWIDTH/2)+20 <= pObj.getMaxX()){
						pObj.setLocation(pObj.getX()+20, pObj.getY());
					}
					else{
						pObj.setLocation(pObj.getMaxX()-(PADDLEWIDTH/2), ((GameObject) pObj).getY());
					}
				}
			}
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * Move (change the position of) the paddle to the left by one unit.
	 */
	public void moveLeft() {
		if (!gamePaused){
			Iterator theElements = listOfGameObjects.getIterator();
			while (theElements.hasNext()){
				GameObject gObj = (GameObject) theElements.getNext();
				if (gObj instanceof Paddle){
					Paddle pObj = (Paddle) gObj;
					if(pObj.getX()-(PADDLEWIDTH/2)-20 >= pObj.getMinX()){
						pObj.setLocation(pObj.getX()-20, pObj.getY());
					}
					else{
						pObj.setLocation(pObj.getMinX()+(PADDLEWIDTH/2), ((GameObject) pObj).getY());
					}
				}
			}
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * Increase the speed of the ball by a factor of some percentage.
	 */
	public void increaseSpeed(float speedPercentage) {
		if (!gamePaused){
			Iterator theElements = listOfGameObjects.getIterator();
			while (theElements.hasNext()){
				GameObject gObj = (GameObject) theElements.getNext();
				if ( gObj instanceof Fireball){
					Fireball ballObj = (Fireball) gObj;
					ballObj.setXSpeed(ballObj.getXSpeed()+(speedPercentage)*ballObj.getXSpeed());
					ballObj.setYSpeed(ballObj.getYSpeed()+(speedPercentage)*ballObj.getYSpeed());
				}
			}
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * Annihilate (remove) a brick from the wall. The brick to be removed is chosen randomly from among the 
	 * remaining brick in the wall.
	 */
	public void annihilate() {
		if (!gamePaused){
			int bricksLeft = 0;
			Iterator theElements0 = listOfGameObjects.getIterator(); //This While loop gets the number of bricks left.
			while (theElements0.hasNext()){
				GameObject gObj0 = (GameObject) theElements0.getNext();
				if (gObj0 instanceof Brick){
					bricksLeft++;
				}
			}
			if(bricksLeft == 0){ //If there are zero bricks lift then the user is told.
				System.out.println("There are no more bricks to annihilate.");
			}
			else{ //If there are more then zero bricks left one is picked randomly to be removed.
				Iterator theElements1 = listOfGameObjects.getIterator();
				while (theElements1.hasNext()){
					GameObject gObj1 = (GameObject) theElements1.getNext();
					if (gObj1 instanceof Brick && ((Brick)gObj1).isAnnihilated()){
						score += ((Brick)gObj1).getPointsValue();
						listOfGameObjects.remove(gObj1);
						bricksLeft--;
						System.out.println("A brick was annihilated.");
					}
				}
			}
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * If the paddle is in normal mode then it switches to random mode, and vice versa.
	 */
	public void changePaddleMode() {
		if (!gamePaused){
			Iterator theElements = listOfGameObjects.getIterator();
			while (theElements.hasNext()){
				GameObject gObj = (GameObject) theElements.getNext();
				if ( gObj instanceof Paddle){
					Paddle pObj = (Paddle) gObj;
					if((pObj.isMode())){
						pObj.setMode(false);
					}
					else{
						pObj.setMode(true);
					}		
				}
			}
			setChanged();
			notifyObservers();
		}
	}


	/**
	 * If there are balls remaining then a new ball is put into play; otherwise, the game is over.
	 * Pretend that the previous ball has hit the bottom edge and been lost. 
	 */
	public void nextBall() {
		if (!gamePaused){
			if (gameBall.getNumberOfBalls() == 0){
				togglePause();
				JOptionPane.showMessageDialog(null, "You have no more balls left Game Over!\n Score: " 
						+ score);
			}
			else{
				gameBall.subtractABall();
				gameBall.resetBall();
				System.out.println("You have " + gameBall.getNumberOfBalls() + " left.\n");
				setChanged();
				notifyObservers();
			}
		}
	}

	/**
	 * Tell the GameWorld that the "game clock" has ticked. A clock tick in the GameWorld has the 
	 * following effects: (1) all movable objects are told to update their positions according to there 
	 * current heading and speed, and (2) the "elapsed game time" is incremented by one. 
	 */
	public void tickClock() {
		if (!gamePaused){
			gameClock++;
			Iterator theElements0 = listOfGameObjects.getIterator();
			while (theElements0.hasNext()){
				GameObject gObj = (GameObject) theElements0.getNext();
				if ( gObj instanceof IMovable){
					IMovable mObj = (IMovable)gObj;
					mObj.move(gameClock);
				}
			}
			Iterator theElements1 = listOfGameObjects.getIterator();
			while (theElements1.hasNext()){
				GameObject gObj0 = theElements1.getNext();//get a collidable object.
				if (gObj0 instanceof Fireball && ((Fireball)gObj0).getNumberOfBalls() == 0){
					togglePause();
					JOptionPane.showMessageDialog(null, "You have no more balls left Game Over!\n Score: " 
							+ score);
				}
				else{
					if(gObj0 instanceof ICollider){
						ICollider curObj = (ICollider) gObj0;
						//check if this object collides with any OTHER object.
						Iterator theElements2 = listOfGameObjects.getIterator();
						while(theElements2.hasNext()){
							GameObject gObj1 = theElements2.getNext();
							if(gObj1 != curObj && gObj1 instanceof ICollider) {
								ICollider otherObj = (ICollider) gObj1;
								if (curObj.collidesWith(otherObj)){
									curObj.handleCollision(otherObj);
								}
							}
						}	
					}
				}
			}
			annihilate();
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * Print a display (output lines of text) giving the current game state values, including: (1) the 
	 * current game clock value, (2) the current score, and (3) the number of balls left in the game. Output 
	 * should be well labeled in easy-to-read format.
	 */
	public String display() {
		String soundState;
		String gameClockOutput = ("Game Clock: " + ((gameClock/50)+1) + " ");//gameClock
		String scoreOutput = ("Score: " + score +" ");//score
		String ballsOutput = ("Balls: " + gameBall.getNumberOfBalls() + " ");//number of balls left
		if (!isMuted()) {
			soundState = ("Sound: On ");
		}
		else{
			soundState = ("Sound: Off ");
		}
		return (gameClockOutput + scoreOutput + ballsOutput + soundState);
	}

	/**
	 * Toggles the game's sound.
	 */
	public void toggleSound() {
		if (!isPaused()){
			isMuted = !isMuted;
			//Goes through the list of game objects and toggles the sound for audible objects
			Iterator theElements = listOfGameObjects.getIterator();
			while (theElements.hasNext()){
				GameObject gObj = (GameObject) theElements.getNext();
				if ( gObj instanceof IAudible){
					IAudible aObj = (IAudible)gObj;
					aObj.toggleSound();		
				}
			}
			setChanged();
			notifyObservers();
		}
	}

	public boolean isMuted() {
		return isMuted;
	}

	/**
	 * Quit, by calling the method System.exit(0) to terminate the program. The Command first 
	 * confirm the user's intent to quit before actually exiting.
	 */
	public void quit() {
		if(!isPaused()){
			togglePause();
		}
		if(JOptionPane.showConfirmDialog(null,"Do you really want to quit?", "Quit?", 
				JOptionPane.YES_NO_OPTION) == 0){
			System.exit(0);
		}
		else{
			togglePause();
		}
		setChanged();
		notifyObservers();
	}

	public GameObjectCollection returnList() {
		return listOfGameObjects;
	}

	@Override
	public void notifyObservers() {
		GameWorldProxy proxy = new GameWorldProxy(this);
		notifyObservers(proxy);
	}

	public void togglePause() {
		if(gameBall.getNumberOfBalls() >= 0){
			gamePaused = !gamePaused;
		}
	}

	public boolean isPaused() {
		return gamePaused;
	}

}