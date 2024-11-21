package com.inventory.servlet;

import com.inventory.dao.ProductDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateStockServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Read the request body as JSON
            ObjectMapper mapper = new ObjectMapper();
            StockUpdateRequest updateRequest = mapper.readValue(request.getInputStream(), StockUpdateRequest.class);

            int productId = updateRequest.getProductId();
            int quantityToAdd = updateRequest.getQuantity();

            if (productId <= 0 || quantityToAdd <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"message\": \"Invalid input data.\"}");
                return;
            }

            // Update product stock in the database
            ProductDAO productDAO = new ProductDAO();
            productDAO.updateQuantity(productId, quantityToAdd);

            // Respond with success message
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\": \"Stock updated successfully.\"}");

        } catch (Exception ex) {
            Logger.getLogger(UpdateStockServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"Failed to update stock.\"}");
        }
    }

    // DTO class for stock update request
    static class StockUpdateRequest {
        private int productId;
        private int quantity;

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
