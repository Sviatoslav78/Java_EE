package web.controller;

import dal.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

import static web.constant.AttributeConstants.LOGGED_USER_NAMES;
import static web.constant.AttributeConstants.SESSION_USER;
import static web.constant.PageConstance.ANONYMOUS_FOLDER;
import static web.constant.PageConstance.LOGIN_REQUEST_COMMAND;
import static web.constant.PageConstance.REDIRECT_COMMAND;

/**
 * Process "user/logout" page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */

@WebServlet(name = "LogOutController", value = "/user/logout")
public class LogOutController extends HttpServlet {
    private static final Logger log = LogManager.getLogger(LogOutController.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("");

        Map<String, HttpSession> loggedUsers = (Map<String, HttpSession>) request
                .getSession().getServletContext()
                .getAttribute(LOGGED_USER_NAMES);
        loggedUsers.remove(((User) request.getSession().getAttribute(SESSION_USER)).getEmail());
        request.getSession().invalidate();
        request.getRequestDispatcher(REDIRECT_COMMAND + ANONYMOUS_FOLDER + LOGIN_REQUEST_COMMAND).forward(request, response);
    }


}
