package web.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logiclayer.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

import static web.constant.PageConstance.ADMIN_FOLDER;
import static web.constant.PageConstance.MAIN_WEB_FOLDER;
import static web.constant.PageConstance.USERS_JSP;

/**
 * Process "admin/users" page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@WebServlet(name = "AdminUsersController", value = "/institution")
public class AdminUsersController extends HttpServlet {
    private static final String USERS_LIST = "usersList";
    private static final Logger log = LogManager.getLogger(AdminUsersController.class);

    @EJB
    private UserService userService;


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug(request.getMethod() + " admin");

        request.setAttribute(USERS_LIST, userService.getAllUsers());
        request.getRequestDispatcher(MAIN_WEB_FOLDER + ADMIN_FOLDER + USERS_JSP).forward(request, response);
    }
}
