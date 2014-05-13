package superBrickBlaster;

import java.awt.Color;

/**
 * Some types of game objects have color which is changeable (meaning that 
 * they provide is interface which allows other objects to modify their 
 * color).
 * @author Tyler Thomas
 *
 */
interface IColorChangeable{
	void setColor(Color newColor);
}
