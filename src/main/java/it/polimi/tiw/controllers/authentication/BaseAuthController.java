package it.polimi.tiw.controllers.authentication;

import it.polimi.tiw.controllers.BaseServlet;
import it.polimi.tiw.dao.UserDAO;

import javax.servlet.ServletException;

abstract class BaseAuthController extends BaseServlet {

    protected static final String MISSING_CREDENTIAL_MESSAGE = "Missing credentials in request";

    protected UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO(connection);
    }
}
