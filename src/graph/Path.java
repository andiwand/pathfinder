package graph;

import java.util.ArrayList;
import java.util.List;

import at.andiwand.commons.math.geometry.GeometryLine2D;
import at.andiwand.commons.math.vector.Vector2d;

public class Path {

    private List<Vector2d> path = new ArrayList<Vector2d>();
    private double length;

    public Path() {
    }

    public Path(List<Vector2d> path) {
	this.path = new ArrayList<Vector2d>(path);

	for (int i = 0; i < path.size() - 1;) {
	    length += this.path.get(i++).sub(this.path.get(i)).length();
	}
    }

    public Path(Vector2d... vertices) {
	if (vertices.length == 0)
	    return;

	path.add(vertices[0]);
	for (int i = 0; i < vertices.length - 1;) {
	    length += vertices[i++].sub(vertices[i]).length();
	    path.add(vertices[i]);
	}
    }

    public Path(Path path) {
	this.path = new ArrayList<Vector2d>(path.path);
	length = path.length;
    }

    public String toString() {
	return path.toString();
    }

    public List<Vector2d> getPath() {
	return new ArrayList<Vector2d>(path);
    }

    public Vector2d getStart() {
	return path.get(0);
    }

    public Vector2d getEnd() {
	return path.get(path.size() - 1);
    }

    public double getLength() {
	return length;
    }

    public List<GeometryLine2D> getLines() {
	List<GeometryLine2D> result = new ArrayList<GeometryLine2D>();

	for (int i = 0; i < path.size() - 1;) {
	    result.add(new GeometryLine2D(path.get(i++), path.get(i)));
	}

	return result;
    }

    public Path addVertex(Vector2d vertex) {
	Path result = new Path(this);

	result.path.add(vertex);
	result.length += path.get(path.size() - 1).sub(vertex).length();

	return result;
    }

}