package graph.util;

import graph.common.Graph;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GraphLoader {
    private static final Gson gson = new Gson();

    public static Graph loadFromJSON(String filename) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            JsonObject json = gson.fromJson(content, JsonObject.class);
            return parseGraph(json);
        } catch (IOException e) {
            return null;
        }
    }

    public static Graph[] loadDataset(String filename) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            JsonObject root = gson.fromJson(content, JsonObject.class);

            if (!root.has("graphs")) {
                return null;
            }

            JsonArray graphsArray = root.getAsJsonArray("graphs");
            Graph[] graphs = new Graph[graphsArray.size()];

            for (int i = 0; i < graphsArray.size(); i++) {
                JsonObject graphJson = graphsArray.get(i).getAsJsonObject();
                graphs[i] = parseGraph(graphJson);
            }

            return graphs;
        } catch (IOException e) {
            return null;
        }
    }

    public static GraphMetadata[] loadDatasetMetadata(String filename) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            JsonObject root = gson.fromJson(content, JsonObject.class);

            if (!root.has("graphs")) {
                return null;
            }

            JsonArray graphsArray = root.getAsJsonArray("graphs");
            GraphMetadata[] metadata = new GraphMetadata[graphsArray.size()];

            for (int i = 0; i < graphsArray.size(); i++) {
                JsonObject graphJson = graphsArray.get(i).getAsJsonObject();
                metadata[i] = parseMetadata(graphJson);
            }

            return metadata;
        } catch (IOException e) {
            return null;
        }
    }

    private static Graph parseGraph(JsonObject json) {
        int n = json.get("n").getAsInt();
        Graph graph = new Graph(n);

        JsonArray edges = json.getAsJsonArray("edges");
        for (JsonElement edgeElem : edges) {
            JsonObject edge = edgeElem.getAsJsonObject();
            int u = edge.get("u").getAsInt();
            int v = edge.get("v").getAsInt();

            int w;
            if (edge.get("w").isJsonPrimitive()) {
                if (edge.get("w").getAsJsonPrimitive().isNumber()) {
                    w = (int) Math.round(edge.get("w").getAsDouble());
                } else {
                    w = 1;
                }
            } else {
                w = 1;
            }

            graph.addEdge(u, v, w);
        }

        return graph;
    }

    private static GraphMetadata parseMetadata(JsonObject json) {
        GraphMetadata meta = new GraphMetadata();

        if (json.has("id")) meta.id = json.get("id").getAsInt();
        if (json.has("n")) meta.nodeCount = json.get("n").getAsInt();
        if (json.has("source")) meta.source = json.get("source").getAsInt();
        if (json.has("variant")) meta.variant = json.get("variant").getAsString();
        if (json.has("density")) meta.density = json.get("density").getAsString();
        if (json.has("weight_model")) meta.weightModel = json.get("weight_model").getAsString();

        if (json.has("edges")) {
            meta.edgeCount = json.getAsJsonArray("edges").size();
        }

        return meta;
    }

    public static int extractSource(String filename) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            JsonObject json = gson.fromJson(content, JsonObject.class);

            if (json.has("source")) {
                return json.get("source").getAsInt();
            }
            return 0;
        } catch (IOException e) {
            return 0;
        }
    }

    public static Graph loadFromFile(String filename) {
        if (filename.endsWith(".json")) {
            return loadFromJSON(filename);
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();

            String[] parts = line.split(" ");
            int vertices = Integer.parseInt(parts[0]);
            int edges = Integer.parseInt(parts[1]);

            Graph graph = new Graph(vertices);

            for (int i = 0; i < edges; i++) {
                line = reader.readLine();
                parts = line.split(" ");
                int from = Integer.parseInt(parts[0]);
                int to = Integer.parseInt(parts[1]);
                int weight = Integer.parseInt(parts[2]);
                graph.addEdge(from, to, weight);
            }

            reader.close();
            return graph;
        } catch (IOException e) {
            return null;
        }
    }

    public static void saveToFile(Graph graph, String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(graph.getVertices() + " " + graph.getEdgeCount() + "\n");

            for (int i = 0; i < graph.getVertices(); i++) {
                for (var edge : graph.getEdges(i)) {
                    writer.write(edge.from + " " + edge.to + " " + edge.weight + "\n");
                }
            }

            writer.close();
        } catch (IOException e) {
        }
    }

    public static void saveToJSON(Graph graph, String filename, int source) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            writer.write("{\n");
            writer.write("  \"directed\": true,\n");
            writer.write("  \"n\": " + graph.getVertices() + ",\n");
            writer.write("  \"edges\": [\n");

            boolean first = true;
            for (int i = 0; i < graph.getVertices(); i++) {
                for (var edge : graph.getEdges(i)) {
                    if (!first) {
                        writer.write(",\n");
                    }
                    writer.write("    {\"u\": " + edge.from + ", \"v\": " + edge.to + ", \"w\": " + edge.weight + "}");
                    first = false;
                }
            }

            writer.write("\n  ],\n");
            writer.write("  \"source\": " + source + ",\n");
            writer.write("  \"weight_model\": \"edge\"\n");
            writer.write("}\n");

            writer.close();
        } catch (IOException e) {
        }
    }

    public static class GraphMetadata {
        public int id;
        public int nodeCount;
        public int edgeCount;
        public int source;
        public String variant;
        public String density;
        public String weightModel;

        @Override
        public String toString() {
            return String.format("Graph #%d: %d nodes, %d edges, variant=%s, density=%s, source=%d",
                    id, nodeCount, edgeCount, variant, density, source);
        }
    }
}