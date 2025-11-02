import graph.common.*;
import graph.scc.KosarajuSCC;
import graph.scc.TarjanSCC;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class KosarajuSCCTest {

    @Test
    public void testSimpleCycle() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);

        KosarajuSCC scc = new KosarajuSCC(g, new MetricsCollector());
        List<List<Integer>> components = scc.findSCCs();

        assertEquals(1, components.size());
        assertEquals(3, components.get(0).size());
    }

    @Test
    public void testConsistencyWithTarjan() {
        Graph g = new Graph(6);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 4, 1);
        g.addEdge(4, 5, 1);
        g.addEdge(5, 3, 1);

        TarjanSCC tarjan = new TarjanSCC(g, new MetricsCollector());
        KosarajuSCC kosaraju = new KosarajuSCC(g, new MetricsCollector());

        List<List<Integer>> tarjanSCCs = tarjan.findSCCs();
        List<List<Integer>> kosarajuSCCs = kosaraju.findSCCs();

        assertEquals(tarjanSCCs.size(), kosarajuSCCs.size());
    }
}