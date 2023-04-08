package web.controller;

import dal.entity.User;
import dto.LoginInfoDto;
import dto.mapper.RequestDtoMapper;
import dto.validation.LoginDtoValidator;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logiclayer.exeption.NoSuchUserException;
import logiclayer.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

import static web.constant.AttributeConstants.LOGGED_USER_NAMES;
import static web.constant.AttributeConstants.SESSION_USER;
import static web.constant.PageConstance.ANONYMOUS_FOLDER;
import static web.constant.PageConstance.LOGIN_FILE_NAME;
import static web.constant.PageConstance.MAIN_WEB_FOLDER;
import static web.constant.PageConstance.REDIRECT_COMMAND;
import static web.constant.PageConstance.USER_PROFILE_REQUEST_COMMAND;

/**
 * Process "anonymous/login" page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */

@WebServlet(name = "LoginController", value = "/anonymous/login")
public class LoginController extends HttpServlet {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String INPUT_HAS_ERRORS = "inputHasErrors";
    private static final Logger log = LogManager.getLogger(LoginController.class);
    @EJB
    private LoginDtoValidator loginDtoValidator;
    @EJB
    private UserService userService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("");
        request.getRequestDispatcher(MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + LOGIN_FILE_NAME).forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isValid = loginDtoValidator.isValid(request);
        log.debug("isValidRequest = " + isValid);
        if (!isValid) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            request.getRequestDispatcher(MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + LOGIN_FILE_NAME).forward(request, response);
            return;
        }
        LoginInfoDto loginInfoDto = getLoginInfoDtoRequestDtoMapper(request).mapToDto(request);
        Map<String, HttpSession> loggedUsers = (Map<String, HttpSession>) request
                .getSession().getServletContext()
                .getAttribute(LOGGED_USER_NAMES);
        if (loggedUsers.containsKey(loginInfoDto.getUsername())) {
            loggedUsers.get(loginInfoDto.getUsername()).invalidate();
        }
        try {
            User user = userService.loginUser(loginInfoDto);
            loggedUsers.put(loginInfoDto.getUsername(), request.getSession());
            request.getSession().setAttribute(SESSION_USER, user);
            request.getRequestDispatcher(REDIRECT_COMMAND + USER_PROFILE_REQUEST_COMMAND).forward(request, response);
        } catch (NoSuchUserException ignored) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            request.getRequestDispatcher(MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + LOGIN_FILE_NAME).forward(request, response);
        }
    }

    private RequestDtoMapper<LoginInfoDto> getLoginInfoDtoRequestDtoMapper(HttpServletRequest request) {
        return request1 -> LoginInfoDto.builder()
                .username(request.getParameter(USERNAME))
                .password(request.getParameter(PASSWORD))
                .build();
    }

}
