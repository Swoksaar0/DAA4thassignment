package graph.util;

import java.io.*;
import java.util.*;

public class CSVReporter {
    private List<String[]> rows;

    public CSVReporter() {
        this.rows = new ArrayList<>();
        addHeader();
    }

    private void addHeader() {
        rows.add(new String[]{
                "graph_id",
                "variant",
                "density",
                "nodes",
                "edges",
                "sccs_found",
                "condensation_nodes",
                "condensation_edges",
                "tarjan_dfs_visits",
                "tarjan_edge_traversals",
                "tarjan_stack_ops",
                "tarjan_time_ms",
                "kosaraju_dfs_visits",
                "kosaraju_edge_traversals",
                "kosaraju_stack_ops",
                "kosaraju_time_ms",
                "kahn_edge_traversals",
                "kahn_queue_ops",
                "kahn_time_ms",
                "dfs_topo_visits",
                "dfs_topo_edge_traversals",
                "dfs_topo_stack_ops",
                "dfs_topo_time_ms",
                "shortest_relaxations",
                "shortest_time_ms",
                "longest_relaxations",
                "longest_time_ms",
                "critical_path_length"
        });
    }

    public void addResult(
            int graphId,
            String variant,
            String density,
            int nodes,
            int edges,
            int sccsFound,
            int condensationNodes,
            int condensationEdges,
            long tarjanDFS,
            long tarjanEdges,
            long tarjanStack,
            double tarjanTime,
            long kosarajuDFS,
            long kosarajuEdges,
            long kosarajuStack,
            double kosarajuTime,
            long kahnEdges,
            long kahnQueue,
            double kahnTime,
            long dfsTopoDFS,
            long dfsTopoEdges,
            long dfsTopoStack,
            double dfsTopoTime,
            long shortestRelax,
            double shortestTime,
            long longestRelax,
            double longestTime,
            int criticalPathLength
    ) {
        rows.add(new String[]{
                String.valueOf(graphId),
                variant,
                density,
                String.valueOf(nodes),
                String.valueOf(edges),
                String.valueOf(sccsFound),
                String.valueOf(condensationNodes),
                String.valueOf(condensationEdges),
                String.valueOf(tarjanDFS),
                String.valueOf(tarjanEdges),
                String.valueOf(tarjanStack),
                String.format(Locale.US, "%.4f", tarjanTime),
                String.valueOf(kosarajuDFS),
                String.valueOf(kosarajuEdges),
                String.valueOf(kosarajuStack),
                String.format(Locale.US, "%.4f", kosarajuTime),
                String.valueOf(kahnEdges),
                String.valueOf(kahnQueue),
                String.format(Locale.US, "%.4f", kahnTime),
                String.valueOf(dfsTopoDFS),
                String.valueOf(dfsTopoEdges),
                String.valueOf(dfsTopoStack),
                String.format(Locale.US, "%.4f", dfsTopoTime),
                String.valueOf(shortestRelax),
                String.format(Locale.US, "%.4f", shortestTime),
                String.valueOf(longestRelax),
                String.format(Locale.US, "%.4f", longestTime),
                String.valueOf(criticalPathLength)
        });
    }

    public void saveToFile(String filename) {
        try {
            File file = new File(filename);
            file.getParentFile().mkdirs();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String[] row : rows) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Failed to write CSV: " + e.getMessage());
        }
    }
}