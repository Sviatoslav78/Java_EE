package web.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logiclayer.service.UserService;
import testingFunctional.crud.services.UserService;

import java.io.IOException;

@WebServlet(name = "UserHttpController", value = "/http/userHttp")
public class UserHttpController extends HttpServlet {

    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    @EJB
    UserService userService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String parameter = request.getParameter(USER_ID);
        if (parameter != null) {
            request.setAttribute(USER_ID, userService.getUserById(Integer.parseInt(parameter)));
        } else {
            request.setAttribute(USER_ID, userService.getAllUsers());
        }
        request.getRequestDispatcher("/WEB-INF/jsptest/userinfo.jsp").forward(request, response);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(USER_ID, userService.createUser(Integer.parseInt(request.getParameter(USER_ID))));
        response.sendRedirect("/http/userHttp");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(USER_ID, userService.updateUserName
                (Integer.parseInt(request.getParameter(USER_ID)), request.getParameter(USER_NAME)));
        response.sendRedirect("/http/userHttp");
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(USER_ID, userService.deleteUserById(Integer.parseInt(request.getParameter(USER_ID))));
        response.sendRedirect("/http/userHttp");
    }
}
