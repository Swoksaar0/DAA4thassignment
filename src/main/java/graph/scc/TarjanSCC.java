package graph.scc;

import graph.common.*;
import java.util.*;

public class TarjanSCC implements SCCAlgorithm {
    private Graph graph;
    private Metrics metrics;
    private int[] ids;
    private int[] low;
    private boolean[] onStack;
    private Stack<Integer> stack;
    private int id;
    private List<List<Integer>> components;

    public TarjanSCC(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
        this.ids = new int[graph.getVertices()];
        this.low = new int[graph.getVertices()];
        this.onStack = new boolean[graph.getVertices()];
        this.stack = new Stack<>();
        this.id = 0;
        this.components = new ArrayList<>();
        Arrays.fill(ids, -1);
    }

    public List<List<Integer>> findSCCs() {
        metrics.startTiming();

        for (int i = 0; i < graph.getVertices(); i++) {
            if (ids[i] == -1) {
                dfs(i);
            }
        }

        metrics.stopTiming();
        return components;
    }

    private void dfs(int at) {
        metrics.incrementDFSVisits();
        ids[at] = id;
        low[at] = id;
        id++;
        stack.push(at);
        metrics.incrementStackOperations();
        onStack[at] = true;

        for (Edge edge : graph.getEdges(at)) {
            metrics.incrementEdgeTraversals();
            int to = edge.to;

            if (ids[to] == -1) {
                dfs(to);
            }

            if (onStack[to]) {
                low[at] = Math.min(low[at], low[to]);
            }
        }

        if (ids[at] == low[at]) {
            List<Integer> component = new ArrayList<>();

            while (true) {
                int node = stack.pop();
                metrics.incrementStackOperations();
                onStack[node] = false;
                component.add(node);

                if (node == at) {
                    break;
                }
            }

            components.add(component);
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