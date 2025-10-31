package graph.common;

public class Edge {
    public int from;
    public int to;
    public int weight;

    public Edge(int from, int to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public String toString() {
        return from + " -> " + to + " (w:" + weight + ")";
    }
}