package com.example.my_demo.servlet;

import com.example.my_demo.dao.UserDao;
import com.example.my_demo.entity.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {

    @EJB
    private UserDao userDao;

    public void init() {
//        userDao = new UserDao();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = new User(1L, "login", "password");

        if(userDao.persistUser(user)) {
            request.getRequestDispatcher("/testJsp").forward(request, response);
        }
    }

    public void destroy() {
    }
}