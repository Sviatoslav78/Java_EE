package web.controller;

import entity.User;
import dto.DeliveryOrderCreateDto;
import dto.mapper.RequestDtoMapper;
import dto.validation.Validator;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logiclayer.exeption.FailCreateDeliveryException;
import logiclayer.exeption.UnsupportableWeightFactorException;
import logiclayer.service.BillService;
import logiclayer.service.LocalityService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Locale;

import static web.constant.AttributeConstants.SESSION_LANG;
import static web.constant.AttributeConstants.SESSION_USER;
import static web.constant.PageConstance.MAIN_WEB_FOLDER;
import static web.constant.PageConstance.USER_DELIVERY_INITIATION_FILE_NAME;
import static web.constant.PageConstance.USER_FOLDER;

/**
 * Process "user/user-delivery-initiation" page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */

@WebServlet(name = "UserDeliveryInitiationController", value = "/user/user-delivery-initiation")
public class UserDeliveryInitiationController extends HttpServlet {
    private static final String LOCALITY_LIST = "localityList";
    private static final String DELIVERY_WEIGHT = "deliveryWeight";
    private static final String LOCALITY_GET_ID = "localityGetID";
    private static final String LOCALITY_SAND_ID = "localitySandID";
    private static final String ADDRESSEE_EMAIL = "addresseeEmail";
    private static final String INPUT_HAS_ERRORS = "inputHasErrors";


    private static final Logger log = LogManager.getLogger(UserDeliveryInitiationController.class);

    @EJB
    private LocalityService localityService;
    @EJB
    private BillService billService;
    @EJB
    private Validator deliveryOrderCreateDtoValidator;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug(request.getMethod() + " UserDeliveryInitiation extends HttpServlet ");

        Locale o = (Locale) request.getSession().getAttribute(SESSION_LANG);
        request.setAttribute(LOCALITY_LIST, localityService.getLocaliseLocalities(o));
        request.getRequestDispatcher(MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_INITIATION_FILE_NAME).forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isValid = deliveryOrderCreateDtoValidator.isValid(request);
        log.debug("isValidRequest = " + isValid);

        request.setAttribute(LOCALITY_LIST, localityService.getLocaliseLocalities((Locale) request.getSession().getAttribute(SESSION_LANG)));
        if (!isValid) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            request.getRequestDispatcher(MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_INITIATION_FILE_NAME).forward(request, response);

            return;
        }
        try {
            billService.initializeBill(getDeliveryOrderCreateDtoRequestDtoMapper(request).mapToDto(request), ((User) request.getSession().getAttribute(SESSION_USER)).getId());
        } catch (UnsupportableWeightFactorException | FailCreateDeliveryException e) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
        }
        request.getRequestDispatcher(MAIN_WEB_FOLDER + USER_FOLDER + USER_DELIVERY_INITIATION_FILE_NAME).forward(request, response);
    }

    private RequestDtoMapper<DeliveryOrderCreateDto> getDeliveryOrderCreateDtoRequestDtoMapper(HttpServletRequest request) {
        return request1 -> DeliveryOrderCreateDto.builder()
                .deliveryWeight(Integer.parseInt(request.getParameter(DELIVERY_WEIGHT)))
                .localityGetID(Long.parseLong(request.getParameter(LOCALITY_GET_ID)))
                .localitySandID(Long.parseLong(request.getParameter(LOCALITY_SAND_ID)))
                .addresseeEmail(request.getParameter(ADDRESSEE_EMAIL))
                .build();
    }


}
