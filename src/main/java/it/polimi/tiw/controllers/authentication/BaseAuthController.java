package it.polimi.tiw.controllers.authentication;

import it.polimi.tiw.controllers.BaseServlet;
import it.polimi.tiw.dao.UserDAO;
import it.polimi.tiw.exceptions.*;
import it.polimi.tiw.utils.credentials.LoginCredentials;
import it.polimi.tiw.utils.credentials.RegistrationCredentials;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public abstract class BaseAuthController extends BaseServlet {

    protected static final String MISSING_CREDENTIAL_MESSAGE = "Missing credentials in request";

    protected UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO(connection);
    }

    protected void tryLogin(HttpServletRequest req)
            throws SQLException, InvalidCredentialsException, MissingCredentialsException {
        LoginCredentials credentials = new LoginCredentials(req);

        if (credentials.areStringsInvalid()) {
            throw new MissingCredentialsException();
        }

        int loggedId = userDAO.login(credentials.getUsername(), credentials.getPassword());

        req.getSession().setAttribute("UserPk", loggedId);
    }

    protected void tryRegister(HttpServletRequest req)
            throws MissingCredentialsException, PasswordConfirmNotMatchException, SQLException,
                    InvalidEmailException, UsernameAlreadyUsedException {
        RegistrationCredentials credentials = new RegistrationCredentials(req);

        if (credentials.areStringsInvalid()) {
            throw new MissingCredentialsException();
        }

        if (!credentials.passwordAndConfirmMatch()) {
            throw new PasswordConfirmNotMatchException();
        }

        int registeredId =
                userDAO.register(
                        credentials.getEmail(),
                        credentials.getUsername(),
                        credentials.getPassword());

        req.getSession().setAttribute("UserPk", registeredId);
    }
}
