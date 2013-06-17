package pathfinder;

import at.andiwand.commons.math.geometry.GeometryLine2D;
import at.andiwand.commons.math.geometry.GeometryLineIntersection2D;
import at.andiwand.commons.math.vector.Vector2d;

public class DirectedWall extends AbstractWall {

    private Vector2d normal;

    public DirectedWall(Vector2d vertexA, Vector2d vertexB) {
	this(vertexA, vertexB, vertexB.sub(vertexA).turnLeft().normalize());
    }

    public DirectedWall(Vector2d vertexA, Vector2d vertexB, Vector2d normal) {
	super(vertexA, vertexB);

	this.normal = normal.normalize();
    }

    public Vector2d getNormal() {
	return normal;
    }

    public boolean crosses(GeometryLine2D line) {
	Vector2d lineDirection = line.getVectorAB();

	if (normal.dot(lineDirection) >= 0)
	    return false;

	GeometryLine2D wall = new GeometryLine2D(vertexA, vertexB);
	GeometryLineIntersection2D lineIntersection = new GeometryLineIntersection2D(
		line, wall);
	return lineIntersection.testIntersection();
    }

}