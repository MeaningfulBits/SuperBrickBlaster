package superBrickBlaster;

/**
 * Some game objects are movable, meaning that they provide an interface 
 * that allows other objects to control their movement.
 * @author Tyler Thomas
 *
 */
interface IMovable{
	void move(float gameClock);
	void bounceVertical();
	void bounceHorizontal();
	void alterCourse();
}
