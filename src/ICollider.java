/**
 * ICollider figures out if two objects have collided and if so handles the collision with the appropriate 
 * action.
 * @author Tyler Thomas
 *
 */
interface ICollider {
	boolean collidesWith(ICollider otherObject);
	void handleCollision(ICollider otherObject);
}
