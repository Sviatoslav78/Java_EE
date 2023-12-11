package testingFunctional.crud.http;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import testingFunctional.crud.services.UserService;
import javax.enterprise.inject.Instance;

import java.io.IOException;

@WebServlet(name = "UserHttpController", value = "/http/userHttp")
public class UserHttpController extends HttpServlet {

    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    @EJB
    Instance<UserService> userService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String parameter = request.getParameter(USER_ID);
        if (parameter != null) {
            request.setAttribute(USER_ID, userService.get().getUserById(Integer.parseInt(parameter)));
        } else {
            request.setAttribute(USER_ID, userService.get().getAllUsers());
        }
        request.getRequestDispatcher("/WEB-INF/jsptest/userinfo.jsp").forward(request, response);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(USER_ID, userService.get().createUser(Integer.parseInt(request.getParameter(USER_ID))));
        response.sendRedirect("/http/userHttp");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(USER_ID, userService.get().updateUserName
                (Integer.parseInt(request.getParameter(USER_ID)), request.getParameter(USER_NAME)));
        response.sendRedirect("/http/userHttp");
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(USER_ID, userService.get().deleteUserById(Integer.parseInt(request.getParameter(USER_ID))));
        response.sendRedirect("/http/userHttp");
    }
}
