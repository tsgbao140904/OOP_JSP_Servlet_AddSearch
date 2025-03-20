package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.User;
import org.example.DAO.UserDAO;
import org.example.DAO.UserDAOImpl;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Lấy thông tin từ form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAOImpl();
        try {
            // Kiểm tra username đã tồn tại chưa
            try {
                User existingUser = userDAO.findByUsername(username);
                
                if (existingUser != null) {
                    request.setAttribute("error", "Username đã tồn tại!");
                    request.getRequestDispatcher("/register.jsp").forward(request, response);
                    return;
                }
                User newUser = new User();
            } catch (ServletException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Username chưa tồn tại, tiếp tục đăng ký

            
            // Tạo user mới
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setRole("USER"); // Mặc định là USER
            newUser.setCreatedAt(LocalDateTime.now());
            
            userDAO.save(newUser);
            
            // Chuyển hướng đến trang đăng nhập
            response.sendRedirect(request.getContextPath() + "/login");
            
        } catch (Exception e) {
            request.setAttribute("error", "Đã xảy ra lỗi khi đăng ký!");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            e.printStackTrace();
        }
    }
    
    // Hiển thị trang đăng ký
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
} 