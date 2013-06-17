package pathfinder;

import at.andiwand.commons.math.geometry.GeometryLine2D;
import at.andiwand.commons.math.geometry.GeometryLineIntersection2D;
import at.andiwand.commons.math.vector.Vector2d;

public class UndirectedWall extends AbstractWall {

    public UndirectedWall(Vector2d vertexA, Vector2d vertexB) {
	super(vertexA, vertexB);
    }

    public UndirectedWall(GeometryLine2D line) {
	super(line.getPointA(), line.getPointB());
    }

    public boolean crosses(GeometryLine2D line) {
	GeometryLine2D wall = new GeometryLine2D(vertexA, vertexB);
	GeometryLineIntersection2D lineIntersection = new GeometryLineIntersection2D(
		line, wall);
	return lineIntersection.testIntersection();
    }

}