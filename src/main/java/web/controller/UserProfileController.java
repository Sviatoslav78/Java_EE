package web.controller;

import dal.entity.User;
import dto.validation.Validator;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logiclayer.exeption.NoSuchUserException;
import logiclayer.exeption.ToMachMoneyException;
import logiclayer.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.exception.OnClientSideProblemException;

import java.io.IOException;

import static web.constant.AttributeConstants.SESSION_USER;
import static web.constant.PageConstance.MAIN_WEB_FOLDER;
import static web.constant.PageConstance.USER_FOLDER;
import static web.constant.PageConstance.USER_PROFILE_FILE_NAME;

/**
 * Process "user/user-profile" page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@WebServlet(name = "UserProfileController", value = "/user/user-profile")
public class UserProfileController extends HttpServlet {
    private static final String MONEY = "money";
    private static final String INPUT_HAS_ERRORS = "inputHasErrors";
    private static final Logger log = LogManager.getLogger(UserProfileController.class);
    @EJB
    private UserService userService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("");

        request.getRequestDispatcher(MAIN_WEB_FOLDER + USER_FOLDER + USER_PROFILE_FILE_NAME).forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isValid = getValidator().isValid(request);
        log.debug("isValidRequest = " + isValid);

        if (!isValid) {
            request.setAttribute(INPUT_HAS_ERRORS, true);

            request.getRequestDispatcher(MAIN_WEB_FOLDER + USER_FOLDER + USER_PROFILE_FILE_NAME).forward(request, response);
            return;
        }
        long money = Long.parseLong(request.getParameter(MONEY));
        User user = (User) request.getSession().getAttribute(SESSION_USER);
        try {
            userService.replenishAccountBalance(user.getId(), money);
        } catch (NoSuchUserException e) {
            throw new OnClientSideProblemException();
        } catch (ToMachMoneyException e) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            request.getRequestDispatcher(MAIN_WEB_FOLDER + USER_FOLDER + USER_PROFILE_FILE_NAME).forward(request, response);
            return;
        }
        user.setUserMoneyInCents(user.getUserMoneyInCents() + money);
        request.setAttribute(SESSION_USER, user);
        request.getRequestDispatcher(MAIN_WEB_FOLDER + USER_FOLDER + USER_PROFILE_FILE_NAME).forward(request, response);
    }

    private Validator getValidator() {
        return request -> {
            try {
                return Long.parseLong(request.getParameter(MONEY)) > 0;
            } catch (NumberFormatException ex) {
                return false;
            }
        };
    }
}
