package superBrickBlaster;

import java.awt.Graphics;
import java.awt.Point;

/** This interface defines the services (methods) provided by an object which is "Selectable" on the screen.
 * 
 * @author Tyler Thomas
 *
 */
interface ISelectable {

	//a way to mark an object as "selected" or not
	void setSelected(boolean yesNo);
	
	//a way to test whether an object is selected
	boolean isSelected();
	
	//a way to determine if a mouse point is "in" an object
	boolean contains(Point p);
	
	//a way to "draw" the object that knows about drawing different ways depending on "isSelected"
	void draw(Graphics g);
}
