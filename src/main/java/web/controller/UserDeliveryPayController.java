package web.controller;

import entity.User;
import dto.validation.IDValidator;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logiclayer.exeption.OperationFailException;
import logiclayer.service.BillService;
import logiclayer.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.exception.OnClientSideProblemException;

import java.io.IOException;
import java.util.Locale;

import static web.constant.AttributeConstants.SESSION_LANG;
import static web.constant.AttributeConstants.SESSION_USER;
import static web.constant.PageConstance.MAIN_WEB_FOLDER;
import static web.constant.PageConstance.USER_DELIVERY_PAY_FILE_NAME;
import static web.constant.PageConstance.USER_FOLDER;

/**
 * Process "user/user-delivery-pay" page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */

@WebServlet(name = "UserDeliveryPayController", value = "/user/user-delivery-pay")
public class UserDeliveryPayController extends HttpServlet {
    private static final String BILL_INFO_TO_PAY = "BillInfoToPay";
    private static final String ID = "Id";
    private static final Logger log = LogManager.getLogger(UserDeliveryPayController.class);
    private static final String NOT_ENOUGH_MONEY = "notEnoughMoney";
    @EJB
    private BillService billService;
    @EJB
    private UserService userService;
    @EJB
    private IDValidator idValidator;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("");

        request.setAttribute(BILL_INFO_TO_PAY, billService.getInfoToPayBillsByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        request.getRequestDispatcher(MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_PAY_FILE_NAME).forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("");
        if (!idValidator.isValid(request, ID)) {
            log.error("id is not valid client is broken");
            throw new OnClientSideProblemException();
        }
        User sessionUser = (User) request.getSession().getAttribute(SESSION_USER);
        try {
            billService.payForDelivery(sessionUser.getId(), Long.parseLong(request.getParameter(ID)));
            sessionUser.setUserMoneyInCents(userService.getUserBalance(sessionUser.getId()));
        } catch (OperationFailException e) {
            request.setAttribute(NOT_ENOUGH_MONEY, true);
        }
        request.setAttribute(BILL_INFO_TO_PAY, billService.getInfoToPayBillsByUserID(((User) request.getSession().getAttribute(SESSION_USER)).getId(), (Locale) request.getSession().getAttribute(SESSION_LANG)));
        request.getRequestDispatcher(MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_PAY_FILE_NAME).forward(request, response);
    }
}
