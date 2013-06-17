package pathfinder;

import graph.Path;
import graph.PathEdge;
import graph.PathGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import at.andiwand.commons.math.vector.Vector2d;

public class Pathfinder {

    private FloorMap floorMap;
    private PathGraph graph;

    public Pathfinder(FloorMap floorMap) {
	this.floorMap = floorMap;

	updateGraph();
    }

    public FloorMap getFloorMap() {
	return floorMap;
    }

    public PathGraph getGraph() {
	return graph;
    }

    public void updateGraph() {
	graph = new PathGraph();

	for (Vector2d pathVertex : floorMap.getPathVertices()) {
	    graph.addVertex(pathVertex);
	}

	List<Vector2d> vertexList = new ArrayList<Vector2d>(graph.getVertices());

	for (int i = 0; i < vertexList.size() - 1; i++) {
	    for (int j = i + 1; j < vertexList.size(); j++) {
		PathEdge edge = new PathEdge(vertexList.get(i),
			vertexList.get(j));

		if (floorMap.intersects(edge.getLine()))
		    continue;

		graph.addEdge(edge);
	    }
	}
    }

    public Path calculatePath(Vector2d pointA, Vector2d pointB) {
	PathGraph graph = new PathGraph(this.graph);

	Set<Vector2d> vertices = graph.getVertices();

	graph.addVertex(pointA);
	graph.addVertex(pointB);

	PathEdge edge = new PathEdge(pointA, pointB);
	if (!floorMap.intersects(edge.getLine()))
	    graph.addEdge(edge);

	for (Vector2d vertex : vertices) {
	    PathEdge edgeA = new PathEdge(vertex, pointA);
	    PathEdge edgeB = new PathEdge(vertex, pointB);

	    if (!floorMap.intersects(edgeA.getLine()))
		graph.addEdge(edgeA);
	    if (!floorMap.intersects(edgeB.getLine()))
		graph.addEdge(edgeB);
	}

	AStar aStar = new AStar(graph, pointA, pointB);

	return aStar.calculatePath();
    }

}