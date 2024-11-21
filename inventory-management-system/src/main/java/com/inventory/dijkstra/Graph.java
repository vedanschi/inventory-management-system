package com.inventory.dijkstra;

import java.util.*;

public class Graph {  // Public for external use
    private final Map<Integer, List<Edge>> adjacencyList = new HashMap<>();

    public void addEdge(int from, int to, int weight) {
        adjacencyList.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(to, weight));
    }

    public List<Edge> getNeighbors(int warehouseId) {
        return adjacencyList.getOrDefault(warehouseId, new ArrayList<>());
    }
}

