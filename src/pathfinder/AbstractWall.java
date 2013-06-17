package pathfinder;

import at.andiwand.commons.math.geometry.GeometryLine2D;
import at.andiwand.commons.math.vector.Vector2d;

public abstract class AbstractWall implements Wall {

    protected final Vector2d vertexA;
    protected final Vector2d vertexB;

    public AbstractWall(Vector2d vertexA, Vector2d vertexB) {
	if (vertexA.equals(vertexB))
	    throw new IllegalArgumentException("No distance!");

	this.vertexA = vertexA;
	this.vertexB = vertexB;
    }

    public Vector2d getVertexA() {
	return vertexA;
    }

    public Vector2d getVertexB() {
	return vertexB;
    }

    public GeometryLine2D getLine() {
	return new GeometryLine2D(vertexA, vertexB);
    }

    public abstract boolean crosses(GeometryLine2D line);

}