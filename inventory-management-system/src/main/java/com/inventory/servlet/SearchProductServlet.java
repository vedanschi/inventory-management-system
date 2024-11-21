package com.inventory.servlet;

import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchProductServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("productId");
        String productName = request.getParameter("productName");
        Product product = null;

        try {
            // Validate and search for product
            if (productId != null && !productId.isEmpty()) {
                product = productDAO.getProductById(Integer.parseInt(productId));
            } else if (productName != null && !productName.isEmpty()) {
                product = productDAO.getProductByName(productName);
            }

            if (product != null) {
                // Redirect to ProductDetails.html with productId
                response.sendRedirect("/InventoryManagementSystem/ProductDetails.html?productId=" + product.getProductId());
            } else {
                // Respond with an error message
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"message\": \"Product not found.\"}");
            }

        } catch (Exception ex) {
            Logger.getLogger(SearchProductServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"An error occurred while processing your request.\"}");
        }
    }
}
