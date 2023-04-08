package web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

import static web.constant.PageConstance.ANONYMOUS_FOLDER;
import static web.constant.PageConstance.INDEX_FILE_NAME;
import static web.constant.PageConstance.MAIN_WEB_FOLDER;

/**
 * Process "anonymous/index" page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */

@WebServlet(name = "IndexController", value = "/anonymous/index")
public class IndexController extends HttpServlet {
    private static final Logger log = LogManager.getLogger(IndexController.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("");

        request.getRequestDispatcher(MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + INDEX_FILE_NAME).forward(request, response);
    }

}
