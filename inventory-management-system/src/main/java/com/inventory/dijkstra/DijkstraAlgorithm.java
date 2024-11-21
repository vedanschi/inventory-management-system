package com.inventory.dijkstra; 

import com.inventory.config.DatabaseConfig;
import java.sql.*;
import java.util.*;

/*class Graph {
    private final Map<Integer, List<Edge>> adjacencyList = new HashMap<>();

    public void addEdge(int from, int to, int weight) {
        adjacencyList.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(to, weight));
    }

    public List<Edge> getNeighbors(int warehouseId) {
        return adjacencyList.getOrDefault(warehouseId, new ArrayList<>());
    }
}

class Edge {
    int to;
    int weight;

    public Edge(int to, int weight) {
        this.to = to;
        this.weight = weight;
    }
}
*/
public class DijkstraAlgorithm {

    public static Graph loadGraph() {
        Graph graph = new Graph();
        String query = "SELECT from_warehouse_id, to_warehouse_id, edge_weight FROM warehouse_connections";

        try {
            // Explicitly load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DatabaseConfig.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    int from = resultSet.getInt("from_warehouse_id");
                    int to = resultSet.getInt("to_warehouse_id");
                    int weight = resultSet.getInt("edge_weight");
                    graph.addEdge(from, to, weight);
                    graph.addEdge(to, from, weight); // Assuming undirected graph
                }

            } catch (SQLException e) {
            }

        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found.");
        }

        return graph;
    }

    public static List<Integer> dijkstra(Graph graph, int start, int end) {
        Map<Integer, Integer> distances = new HashMap<>();
        Map<Integer, Integer> previous = new HashMap<>();
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        distances.put(start, 0);
        queue.add(new int[]{start, 0});

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int warehouseId = current[0];
            int currentDist = current[1];

            if (warehouseId == end) break;

            for (Edge neighbor : graph.getNeighbors(warehouseId)) {
                int newDist = currentDist + neighbor.weight;
                if (newDist < distances.getOrDefault(neighbor.to, Integer.MAX_VALUE)) {
                    distances.put(neighbor.to, newDist);
                    previous.put(neighbor.to, warehouseId);
                    queue.add(new int[]{neighbor.to, newDist});
                }
            }
        }

        List<Integer> path = new ArrayList<>();
        for (Integer at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        if (path.size() == 1 && !path.contains(start)) {
            System.out.println("No path found.");
            return Collections.emptyList();
        }

        return path;
    }
    public static Map<Integer, String> getWarehouseNames() {
    Map<Integer, String> warehouseNames = new HashMap<>();
    String query = "SELECT warehouse_id, name FROM warehouse";

    try (Connection connection = DatabaseConfig.getConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

        while (resultSet.next()) {
            warehouseNames.put(resultSet.getInt("warehouse_id"), resultSet.getString("name"));
        }

    } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
    }
    return warehouseNames;
}


    public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Graph graph = loadGraph();
    Map<Integer, String> warehouseNames = getWarehouseNames();

    System.out.print("Enter the starting warehouse ID: ");
    int start = scanner.nextInt();

    System.out.print("Enter the destination warehouse ID: ");
    int end = scanner.nextInt();

    List<Integer> path = dijkstra(graph, start, end);
    if (!path.isEmpty()) {
        System.out.print("Shortest path: ");
        for (int warehouseId : path) {
            System.out.print(warehouseNames.get(warehouseId) + " (ID: " + warehouseId + ") → ");
        }
        System.out.println("End");
    } else {
        System.out.println("No path exists between the specified warehouses.");
    }
    }
}
