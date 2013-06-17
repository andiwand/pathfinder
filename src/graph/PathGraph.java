package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import at.andiwand.commons.math.geometry.GeometryLine2D;
import at.andiwand.commons.math.graph.AbstractUndirectedGraph;
import at.andiwand.commons.math.vector.Vector2d;
import at.andiwand.commons.util.collection.HashMultiset;
import at.andiwand.commons.util.collection.Multiset;

public class PathGraph extends AbstractUndirectedGraph<Vector2d, PathEdge> {

    private Set<Vector2d> vertices;
    private Set<PathEdge> edges;
    private Map<Vector2d, Set<Vector2d>> connectedVertices;

    public PathGraph() {
	vertices = new HashSet<Vector2d>();
	edges = new HashSet<PathEdge>();
	connectedVertices = new HashMap<Vector2d, Set<Vector2d>>();
    }

    public PathGraph(PathGraph graph) {
	vertices = new HashSet<Vector2d>(graph.vertices);
	edges = new HashSet<PathEdge>(graph.edges);

	connectedVertices = new HashMap<Vector2d, Set<Vector2d>>();
	for (Map.Entry<Vector2d, Set<Vector2d>> connectedEdge : graph.connectedVertices
		.entrySet()) {
	    connectedVertices.put(connectedEdge.getKey(),
		    new HashSet<Vector2d>(connectedEdge.getValue()));
	}
    }

    public Set<Vector2d> getVertices() {
	return new HashSet<Vector2d>(vertices);
    }

    public Multiset<PathEdge> getEdges() {
	return new HashMultiset<PathEdge>(edges);
    }

    public Set<GeometryLine2D> getEdgeLines() {
	Set<GeometryLine2D> result = new HashSet<GeometryLine2D>();

	for (PathEdge edge : edges) {
	    result.add(edge.getLine());
	}

	return result;
    }

    public Set<Vector2d> getConnectedVertices(Vector2d vertex) {
	return new HashSet<Vector2d>(connectedVertices.get(vertex));
    }

    public Set<PathEdge> getConnectedEdges(Vector2d vertex) {
	Set<PathEdge> result = new HashSet<PathEdge>();

	for (Vector2d neighbour : connectedVertices.get(vertex)) {
	    result.add(new PathEdge(vertex, neighbour));
	}

	return result;
    }

    public Double getWeight() {
	double result = 0;

	for (PathEdge edge : edges) {
	    result += edge.getWeight();
	}

	return result;
    }

    public boolean addVertex(Vector2d vertex) {
	if (vertices.add(vertex)) {
	    connectedVertices.put(vertex, new HashSet<Vector2d>());

	    return true;
	}

	return false;
    }

    public boolean addEdge(PathEdge edge) {
	if (edges.add(edge)) {
	    connectedVertices.get(edge.getVertexA()).add(edge.getVertexB());
	    connectedVertices.get(edge.getVertexB()).add(edge.getVertexA());

	    return true;
	}

	return false;
    }

    public boolean removeVertex(Vector2d vertex) {
	if (vertices.remove(vertex)) {
	    for (Vector2d neighbour : connectedVertices.get(vertex)) {
		connectedVertices.get(neighbour).remove(neighbour);
	    }

	    return true;
	}

	return false;
    }

    public boolean removeEdge(PathEdge edge) {
	return edges.remove(edge);
    }

    public boolean removeAllEdges(PathEdge edge) {
	return removeEdge(edge);
    }

}