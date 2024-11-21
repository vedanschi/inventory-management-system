package com.inventory.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.dijkstra.DijkstraAlgorithm;
import com.inventory.dijkstra.Graph;
import com.inventory.dao.WarehouseDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DijkstraServlet extends HttpServlet {

    private Graph graph;
    private Map<Integer, String> warehouseNames;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            graph = DijkstraAlgorithm.loadGraph();
            warehouseNames = new WarehouseDAO().getAllWarehouseNames();
            System.out.println("Graph and warehouse names loaded successfully.");
        } catch (Exception e) {
            System.err.println("Failed to load graph or warehouse names.");
            e.printStackTrace();
        }
    }

    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
        int startWarehouseId = Integer.parseInt(request.getParameter("startWarehouseId"));
        int endWarehouseId = Integer.parseInt(request.getParameter("endWarehouseId"));

        List<Integer> path = DijkstraAlgorithm.dijkstra(graph, startWarehouseId, endWarehouseId);

        if (path.isEmpty()) {
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"No path found.\"}");
        } else {
            response.setContentType("application/json");
            response.getWriter().write("{\"path\": " + new ObjectMapper().writeValueAsString(path) + "}");
        }
    } catch (Exception e) {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("{\"message\": \"Error calculating route.\"}");
    }
}
}
