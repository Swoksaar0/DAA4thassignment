package graph.dagsp;

import graph.common.*;
import java.util.*;

public class DAGShortestPath {
    private Graph graph;
    private Metrics metrics;

    public DAGShortestPath(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }


    public int[] findShortestPaths(int source, List<Integer> topoOrder) {
        if (topoOrder == null || topoOrder.size() != graph.getVertices()) {
            return null;
        }

        metrics.startTiming();

        int[] distances = new int[graph.getVertices()];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[source] = 0;
        for (int u : topoOrder) {
            if (distances[u] != Integer.MAX_VALUE) {
                for (Edge edge : graph.getEdges(u)) {
                    metrics.incrementRelaxations();
                    if (distances[u] + edge.weight < distances[edge.to]) {
                        distances[edge.to] = distances[u] + edge.weight;
                    }
                }
            }
        }

        metrics.stopTiming();
        return distances;
    }


    public List<Integer> reconstructPath(int source, int destination, int[] distances) {
        if (distances[destination] == Integer.MAX_VALUE) {
            return null;
        }

        List<Integer> path = new ArrayList<>();
        int current = destination;
        path.add(current);

        while (current != source) {
            boolean found = false;
            for (int v = 0; v < graph.getVertices(); v++) {
                for (Edge edge : graph.getEdges(v)) {
                    if (edge.to == current) {
                        if (distances[v] != Integer.MAX_VALUE &&
                                distances[v] + edge.weight == distances[current]) {
                            path.add(v);
                            current = v;
                            found = true;
                            break;
                        }
                    }
                }
                if (found) break;
            }
            if (!found) break;
        }

        Collections.reverse(path);
        return path;
    }
}