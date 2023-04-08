package web.controller;

import dto.DeliveryInfoRequestDto;
import dto.PriceAndTimeOnDeliveryDto;
import dto.mapper.RequestDtoMapper;
import dto.validation.Validator;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logiclayer.service.DeliveryService;
import logiclayer.service.LocalityService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import static web.constant.AttributeConstants.SESSION_LANG;
import static web.constant.PageConstance.COUNTER_FILE_NAME;
import static web.constant.PageConstance.MAIN_WEB_FOLDER;

/**
 * Process "counter" page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */

@Singleton
@WebServlet(name = "CounterController", value = "/counter")
public class CounterController extends HttpServlet {
    private static final String LOCALITY_LIST = "localityList";
    private static final String COST_AND_TIME_DTO = "CostAndTimeDto";
    private static final String IS_NOT_EXIST_SUCH_WAY_OR_WEIGHT_FOR_THIS_WAY = "IsNotExistSuchWayOrWeightForThisWay";
    private static final String DELIVERY_WEIGHT = "deliveryWeight";
    private static final String LOCALITY_GET_ID = "localityGetID";
    private static final String LOCALITY_SAND_ID = "localitySandID";
    private static final String INPUT_HAS_ERRORS = "inputHasErrors";
    private static final Logger log = LogManager.getLogger(CounterController.class);
    @EJB
    private LocalityService localityService;
    @EJB
    private DeliveryService deliveryService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("");

        request.setAttribute(LOCALITY_LIST, localityService.getLocaliseLocalities((Locale) request.getSession().getAttribute(SESSION_LANG)));
        request.getRequestDispatcher(MAIN_WEB_FOLDER + COUNTER_FILE_NAME).forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isDataValid = getDeliveryInfoRequestDtoValidator().isValid(request);
        log.debug("isValidRequest = " + isDataValid);

        request.setAttribute(LOCALITY_LIST, localityService.getLocaliseLocalities((Locale) request.getSession().getAttribute(SESSION_LANG)));
        if (!isDataValid) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            request.getRequestDispatcher(MAIN_WEB_FOLDER + COUNTER_FILE_NAME).forward(request, response);
            return;
        }
        Optional<PriceAndTimeOnDeliveryDto> deliveryCostAndTimeDto = deliveryService.getDeliveryCostAndTimeDto
                (getDeliveryInfoRequestDtoRequestDtoMapper(request).mapToDto(request));
        if (deliveryCostAndTimeDto.isPresent()) {
            request.setAttribute(COST_AND_TIME_DTO, deliveryCostAndTimeDto.get());
            request.getRequestDispatcher(MAIN_WEB_FOLDER + COUNTER_FILE_NAME).forward(request, response);
            return;
        }
        request.setAttribute(IS_NOT_EXIST_SUCH_WAY_OR_WEIGHT_FOR_THIS_WAY, true);
        request.getRequestDispatcher(MAIN_WEB_FOLDER + COUNTER_FILE_NAME).forward(request, response);
    }

    private RequestDtoMapper<DeliveryInfoRequestDto> getDeliveryInfoRequestDtoRequestDtoMapper(HttpServletRequest request) {
        return request1 -> DeliveryInfoRequestDto.builder()
                .deliveryWeight(Integer.parseInt(request.getParameter(DELIVERY_WEIGHT)))
                .localityGetID(Long.parseLong(request.getParameter(LOCALITY_GET_ID)))
                .localitySandID(Long.parseLong(request.getParameter(LOCALITY_SAND_ID)))
                .build();
    }

    private Validator getDeliveryInfoRequestDtoValidator() {
        return request -> {
            try {
                return ((Integer.parseInt(request.getParameter(DELIVERY_WEIGHT)) > 0) &&
                        (Long.parseLong(request.getParameter(LOCALITY_GET_ID)) > 0) &&
                        (Long.parseLong(request.getParameter(LOCALITY_SAND_ID)) > 0));
            } catch (NumberFormatException ex) {
                return false;
            }
        };
    }
}
