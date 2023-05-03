package web.controller;

import entity.User;
import dto.validation.IDValidator;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logiclayer.service.DeliveryService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.exception.OnClientSideProblemException;

import java.io.IOException;
import java.util.Locale;

import static web.constant.AttributeConstants.SESSION_LANG;
import static web.constant.AttributeConstants.SESSION_USER;
import static web.constant.PageConstance.MAIN_WEB_FOLDER;
import static web.constant.PageConstance.USER_DELIVERY_GET_CONFIRM_FILE_NAME;
import static web.constant.PageConstance.USER_FOLDER;

/**
 * Process "user/delivers-to-get" page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */

@WebServlet(name = "UserDeliveryGetController", value = "/user/delivers-to-get")
public class UserDeliveryGetController extends HttpServlet {
    private static final String DELIVERIES_WHICH_ADDRESSED_FOR_USER = "deliveriesWhichAddressedForUser";
    private static final String DELIVERY_ID = "deliveryId";
    private static final Logger log = LogManager.getLogger(UserDeliveryGetController.class);

    @EJB
    private IDValidator idValidator;
    @EJB
    private DeliveryService deliveryService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("");

        request.setAttribute(DELIVERIES_WHICH_ADDRESSED_FOR_USER, deliveryService.getInfoToGetDeliveriesByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        request.getRequestDispatcher(MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_GET_CONFIRM_FILE_NAME).forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isValid = idValidator.isValid(request, DELIVERY_ID);
        log.debug("isValid" + isValid);
        if (!isValid) {
            log.error("id is not valid client is broken");

            throw new OnClientSideProblemException();
        }
        deliveryService.confirmGettingDelivery(((User) request.getSession().getAttribute(SESSION_USER)).getId(), Long.parseLong(request.getParameter(DELIVERY_ID)));
        request.setAttribute(DELIVERIES_WHICH_ADDRESSED_FOR_USER, deliveryService.getInfoToGetDeliveriesByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        request.getRequestDispatcher(MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_GET_CONFIRM_FILE_NAME).forward(request, response);
    }
}
