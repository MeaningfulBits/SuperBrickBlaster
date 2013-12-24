package superBrickBlaster;

/**
 * Some game objects are positionable, meaning that they provide an 
 * interface that allows other objects to change their location after 
 * they have been created.
 * @author Tyler Thomas
 *
 */
interface IPositionable{
	void setLocation(float newX, float newY);
}
