package com.inventory.servlet;

import com.inventory.dao.WarehouseDAO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Map;

public class GetWarehousesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WarehouseDAO warehouseDAO = new WarehouseDAO();

        try {
            Map<Integer, String> warehouses = warehouseDAO.getAllWarehouseNames();
            String json = new Gson().toJson(warehouses);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (ClassNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"Error fetching warehouse data.\"}");
            e.printStackTrace();
        }
    }
}
