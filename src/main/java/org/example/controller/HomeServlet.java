package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.DAO.PostsDAO;
import org.example.DAO.PostsDAOImpl;
import org.example.model.Posts;
import org.example.model.User;

import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final int POSTS_PER_PAGE = 5;
    private PostsDAO postsDAO = new PostsDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        int page = 1;
        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }

        int offset = (page - 1) * POSTS_PER_PAGE;
        List<Posts> posts = postsDAO.findAll(offset, POSTS_PER_PAGE, currentUser);

        request.setAttribute("posts", posts);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", (int) Math.ceil((double) posts.size() / POSTS_PER_PAGE));

        request.getRequestDispatcher("/home.jsp").forward(request, response);
    }
} 