package com.inventory.servlet;

import com.inventory.dao.ProductDAO;
import com.inventory.dao.WarehouseDAO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransferProductServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int newWarehouseId = Integer.parseInt(request.getParameter("warehouseId"));

            ProductDAO productDAO = new ProductDAO();
            productDAO.transferProduct(productId, newWarehouseId);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\": \"Product transferred successfully.\"}");

        } catch (ClassNotFoundException | NumberFormatException ex) {
            Logger.getLogger(TransferProductServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"Failed to transfer product. Please check your input.\"}");
        }
    }
}
