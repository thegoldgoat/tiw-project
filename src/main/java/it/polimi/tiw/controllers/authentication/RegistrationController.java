package it.polimi.tiw.controllers.authentication;

import it.polimi.tiw.exceptions.UsernameAlreadyUsedException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static it.polimi.tiw.utils.ControllerUtils.sendBadGateway;
import static it.polimi.tiw.utils.ControllerUtils.sendBadRequest;

@WebServlet("/register")
public class RegistrationController extends BaseAuthController {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Credentials credentials = getCredentials(req);

        if (credentials.areStringsInvalid()) {
            sendBadRequest(res, MISSING_CREDENTIAL_MESSAGE);
            return;
        }

        int registeredId;
        try {
            registeredId = userDAO.register(credentials.username, credentials.password);
        } catch (SQLException e) {
            sendBadGateway(res);
            return;
        } catch (UsernameAlreadyUsedException e) {
            res.sendRedirect(getRedirectURL("/login?msg=Username+already+taken"));
            return;
        }

        req.getSession().setAttribute("UserPk", registeredId);
        res.sendRedirect(getRedirectURL("/home"));
    }
}
