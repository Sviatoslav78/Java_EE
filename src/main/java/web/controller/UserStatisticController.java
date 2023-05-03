package web.controller;

import entity.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logiclayer.service.BillService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.util.Pagination;

import java.io.IOException;

import static web.constant.AttributeConstants.SESSION_USER;
import static web.constant.PageConstance.MAIN_WEB_FOLDER;
import static web.constant.PageConstance.USER_FOLDER;
import static web.constant.PageConstance.USER_STATISTIC_FILE_NAME;

/**
 * Process user/user-statistic page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */

@WebServlet(name = "UserStatisticController", value = "/user/user-statistic")
public class UserStatisticController extends HttpServlet {

    private static final String USER_USER_STATISTIC = "user/user-statistic";
    private static final int PAGE_ATTRIBUTE = 1;
    private static final int PAGE_SIZE = 10;
    private static final String BILLS_LIST = "billsList";
    private static final Logger log = LogManager.getLogger(UserStatisticController.class);
    @EJB
    private Pagination pagination;
    @EJB
    private BillService billService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("");

        int pageAtribute = PAGE_ATTRIBUTE;
        int pageSize = PAGE_SIZE;
        if (pagination.validate(request)) {
            pageAtribute = Integer.parseInt(request.getParameter("page"));
            pageSize = Integer.parseInt(request.getParameter("size"));
        }
        int offset = (pageAtribute - 1) * pageSize;

        pagination.paginate(pageAtribute, pageSize, billService.countAllBillsByUserId(((User) request.getSession().getAttribute(SESSION_USER)).getId()), request, USER_USER_STATISTIC);
        request.setAttribute(BILLS_LIST, billService.getBillHistoryByUserId(((User) request.getSession().getAttribute(SESSION_USER)).getId(), offset, pageSize));
        request.getRequestDispatcher(MAIN_WEB_FOLDER + USER_FOLDER + USER_STATISTIC_FILE_NAME).forward(request, response);
    }


}
