package it.polimi.tiw.controllers.authentication;

import it.polimi.tiw.exceptions.InvalidCredentialsException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static it.polimi.tiw.utils.ControllerUtils.sendBadGateway;
import static it.polimi.tiw.utils.ControllerUtils.sendBadRequest;

@WebServlet("/login")
public class LoginController extends BaseAuthController {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Credentials credentials = getCredentials(req);

        if (credentials.areStringsInvalid()) {
            sendBadRequest(res, MISSING_CREDENTIAL_MESSAGE);
            return;
        }

        int loggedId;
        try {
            loggedId = userDAO.login(credentials.username, credentials.password);
        } catch (SQLException e) {
            sendBadGateway(res);
            return;
        } catch (InvalidCredentialsException e) {
            res.sendRedirect(getRedirectURL("/index?msg=Invalid Credentials"));
            return;
        }

        req.getSession().setAttribute("UserPk", loggedId);
        res.sendRedirect("/home");
    }
}
