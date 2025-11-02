
import graph.common.*;
import graph.scc.TarjanSCC;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class TarjanSCCTest {

    @Test
    public void testSimpleCycle() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);

        TarjanSCC scc = new TarjanSCC(g, new MetricsCollector());
        List<List<Integer>> components = scc.findSCCs();

        assertEquals(1, components.size());
        assertEquals(3, components.get(0).size());
    }

    @Test
    public void testTwoSeparateCycles() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 2, 1);

        TarjanSCC scc = new TarjanSCC(g, new MetricsCollector());
        List<List<Integer>> components = scc.findSCCs();

        assertEquals(2, components.size());
    }

    @Test
    public void testDAG() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);

        TarjanSCC scc = new TarjanSCC(g, new MetricsCollector());
        List<List<Integer>> components = scc.findSCCs();

        assertEquals(3, components.size());
        for (List<Integer> component : components) {
            assertEquals(1, component.size());
        }
    }

    @Test
    public void testSingleNode() {
        Graph g = new Graph(1);

        TarjanSCC scc = new TarjanSCC(g, new MetricsCollector());
        List<List<Integer>> components = scc.findSCCs();

        assertEquals(1, components.size());
        assertEquals(1, components.get(0).size());
    }

    @Test
    public void testCondensationGraph() {
        Graph g = new Graph(5);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 4, 1);
        g.addEdge(4, 3, 1);

        TarjanSCC scc = new TarjanSCC(g, new MetricsCollector());
        scc.findSCCs();
        Graph condensation = scc.buildCondensationGraph();

        assertEquals(2, condensation.getVertices());
        assertTrue(condensation.getEdgeCount() >= 1);
    }
}