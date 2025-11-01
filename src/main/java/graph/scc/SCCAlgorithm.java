package graph.scc;

import graph.common.Graph;
import java.util.List;

public interface SCCAlgorithm {
    List<List<Integer>> findSCCs();
    Graph buildCondensationGraph();
}