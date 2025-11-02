import graph.common.*;
import graph.dagsp.DAGLongestPath;
import graph.topo.DFSTopologicalSort;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class DAGLongestPathTest {

    @Test
    public void testSimplePath() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 5);
        g.addEdge(1, 2, 3);

        List<Integer> topoOrder = getTopoOrder(g);
        DAGLongestPath lp = new DAGLongestPath(g, new MetricsCollector());
        int[] distances = lp.findLongestPaths(0, topoOrder);

        assertNotNull(distances);
        assertEquals(0, distances[0]);
        assertEquals(5, distances[1]);
        assertEquals(8, distances[2]);
    }

    @Test
    public void testMultiplePaths() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 5);
        g.addEdge(1, 2, 3);
        g.addEdge(0, 2, 6);

        List<Integer> topoOrder = getTopoOrder(g);
        DAGLongestPath lp = new DAGLongestPath(g, new MetricsCollector());
        int[] distances = lp.findLongestPaths(0, topoOrder);

        assertEquals(8, distances[2]);
    }

    @Test
    public void testCriticalPath() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 5);
        g.addEdge(0, 2, 3);
        g.addEdge(1, 3, 4);
        g.addEdge(2, 3, 8);

        List<Integer> topoOrder = getTopoOrder(g);
        DAGLongestPath lp = new DAGLongestPath(g, new MetricsCollector());
        int criticalLength = lp.findCriticalPathLength(0, topoOrder);

        assertEquals(11, criticalLength);
    }

    @Test
    public void testPathReconstruction() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 5);
        g.addEdge(1, 2, 3);

        List<Integer> topoOrder = getTopoOrder(g);
        DAGLongestPath lp = new DAGLongestPath(g, new MetricsCollector());
        int[] distances = lp.findLongestPaths(0, topoOrder);
        List<Integer> path = lp.reconstructPath(0, 2, distances);

        assertNotNull(path);
        assertEquals(Arrays.asList(0, 1, 2), path);
    }

    private List<Integer> getTopoOrder(Graph g) {
        DFSTopologicalSort topo = new DFSTopologicalSort(g, new MetricsCollector());
        return topo.sort();
    }
}