package com.inventory.dijkstra;
public class Edge {  // Public for compatibility with external use
    int to;
    int weight;

    public Edge(int to, int weight) {
        this.to = to;
        this.weight = weight;
    }
}
