package graph.common;

public class MetricsCollector implements Metrics {
    private long dfsVisits = 0;
    private long edgeTraversals = 0;
    private long relaxations = 0;
    private long stackOperations = 0;
    private long startTime = 0;
    private long endTime = 0;

    public void incrementDFSVisits() {
        dfsVisits++;
    }

    public void incrementEdgeTraversals() {
        edgeTraversals++;
    }

    public void incrementRelaxations() {
        relaxations++;
    }

    public void incrementStackOperations() {
        stackOperations++;
    }

    public long getDFSVisits() {
        return dfsVisits;
    }

    public long getEdgeTraversals() {
        return edgeTraversals;
    }

    public long getRelaxations() {
        return relaxations;
    }

    public long getStackOperations() {
        return stackOperations;
    }

    public void startTiming() {
        startTime = System.nanoTime();
    }

    public void stopTiming() {
        endTime = System.nanoTime();
    }

    public long getElapsedNanos() {
        return endTime - startTime;
    }

    public double getElapsedMillis() {
        return (endTime - startTime) / 1_000_000.0;
    }

    public void reset() {
        dfsVisits = 0;
        edgeTraversals = 0;
        relaxations = 0;
        stackOperations = 0;
        startTime = 0;
        endTime = 0;
    }
}