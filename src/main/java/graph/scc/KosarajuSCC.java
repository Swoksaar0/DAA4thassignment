package graph.scc;

import graph.common.*;
import java.util.*;

public class KosarajuSCC implements SCCAlgorithm {
    private Graph graph;
    private Metrics metrics;
    private List<List<Integer>> components;
    private boolean[] visited;
    private Stack<Integer> finishStack;

    public KosarajuSCC(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
        this.components = new ArrayList<>();
        this.visited = new boolean[graph.getVertices()];
        this.finishStack = new Stack<>();
    }

    public List<List<Integer>> findSCCs() {
        metrics.startTiming();

        for (int i = 0; i < graph.getVertices(); i++) {
            if (!visited[i]) {
                dfs1(i);
            }
        }

        Graph transpose = graph.getTranspose();
        Arrays.fill(visited, false);

        while (!finishStack.isEmpty()) {
            int v = finishStack.pop();
            metrics.incrementStackOperations();
            if (!visited[v]) {
                List<Integer> component = new ArrayList<>();
                dfs2(transpose, v, component);
                components.add(component);
            }
        }

        metrics.stopTiming();
        return components;
    }

    private void dfs1(int v) {
        metrics.incrementDFSVisits();
        visited[v] = true;

        for (Edge edge : graph.getEdges(v)) {
            metrics.incrementEdgeTraversals();
            if (!visited[edge.to]) {
                dfs1(edge.to);
            }
        }

        finishStack.push(v);
        metrics.incrementStackOperations();
    }

    private void dfs2(Graph g, int v, List<Integer> component) {
        metrics.incrementDFSVisits();
        visited[v] = true;
        component.add(v);

        for (Edge edge : g.getEdges(v)) {
            metrics.incrementEdgeTraversals();
            if (!visited[edge.to]) {
                dfs2(g, edge.to, component);
            }
        }
    }

    public Graph buildCondensationGraph() {
        Map<Integer, Integer> nodeToComponent = new HashMap<>();
        for (int i = 0; i < components.size(); i++) {
            for (int node : components.get(i)) {
                nodeToComponent.put(node, i);
            }
        }

        Graph condensation = new Graph(components.size());
        Set<String> addedEdges = new HashSet<>();

        for (int v = 0; v < graph.getVertices(); v++) {
            int fromComp = nodeToComponent.get(v);
            for (Edge edge : graph.getEdges(v)) {
                int toComp = nodeToComponent.get(edge.to);
                if (fromComp != toComp) {
                    String edgeKey = fromComp + "-" + toComp;
                    if (!addedEdges.contains(edgeKey)) {
                        condensation.addEdge(fromComp, toComp, edge.weight);
                        addedEdges.add(edgeKey);
                    }
                }
            }
        }

        return condensation;
    }

    public List<List<Integer>> getComponents() {
        return components;
    }
}