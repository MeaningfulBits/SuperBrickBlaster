import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.border.*;

/**
 * The main portion of the overall game. This part of the program
 * sets up the GUI and GameWorld and links the two with Command 
 * Objects.
 * @author Tyler Thomas
 *
 */
@SuppressWarnings("serial")
class Game extends JFrame implements ActionListener{
	private GameWorld gw; //model
	private MapView mv;
	private ScoreView sv;
	private Timer myTimer = new Timer(20, this);

	Game() {
		setTitle("BrickBreak");
		setSize(800, 600);
		buildMenuBar();
		guiLayout();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		gw = new GameWorld(); //Create "observable" model
		mv = new MapView(this); //Create MapView Observer
		gw.addObserver(mv);
		sv = new ScoreView(this); //Create ScoreView Observer
		gw.addObserver(sv); //Register the ScoreView Observer
		myTimer.start();
	}

	/**
	 * Sets up the GUI Layout manager, buttons, Key Binding, and other GUI elements.
	 */
	private void guiLayout() { //Default BorderLayout		
		JPanel inputPanel = new JPanel(); //Input Panel
		this.add(inputPanel,BorderLayout.SOUTH);

		int mapName = JComponent.WHEN_IN_FOCUSED_WINDOW; //Key Binding start

		InputMap imap = inputPanel.getInputMap(mapName);
		KeyStroke p = KeyStroke.getKeyStroke("P");
		imap.put(p, "theLetterP");
		KeyStroke s = KeyStroke.getKeyStroke("S");
		imap.put(s, "theLetterS");
		KeyStroke leftArrowKey = KeyStroke.getKeyStroke("LEFT");
		imap.put(leftArrowKey, "left");
		KeyStroke rightArrowKey = KeyStroke.getKeyStroke("RIGHT");
		imap.put(rightArrowKey, "right");
		KeyStroke upArrowKey = KeyStroke.getKeyStroke("UP");
		imap.put(upArrowKey, "up");
		KeyStroke cKey = KeyStroke.getKeyStroke('c');
		imap.put(cKey, "c");
		KeyStroke spaceKey = KeyStroke.getKeyStroke("SPACE");
		imap.put(spaceKey, "space");

		ActionMap amap = inputPanel.getActionMap();
		amap.put("theLetterP", pauseCommand);
		amap.put("theLetterS", soundCommand);
		amap.put("left", moveLeft);
		amap.put("right", moveRight);
		amap.put("up", increaseSpeed);
		amap.put("c", changePaddleMode);
		amap.put("space", nextBall);

		this.requestFocus(); //Key Binding end
	}

	/**
	 * Sets up the GUI menu bar for the program, links actions with command Objects.
	 */
	private void buildMenuBar() {
		JMenuBar bar = new JMenuBar();
		setJMenuBar(bar);

		//file menu
		JMenu fileMenu = new JMenu("File"); //file
		bar.add(fileMenu);

		//Create AbstractActions for file menu
		//file->New
		fileMenu.add(newGameCommand);
		
		//file->Play/Pause
		fileMenu.add(pauseCommand);

		//file->about
		fileMenu.add(aboutCommand);

		fileMenu.addSeparator(); //Separator

		//file->quit
		fileMenu.add(quitCommand);
		
		//option menu
		JMenu optionMenu = new JMenu("Options"); //file
		bar.add(optionMenu);
		
		//options->sound
		optionMenu.add(soundCommand);

	}

	public void actionPerformed(ActionEvent arg0) {
		gw.tickClock();
	}

	//CommandObjects classes and instantiations
	private NewGameCommand newGameCommand = new NewGameCommand();
	private class NewGameCommand extends AbstractAction {
		public NewGameCommand() {
			super("New Game");
		}

		public void actionPerformed(ActionEvent e) {
			System.out.println("New Game was selected.");
			gw = new GameWorld(); //Create "observable" model
			gw.addObserver(mv);
			gw.addObserver(sv); //Register the ScoreView Observer
			myTimer.start();
			JOptionPane.showMessageDialog(null, "You selected \"New Game\".");
		}

	}

	private AboutCommand aboutCommand = new AboutCommand();
	/**
	 * Output information about the Overall to the user in a dialog.
	 * @author Tyler Thomas
	 *
	 */
	private class AboutCommand extends AbstractAction {
		public AboutCommand() {
			super("About");
		}

		public void actionPerformed(ActionEvent arg0) {
			System.out.println("About was selected.");
			JOptionPane.showMessageDialog(null, "Programed by Tyler Thomas\nSacramento State University " +
			"\nSpring 2012 \nCSC 133");
		}
	}

	private Sound soundCommand = new Sound();
	/**
	 * Command Object to toggle sound by calling the appropriate operation within the GameWorld.
	 * @author Tyler Thomas
	 *
	 */
	private class Sound extends AbstractAction {
		public Sound() {
			super("Sound");
		}

		public void actionPerformed(ActionEvent e) {
			System.out.println("Toggle sound was selected.\n");
			gw.toggleSound();
		}
	}

	private MoveRight moveRight = new MoveRight();
	/**
	 * Move the paddle to the right by one unit by calling the appropriate method within the GameWorld.
	 */
	private class MoveRight extends AbstractAction {
		public MoveRight() {
			super("Move Paddle Right");
		}

		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Move right was selected.\n");
			gw.moveRight();
		}
	}

	private MoveLeft moveLeft = new MoveLeft();
	/**
	 * Move the paddle to the left by one unit by calling the appropriate method within the GameWorld.
	 */
	private class MoveLeft extends AbstractAction {
		public MoveLeft() {
			super("Move Paddle Left");
		}

		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Move left was selected.\n");
			gw.moveLeft();
		}
	}

	private IncreaseSpeed increaseSpeed = new IncreaseSpeed();
	/**
	 * Increase the speed of the ball by a factor of 20% by calling the appropriate method within the 
	 * GameWorld.
	 */
	private class IncreaseSpeed extends AbstractAction {
		public IncreaseSpeed() {
			super("Increase Speed");
		}

		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Increase Speed was selected.\n");
			gw.increaseSpeed(.2f);
		}
	}

	private ChangePaddleMode changePaddleMode = new ChangePaddleMode();
	/**
	 * If the paddle is in normal mode then it switches to random mode, and vice 
	 * versa, the appropriate method is called from within the GameWorld.
	 */
	private class ChangePaddleMode extends AbstractAction {
		public ChangePaddleMode() {
			super("Change Paddle Mode");
		}

		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Change paddle mode was selected.\n");
			gw.changePaddleMode();
		}
	}

	private NextBall nextBall = new NextBall();
	/**
	 * If there are balls remaining then a new ball is put into play; otherwise, the game is over, the 
	 * appropriate method is called from within the GameWorld.
	 */
	private class NextBall extends AbstractAction {
		public NextBall() {
			super("Undo Bonus");
		}

		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Next ball was selected.");
			gw.nextBall();
		}
	}

	private PlayCommand pauseCommand = new PlayCommand();
	/**
	 * Play or Pause the game by calling the appropriate method within the GameWorld.
	 */
	private class PlayCommand extends AbstractAction {
		public PlayCommand() {
			super("Pause");
		}

		public void actionPerformed(ActionEvent arg0) {
			gw.togglePause();
		}
	}

	private QuitCommand quitCommand = new QuitCommand();
	/**
	 * Quit by calling the appropriate method within the GameWorld.
	 */
	private class QuitCommand extends AbstractAction {
		public QuitCommand() {
			super("Quit");
		}

		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Quit was selected.\n");
			gw.quit();
		}
	}
}

/**
 * Prints a "map" showing the current world state by calling the appropriate method [map()] within the GameWorld 
 * whenever the GameWorld is updated. Note that the received GameWorld is a GameWorldProxy and is cast to type 
 * IGameWorld in order to access the GameWorld methods in it.
 * @author Tyler Thomas
 *
 */
@SuppressWarnings("serial")
class MapView extends JPanel implements Observer, MouseMotionListener{
	private GameWorld theGameWorld;
	private float zoomX = 600;
	private float zoomY = 500;

	MapView(JFrame myModel) {
		myModel.add(this,BorderLayout.CENTER);//Center Panel End
		addMouseMotionListener(this);
	}

	public void update(Observable o, Object arg) {
		theGameWorld = (GameWorld)o;
		repaint();
	}

	@Override
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform saveAT = g2d.getTransform();

		//Screen Transformation (used so that the GameWorld appears "Right Side Up")
		g2d.translate(0, getHeight()); //apply a translation that moves the MapView down to the ScreenHeight
		g2d.scale(1, -1); //flips the screen up in the y direction
		//end of Screen Transformation

		AffineTransform worldToND = new AffineTransform();
		AffineTransform ndToScreen = new AffineTransform();
		AffineTransform theVTM = new AffineTransform();

		//update the viewing transformation Matrix
		ndToScreen.translate(0, 0); ndToScreen.scale(getWidth(), getHeight());
		worldToND.translate(0, 0); worldToND.scale((1/(zoomX)), (1/(zoomY)));
		theVTM = ndToScreen;
		theVTM.concatenate(worldToND);

		//concatenate the VTM onto the g2d's current transformation
		g2d.transform(theVTM);

		Iterator theElements = theGameWorld.returnList().getIterator();
		while (theElements.hasNext()){
			theElements.getNext().draw(g2d);
		}

		g2d.setTransform(saveAT);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}


/**
 * Print a display (output lines of text) giving the current game state values, including: (1) the current game clock 
 * value, (2) the current score, (3) the current sound state, and (4) the number of balls left in the game, by calling 
 * the appropriate method within the GameWorld. Code to update JLabels from data in the Observable (GameWorldProxy)
 * @author Tyler Thomas
 *
 */
@SuppressWarnings("serial")
final class ScoreView extends JPanel implements Observer {
	private JLabel scoreLabel = new JLabel("Game Clock: 0 Score: 0 Balls: 3 Sound: On ");

	ScoreView(JFrame myModel){
		JPanel topPanel = new JPanel(); //Top Panel
		topPanel.setBorder(new TitledBorder(" Score:"));
		topPanel.add(scoreLabel);
		myModel.add(topPanel,BorderLayout.NORTH); //End of Top Panel
	}

	public void update(Observable o, Object arg) {
		scoreLabel.setText(((IGameWorld) arg).display());
	}
}