import graph.common.*;
import graph.topo.DFSTopologicalSort;
import graph.topo.KahnTopologicalSort;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class TopologicalSortTest {

    @Test
    public void testKahnSimpleDAG() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);

        KahnTopologicalSort kahn = new KahnTopologicalSort(g, new MetricsCollector());
        List<Integer> order = kahn.sort();

        assertNotNull(order);
        assertEquals(3, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(1) < order.indexOf(2));
    }

    @Test
    public void testKahnCyclicGraph() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);

        KahnTopologicalSort kahn = new KahnTopologicalSort(g, new MetricsCollector());
        List<Integer> order = kahn.sort();

        assertNull(order);
    }

    @Test
    public void testDFSSimpleDAG() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);

        DFSTopologicalSort dfs = new DFSTopologicalSort(g, new MetricsCollector());
        List<Integer> order = dfs.sort();

        assertNotNull(order);
        assertEquals(3, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(1) < order.indexOf(2));
    }

    @Test
    public void testDiamondDAG() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 1);
        g.addEdge(0, 2, 1);
        g.addEdge(1, 3, 1);
        g.addEdge(2, 3, 1);

        KahnTopologicalSort kahn = new KahnTopologicalSort(g, new MetricsCollector());
        List<Integer> order = kahn.sort();

        assertNotNull(order);
        assertEquals(4, order.size());
        assertEquals(0, order.get(0).intValue());
        assertEquals(3, order.get(3).intValue());
    }
}
