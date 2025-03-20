package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.DAO.FollowDAO;
import org.example.DAO.FollowDAOImpl;
import org.example.model.User;

import java.io.IOException;
import java.util.List;

@WebServlet("/follows/*")
public class FollowListServlet extends HttpServlet {
    private FollowDAO followDAO = new FollowDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/follows/following");
            return;
        }

        if ("/following".equals(pathInfo)) {
            List<User> following = followDAO.findFollowing(currentUser);
            request.setAttribute("following", following);
            request.setAttribute("listType", "following");
        } else if ("/followers".equals(pathInfo)) {
            List<User> followers = followDAO.findFollowers(currentUser);
            request.setAttribute("followers", followers);
            request.setAttribute("listType", "followers");
        }

        request.getRequestDispatcher("/follows.jsp").forward(request, response);
    }
} 