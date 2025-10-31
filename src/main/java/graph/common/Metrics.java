package graph.common;

public interface Metrics {
    void incrementDFSVisits();
    void incrementEdgeTraversals();
    void incrementRelaxations();
    void incrementStackOperations();
    long getDFSVisits();
    long getEdgeTraversals();
    long getRelaxations();
    long getStackOperations();
    void startTiming();
    void stopTiming();
    long getElapsedNanos();
    void reset();
}