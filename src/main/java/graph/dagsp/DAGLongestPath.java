package graph.dagsp;

import graph.common.*;
import java.util.*;

public class DAGLongestPath {
    private Graph graph;
    private Metrics metrics;

    public DAGLongestPath(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public int[] findLongestPaths(int source, List<Integer> topoOrder) {
        if (topoOrder == null || topoOrder.size() != graph.getVertices()) {
            return null;
        }

        metrics.startTiming();

        int[] distances = new int[graph.getVertices()];
        Arrays.fill(distances, Integer.MIN_VALUE);
        distances[source] = 0;

        for (int u : topoOrder) {
            if (distances[u] != Integer.MIN_VALUE) {
                for (Edge edge : graph.getEdges(u)) {
                    metrics.incrementRelaxations();
                    if (distances[u] + edge.weight > distances[edge.to]) {
                        distances[edge.to] = distances[u] + edge.weight;
                    }
                }
            }
        }

        metrics.stopTiming();
        return distances;
    }

    public List<Integer> reconstructPath(int source, int destination, int[] distances) {
        if (distances[destination] == Integer.MIN_VALUE) {
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
                        if (distances[v] != Integer.MIN_VALUE &&
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


    public int findCriticalPathLength(int source, List<Integer> topoOrder) {
        int[] longest = findLongestPaths(source, topoOrder);
        if (longest == null) {
            return -1;
        }

        int maxLength = 0;
        for (int i = 0; i < longest.length; i++) {
            if (longest[i] != Integer.MIN_VALUE && longest[i] > maxLength) {
                maxLength = longest[i];
            }
        }

        return maxLength;
    }
}