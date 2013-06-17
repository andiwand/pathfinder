package pathfinder;

import graph.Path;
import graph.PathGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import at.andiwand.commons.math.vector.Vector2d;

public class AStar {

    private PathGraph graph;

    private Vector2d start;
    private Vector2d destination;

    private Map<Vector2d, OpenMapValue> openMap = new HashMap<Vector2d, OpenMapValue>();
    private Set<Vector2d> closedSet = new HashSet<Vector2d>();

    public AStar(PathGraph graph, Vector2d start, Vector2d destination) {
	this.graph = graph;

	this.start = start;
	this.destination = destination;
    }

    public Vector2d getStart() {
	return start;
    }

    public Vector2d getDestination() {
	return destination;
    }

    public Path calculatePath() {
	Path result = null;

	openMap.put(start, new OpenMapValue(start));

	do {
	    Vector2d minVertex = getMinVertex();

	    if (minVertex == destination) {
		result = openMap.get(destination).path;
		break;
	    }

	    expand(minVertex);
	    openMap.remove(minVertex);
	    closedSet.add(minVertex);
	} while (!openMap.isEmpty());

	openMap.clear();
	closedSet.clear();

	return result;
    }

    private void expand(Vector2d vertex) {
	Path parentPath = openMap.get(vertex).path;
	double parentG = parentPath.getLength();

	for (Vector2d neighbour : graph.getConnectedVertices(vertex)) {
	    if (closedSet.contains(neighbour))
		continue;

	    double g = parentG + vertex.sub(neighbour).length();
	    if (openMap.containsKey(neighbour)
		    && (g >= openMap.get(neighbour).path.getLength()))
		continue;

	    double f = g + neighbour.sub(destination).length();
	    openMap.put(neighbour,
		    new OpenMapValue(parentPath.addVertex(neighbour), f));
	}
    }

    private Vector2d getMinVertex() {
	Vector2d result = null;
	double minF = 0;

	for (Map.Entry<Vector2d, OpenMapValue> openEntry : openMap.entrySet()) {
	    double f = openEntry.getValue().f;

	    if ((result == null) || (minF > f)) {
		result = openEntry.getKey();
		minF = f;
	    }
	}

	return result;
    }

    private static class OpenMapValue {
	private Path path;
	private double f;

	public OpenMapValue(Vector2d start) {
	    this(new Path(start), 0);
	}

	public OpenMapValue(Path path, double f) {
	    this.path = path;
	    this.f = f;
	}
    }

}