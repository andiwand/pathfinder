package pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.andiwand.commons.math.geometry.GeometryLine2D;
import at.andiwand.commons.math.vector.Vector2d;

public class FloorMap {

    private double vertexDistance;

    private List<AbstractWall> walls = new ArrayList<AbstractWall>();
    private Map<Vector2d, List<AbstractWall>> connectedWallsMap = new HashMap<Vector2d, List<AbstractWall>>();

    private List<Vector2d> pathVertices = new ArrayList<Vector2d>();

    public FloorMap(double vertexDistance) {
	this.vertexDistance = vertexDistance;
    }

    public List<AbstractWall> getWalls() {
	return new ArrayList<AbstractWall>(walls);
    }

    public List<Vector2d> getPathVertices() {
	return new ArrayList<Vector2d>(pathVertices);
    }

    public void addWall(AbstractWall wall) {
	walls.add(wall);

	if (!connectedWallsMap.containsKey(wall.getVertexA()))
	    connectedWallsMap.put(wall.getVertexA(),
		    new ArrayList<AbstractWall>());
	if (!connectedWallsMap.containsKey(wall.getVertexB()))
	    connectedWallsMap.put(wall.getVertexB(),
		    new ArrayList<AbstractWall>());

	connectedWallsMap.get(wall.getVertexA()).add(wall);
	connectedWallsMap.get(wall.getVertexB()).add(wall);

	pathVertices.clear();

	for (Map.Entry<Vector2d, List<AbstractWall>> connectedWalls : connectedWallsMap
		.entrySet()) {
	    Vector2d vertex = connectedWalls.getKey();
	    List<AbstractWall> walls = connectedWalls.getValue();

	    Set<Vector2d> othersSet = new HashSet<Vector2d>();
	    for (AbstractWall w : walls) {
		othersSet.add(w.getVertexA());
		othersSet.add(w.getVertexB());
	    }
	    othersSet.remove(vertex);
	    List<Vector2d> others = new ArrayList<Vector2d>(othersSet);

	    if (walls.size() == 1) {
		Vector2d other = others.get(0);
		double vertexDistanceFactor = vertexDistance / Math.sqrt(2);
		Vector2d direction = other.sub(vertex).normalize()
			.mul(vertexDistanceFactor);
		Vector2d normal = direction.turnLeft();
		Vector2d pathVertex1 = vertex.sub(direction.add(normal));
		Vector2d pathVertex2 = vertex
			.sub(direction.add(normal.negate()));

		pathVertices.add(pathVertex1);
		pathVertices.add(pathVertex2);
	    } else {
		List<Double> angles = new ArrayList<Double>();
		for (Vector2d other : others) {
		    Vector2d direction = other.sub(vertex);
		    double angle = Math.atan2(direction.getY(),
			    direction.getX());
		    angles.add(angle);
		}

		Collections.sort(angles);
		angles.add(angles.get(0) + Math.PI * 2);

		for (int i = 0; i < angles.size() - 1;) {
		    double angle = (angles.get(i++) + angles.get(i)) / 2;

		    Vector2d direction = new Vector2d(vertexDistance);
		    direction = direction.mul(new Vector2d(Math.cos(angle),
			    Math.sin(angle)));
		    Vector2d pathVertex = vertex.add(direction);

		    pathVertices.add(pathVertex);
		}
	    }
	}
    }

    public boolean intersects(GeometryLine2D line) {
	for (AbstractWall wall : walls) {
	    if (wall.crosses(line))
		return true;

	    // if (testNearLinePoint(line, wall.getVertexA())) return true;
	    // if (testNearLinePoint(line, wall.getVertexB())) return true;
	}

	return false;
    }
    // private boolean testNearLinePoint(GeometryLine2D line, Vector2d point) {
    // Vector2d relativPoint = point.sub(line.getPointA());
    // double distance = line.getVectorAB().dot(relativPoint) /
    // line.getVectorAB().length();
    //
    // double height =
    // relativPoint.sub(line.getVectorAB().normalize().mul(distance)).length();
    // double length;
    //
    // if (distance < 0) {
    // length = Math.sqrt(vertexDistance * vertexDistance + height * height);
    // } else if (distance > line.getVectorAB().length()) {
    // double x = vertexDistance - line.getVectorAB().length();
    // length = Math.sqrt(x * x + height * height);
    // } else {
    // length = height;
    // }
    //
    // return length < vertexDistance * 0.5;
    // }

}