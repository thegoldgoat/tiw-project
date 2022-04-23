package it.polimi.tiw.controllers.authentication;

import it.polimi.tiw.exceptions.InvalidEmailException;
import it.polimi.tiw.exceptions.UsernameAlreadyUsedException;
import it.polimi.tiw.utils.credentials.RegistrationCredentials;

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
        RegistrationCredentials credentials = new RegistrationCredentials(req);

        if (credentials.areStringsInvalid()) {
            sendBadRequest(res, MISSING_CREDENTIAL_MESSAGE);
            return;
        }

        if (!credentials.passwordAndConfirmMatch()) {
            res.sendRedirect(getRedirectURL("/login?msg=Password+and+confirm+must+match"));
            return;
        }

        int registeredId;
        try {
            registeredId =
                    userDAO.register(
                            credentials.getEmail(),
                            credentials.getUsername(),
                            credentials.getPassword());
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
