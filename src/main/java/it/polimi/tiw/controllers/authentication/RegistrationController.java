package it.polimi.tiw.controllers.authentication;

import it.polimi.tiw.exceptions.InvalidEmailException;
import it.polimi.tiw.exceptions.UsernameAlreadyUsedException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static it.polimi.tiw.utils.ControllerUtils.*;

@WebServlet("/register")
public class RegistrationController extends BaseAuthController {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Credentials credentials = getCredentials(req);
        String email = req.getParameter("email");

        if (credentials.areStringsInvalid() || stringInvalid(email)) {
            sendBadRequest(res, MISSING_CREDENTIAL_MESSAGE);
            return;
        }

        int registeredId;
        try {
            registeredId = userDAO.register(email, credentials.username, credentials.password);
        } catch (SQLException e) {
            sendBadGateway(res);
            return;
        } catch (UsernameAlreadyUsedException e) {
            res.sendRedirect(getRedirectURL("/login?msg=Username+already+taken"));
            return;
        } catch (InvalidEmailException e) {
            res.sendRedirect(getRedirectURL("/login?msg=Invalid+Email"));
            return;
        }

        req.getSession().setAttribute("UserPk", registeredId);
        res.sendRedirect(getRedirectURL("/home"));
    }
}
