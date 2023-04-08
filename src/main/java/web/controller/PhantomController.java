package web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

import static web.constant.PageConstance.ERROR_404_COMMAND;
import static web.constant.PageConstance.REDIRECT_COMMAND;

/**
 * Process request for which application context has no mapping
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@WebServlet(name = "PhantomController", value = "/offnow")
public class PhantomController extends HttpServlet {
    private static final Logger log = LogManager.getLogger(PhantomController.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("");
        request.getRequestDispatcher(REDIRECT_COMMAND + ERROR_404_COMMAND).forward(request, response);
    }


}
