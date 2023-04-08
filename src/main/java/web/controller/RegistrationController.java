package web.controller;

import dto.RegistrationInfoDto;
import dto.mapper.RequestDtoMapper;
import dto.validation.RegistrationDtoValidator;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logiclayer.exeption.OccupiedLoginException;
import logiclayer.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

import static web.constant.PageConstance.ANONYMOUS_FOLDER;
import static web.constant.PageConstance.LOGIN_REQUEST_COMMAND;
import static web.constant.PageConstance.MAIN_WEB_FOLDER;
import static web.constant.PageConstance.REDIRECT_COMMAND;
import static web.constant.PageConstance.REGISTRATION_FILE_NAME;

/**
 * Process "anonymous/registration" page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */

@WebServlet(name = "RegistrationController", value = "/anonymous/registration")
public class RegistrationController extends HttpServlet {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_REPEAT = "passwordRepeat";
    private static final String INPUT_HAS_ERRORS = "inputHasErrors";
    private static final String INPUT_LOGIN_ALREADY_TAKEN = "inputLoginAlreadyTaken";
    private static final Logger log = LogManager.getLogger(RegistrationController.class);
    @EJB
    private RegistrationDtoValidator registrationInfoDtoValidator;
    @EJB
    private UserService userService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("");
        request.getRequestDispatcher(MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + REGISTRATION_FILE_NAME).forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isValid = registrationInfoDtoValidator.isValid(request);
        log.debug("isValidRequest = " + isValid);

        if (!isValid) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            request.getRequestDispatcher(MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + REGISTRATION_FILE_NAME).forward(request, response);
            return;
        }
        String processingServiceRegistrationRequest = processingServiceRegistrationRequest(request, getRegistrationInfoDtoRequestDtoMapper(request).mapToDto(request));
        request.getRequestDispatcher(processingServiceRegistrationRequest).forward(request, response);
    }

    private RequestDtoMapper<RegistrationInfoDto> getRegistrationInfoDtoRequestDtoMapper(HttpServletRequest request) {
        return req -> RegistrationInfoDto.builder()
                .username(request.getParameter(USERNAME))
                .password(request.getParameter(PASSWORD))
                .passwordRepeat(request.getParameter(PASSWORD_REPEAT))
                .build();
    }

    private String processingServiceRegistrationRequest(HttpServletRequest request, RegistrationInfoDto registrationInfoDto) {
        try {
            userService.addNewUserToDB(registrationInfoDto);
            return REDIRECT_COMMAND + ANONYMOUS_FOLDER + LOGIN_REQUEST_COMMAND;
        } catch (OccupiedLoginException e) {
            request.setAttribute(INPUT_LOGIN_ALREADY_TAKEN, true);
            return MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + REGISTRATION_FILE_NAME;
        }
    }
}
