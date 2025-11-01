package graph.topo;

import graph.common.*;
import java.util.*;

public class KahnTopologicalSort implements TopologicalSort {
    private Graph graph;
    private Metrics metrics;

    public KahnTopologicalSort(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public List<Integer> sort() {
        metrics.startTiming();

        int[] inDegree = new int[graph.getVertices()];
        for (int v = 0; v < graph.getVertices(); v++) {
            for (Edge edge : graph.getEdges(v)) {
                inDegree[edge.to]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int v = 0; v < graph.getVertices(); v++) {
            if (inDegree[v] == 0) {
                queue.offer(v);
                metrics.incrementStackOperations();
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int current = queue.poll();
            metrics.incrementStackOperations();
            result.add(current);

            for (Edge edge : graph.getEdges(current)) {
                metrics.incrementEdgeTraversals();
                inDegree[edge.to]--;
                if (inDegree[edge.to] == 0) {
                    queue.offer(edge.to);
                    metrics.incrementStackOperations();
                }
            }
        }

        metrics.stopTiming();

        if (result.size() != graph.getVertices()) {
            return null;
        }

        return result;
    }
}