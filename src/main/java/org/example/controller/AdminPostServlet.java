package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DAO.PostsDAO;
import org.example.DAO.PostsDAOImpl;
import org.example.model.Posts;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/admin/posts/*")
public class AdminPostServlet extends HttpServlet {
    private PostsDAO postsDAO = new PostsDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            listPosts(request, response);
        } else if (pathInfo.startsWith("/edit/")) {
            showEditForm(request, response);
        }
    }

    private void listPosts(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Posts> posts = postsDAO.findAll();
        request.setAttribute("posts", posts);
        request.getRequestDispatcher("/admin/posts.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        Long postId = Long.parseLong(pathInfo.split("/")[2]);
        Posts post = postsDAO.findById(postId);
        if (post == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.setAttribute("post", post);
        request.getRequestDispatcher("/admin/edit-post.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.equals("/edit")) {
            updatePost(request, response);
        }
    }

    private void updatePost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Long postId = Long.parseLong(request.getParameter("postId"));
        String title = request.getParameter("title");
        String body = request.getParameter("body");
        String status = request.getParameter("status");

        Posts post = postsDAO.findById(postId);
        if (post == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        post.setTitle(title);
        post.setBody(body);
        post.setStatus(status);
        post.setUpdatedAt(LocalDateTime.now());

        postsDAO.update(post);
        response.sendRedirect(request.getContextPath() + "/admin/posts");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Long postId = Long.parseLong(request.getParameter("postId"));
        Posts post = postsDAO.findById(postId);
        if (post == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        postsDAO.delete(postId);
        response.setStatus(HttpServletResponse.SC_OK);
    }
} 