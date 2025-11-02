package graph;

import graph.common.*;
import graph.scc.*;
import graph.topo.*;
import graph.dagsp.*;
import graph.util.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        DatasetGenerator generator = new DatasetGenerator(42);
        try {
            generator.generateAll();
        } catch (Exception e) {
            System.err.println("Dataset generation failed: " + e.getMessage());
            return;
        }

        CSVReporter reporter = new CSVReporter();

        testDataset("data/input_sparse.json", reporter);
        testDataset("data/input_dense.json", reporter);

        reporter.saveToFile("results/performance_results.csv");
    }

    private static void testDataset(String filename, CSVReporter reporter) {
        Graph[] graphs = GraphLoader.loadDataset(filename);
        GraphLoader.GraphMetadata[] metadata = GraphLoader.loadDatasetMetadata(filename);

        if (graphs == null || metadata == null) {
            return;
        }

        for (int i = 0; i < graphs.length; i++) {
            runAllAlgorithms(graphs[i], metadata[i], reporter);
        }
    }

    private static void runAllAlgorithms(Graph graph, GraphLoader.GraphMetadata meta, CSVReporter reporter) {
        MetricsCollector tarjanMetrics = new MetricsCollector();
        TarjanSCC tarjanSCC = new TarjanSCC(graph, tarjanMetrics);
        List<List<Integer>> tarjanComponents = tarjanSCC.findSCCs();
        Graph condensation = tarjanSCC.buildCondensationGraph();

        MetricsCollector kosarajuMetrics = new MetricsCollector();
        KosarajuSCC kosarajuSCC = new KosarajuSCC(graph, kosarajuMetrics);
        kosarajuSCC.findSCCs();

        Map<Integer, Integer> nodeToComponent = new HashMap<>();
        for (int i = 0; i < tarjanComponents.size(); i++) {
            for (int node : tarjanComponents.get(i)) {
                nodeToComponent.put(node, i);
            }
        }
        int condensationSource = nodeToComponent.getOrDefault(meta.source, 0);

        MetricsCollector kahnMetrics = new MetricsCollector();
        KahnTopologicalSort kahnSort = new KahnTopologicalSort(condensation, kahnMetrics);
        List<Integer> topoOrder = kahnSort.sort();

        MetricsCollector dfsTopoMetrics = new MetricsCollector();
        DFSTopologicalSort dfsSort = new DFSTopologicalSort(condensation, dfsTopoMetrics);
        dfsSort.sort();

        MetricsCollector shortestMetrics = new MetricsCollector();
        DAGShortestPath dagSP = new DAGShortestPath(condensation, shortestMetrics);
        int[] shortest = dagSP.findShortestPaths(condensationSource, topoOrder);

        MetricsCollector longestMetrics = new MetricsCollector();
        DAGLongestPath dagLP = new DAGLongestPath(condensation, longestMetrics);
        int[] longest = dagLP.findLongestPaths(condensationSource, topoOrder);

        int criticalPathLength = 0;
        if (longest != null) {
            for (int dist : longest) {
                if (dist != Integer.MIN_VALUE && dist > criticalPathLength) {
                    criticalPathLength = dist;
                }
            }
        }

        reporter.addResult(
                meta.id,
                meta.variant,
                meta.density,
                meta.nodeCount,
                meta.edgeCount,
                tarjanComponents.size(),
                condensation.getVertices(),
                condensation.getEdgeCount(),
                tarjanMetrics.getDFSVisits(),
                tarjanMetrics.getEdgeTraversals(),
                tarjanMetrics.getStackOperations(),
                tarjanMetrics.getElapsedMillis(),
                kosarajuMetrics.getDFSVisits(),
                kosarajuMetrics.getEdgeTraversals(),
                kosarajuMetrics.getStackOperations(),
                kosarajuMetrics.getElapsedMillis(),
                kahnMetrics.getEdgeTraversals(),
                kahnMetrics.getStackOperations(),
                kahnMetrics.getElapsedMillis(),
                dfsTopoMetrics.getDFSVisits(),
                dfsTopoMetrics.getEdgeTraversals(),
                dfsTopoMetrics.getStackOperations(),
                dfsTopoMetrics.getElapsedMillis(),
                shortestMetrics.getRelaxations(),
                shortestMetrics.getElapsedMillis(),
                longestMetrics.getRelaxations(),
                longestMetrics.getElapsedMillis(),
                criticalPathLength
        );
    }
}