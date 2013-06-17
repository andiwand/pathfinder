package graph;

import at.andiwand.commons.math.geometry.GeometryLine2D;
import at.andiwand.commons.math.graph.AbstractUndirectedEdge;
import at.andiwand.commons.math.vector.Vector2d;

public class PathEdge extends AbstractUndirectedEdge {

    private Vector2d vertexA;
    private Vector2d vertexB;
    private double distance;

    public PathEdge(Vector2d vertexA, Vector2d vertexB) {
	if (vertexA.equals(vertexB))
	    throw new IllegalArgumentException("No distance!");

	this.vertexA = vertexA;
	this.vertexB = vertexB;

	distance = vertexA.sub(vertexB).length();
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

    public double getWeight() {
	return distance;
    }

}