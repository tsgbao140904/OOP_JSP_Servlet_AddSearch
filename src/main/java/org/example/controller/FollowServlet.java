package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.DAO.FollowDAO;
import org.example.DAO.FollowDAOImpl;
import org.example.DAO.UserDAOImpl;
import org.example.model.User;

import java.io.IOException;

@WebServlet("/follow/*")
public class FollowServlet extends HttpServlet {
    private FollowDAO followDAO = new FollowDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            Long followingId = Long.parseLong(pathInfo.substring(1));
            
            // Fetch the user to follow from the database
            User followingUser = new UserDAOImpl().findById(followingId); // Ensure you have a method to fetch user by ID

            if (followingUser == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User to follow not found");
                return;
            }

            followDAO.follow(currentUser, followingUser);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
        } catch (Exception e) {
            throw new ServletException("Error following user", e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            if (pathInfo == null || pathInfo.length() <= 1) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing user ID");
                return;
            }
            
            Long followingId = Long.parseLong(pathInfo.substring(1));
            
            // Fetch the user to unfollow from the database
            User followingUser = new UserDAOImpl().findById(followingId);

            if (followingUser == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User to unfollow not found");
                return;
            }

            // Kiểm tra xem currentUser và followingUser có đầy đủ thông tin không
            if (currentUser.getId() == null || followingUser.getId() == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user information");
                return;
            }

            followDAO.unfollow(currentUser, followingUser);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
        } catch (Exception e) {
            throw new ServletException("Error unfollowing user", e);
        }
    }
} 