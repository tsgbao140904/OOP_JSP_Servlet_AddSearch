package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DAO.UserDAO;
import org.example.DAO.UserDAOImpl;
import org.example.model.User;
import java.io.IOException;
import java.util.List;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
        System.out.println("SearchServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        System.out.println("Received search request with query: " + query);

        List<User> users = null;
        if (query != null && !query.trim().isEmpty()) {
            users = userDAO.searchByUsername(query.trim());
            System.out.println("Search completed, found " + (users != null ? users.size() : 0) + " users for query: " + query);
        } else {
            System.out.println("No valid search query provided");
        }

        request.setAttribute("users", users);
        request.setAttribute("query", query);
        request.getRequestDispatcher("/search.jsp").forward(request, response);
        System.out.println("Forwarded to search.jsp");
    }
}