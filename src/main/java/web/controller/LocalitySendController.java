package web.controller;

import com.google.gson.Gson;
import dto.validation.IDValidator;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logiclayer.service.LocalityService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.exception.OnClientSideProblemException;

import java.io.IOException;
import java.util.Locale;

import static web.constant.AttributeConstants.SESSION_LANG;

/**
 * Process "get/localitiesGet/by/localitySend/id" requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */

@WebServlet(name = "LocalitySendController", value = "/get/localitiesGet/by/localitySend/id")
public class LocalitySendController extends HttpServlet {
    private static final Logger log = LogManager.getLogger(LocalitySendController.class);
    private static final String ID = "id";
    @EJB
    private LocalityService localityService;
    @EJB
    private IDValidator idValidator;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("");

        if (!idValidator.isValid(request, ID)) {
            log.error("id is not valid client is broken");
            throw new OnClientSideProblemException();
        }
        String responseGson = new Gson().toJson(localityService.getLocaliseLocalitiesGetByLocalitySendId(
                (Locale) request.getSession().getAttribute(SESSION_LANG),
                Long.parseLong(request.getParameter(ID))));
        response.getWriter().print(responseGson);
    }


}
