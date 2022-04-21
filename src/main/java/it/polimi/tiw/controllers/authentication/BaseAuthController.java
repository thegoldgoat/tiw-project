package it.polimi.tiw.controllers.authentication;

import it.polimi.tiw.controllers.BaseServlet;
import it.polimi.tiw.dao.UserDAO;
import it.polimi.tiw.utils.ControllerUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public abstract class BaseAuthController extends BaseServlet {

    protected static final String MISSING_CREDENTIAL_MESSAGE = "Missing credentials in request";

    protected UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO(connection);
    }

    protected Credentials getCredentials(HttpServletRequest req) {
        return new Credentials(req.getParameter("username"), req.getParameter("password"));
    }

    protected static class Credentials {
        public final String username;
        public final String password;

        public Credentials(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public boolean areStringsInvalid() {
            return ControllerUtils.stringInvalid(username)
                    || ControllerUtils.stringInvalid(password);
        }
    }
}
