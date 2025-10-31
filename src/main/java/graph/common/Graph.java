package graph.common;

import java.util.*;

public class Graph {
    private int vertices;
    private List<List<Edge>> adjacencyList;
    private Map<Integer, String> vertexNames;

    public Graph(int vertices) {
        this.vertices = vertices;
        this.adjacencyList = new ArrayList<>(vertices);
        this.vertexNames = new HashMap<>();
        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
            vertexNames.put(i, "Task_" + i);
        }
    }

    public void addEdge(int from, int to, int weight) {
        adjacencyList.get(from).add(new Edge(from, to, weight));
    }

    public List<Edge> getEdges(int vertex) {
        return adjacencyList.get(vertex);
    }

    public int getVertices() {
        return vertices;
    }

    public void setVertexName(int vertex, String name) {
        vertexNames.put(vertex, name);
    }

    public String getVertexName(int vertex) {
        return vertexNames.get(vertex);
    }

    public Graph getTranspose() {
        Graph transpose = new Graph(vertices);
        for (int v = 0; v < vertices; v++) {
            for (Edge edge : adjacencyList.get(v)) {
                transpose.addEdge(edge.to, edge.from, edge.weight);
            }
        }
        return transpose;
    }

    public int getEdgeCount() {
        int count = 0;
        for (int i = 0; i < vertices; i++) {
            count += adjacencyList.get(i).size();
        }
        return count;
    }
}