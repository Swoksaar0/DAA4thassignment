import graph.common.Graph;
import graph.util.DatasetGenerator;
import graph.util.GraphLoader;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class DatasetGeneratorTest {

    @Test
    public void testGenerateAll() throws Exception {
        DatasetGenerator generator = new DatasetGenerator(42);
        generator.generateAll();

        File sparseFile = new File("data/input_sparse.json");
        File denseFile = new File("data/input_dense.json");

        assertTrue(sparseFile.exists());
        assertTrue(denseFile.exists());
    }

    @Test
    public void testDatasetStructure() throws Exception {
        DatasetGenerator generator = new DatasetGenerator(42);
        generator.generateAll();

        Graph[] graphs = GraphLoader.loadDataset("data/input_sparse.json");
        GraphLoader.GraphMetadata[] metadata = GraphLoader.loadDatasetMetadata("data/input_sparse.json");

        assertNotNull(graphs);
        assertEquals(9, graphs.length);

        for (int i = 0; i < 3; i++) {
            assertTrue(metadata[i].nodeCount >= 6 && metadata[i].nodeCount <= 10);
        }

        for (int i = 3; i < 6; i++) {
            assertTrue(metadata[i].nodeCount >= 10 && metadata[i].nodeCount <= 20);
        }

        for (int i = 6; i < 9; i++) {
            assertTrue(metadata[i].nodeCount >= 20 && metadata[i].nodeCount <= 50);
        }
    }
}