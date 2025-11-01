package graph.topo;

import graph.common.*;
import java.util.*;

public class DFSTopologicalSort implements TopologicalSort {
    private Graph graph;
    private Metrics metrics;
    private boolean[] visited;
    private Stack<Integer> stack;

    public DFSTopologicalSort(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
        this.visited = new boolean[graph.getVertices()];
        this.stack = new Stack<>();
    }

    public List<Integer> sort() {
        metrics.startTiming();

        for (int i = 0; i < graph.getVertices(); i++) {
            if (!visited[i]) {
                dfs(i);
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
            metrics.incrementStackOperations();
        }

        metrics.stopTiming();
        return result;
    }

    private void dfs(int v) {
        metrics.incrementDFSVisits();
        visited[v] = true;

        for (Edge edge : graph.getEdges(v)) {
            metrics.incrementEdgeTraversals();
            if (!visited[edge.to]) {
                dfs(edge.to);
            }
        }

        stack.push(v);
        metrics.incrementStackOperations();
    }
}