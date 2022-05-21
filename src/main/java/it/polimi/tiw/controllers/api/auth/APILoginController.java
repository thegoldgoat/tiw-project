package it.polimi.tiw.controllers.api.auth;

import it.polimi.tiw.controllers.authentication.BaseAuthController;
import it.polimi.tiw.exceptions.InvalidCredentialsException;
import it.polimi.tiw.exceptions.MissingCredentialsException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static it.polimi.tiw.utils.ControllerUtils.*;

@WebServlet("/api/login")
public class APILoginController extends BaseAuthController {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            tryLogin(req);

            res.setStatus(HttpServletResponse.SC_OK);
        } catch (InvalidCredentialsException e) {
            sendNotFound(res, "Invalid Credentials");
        } catch (SQLException e) {
            sendBadGateway(res);
        } catch (MissingCredentialsException e) {
            sendBadRequest(res, MISSING_CREDENTIAL_MESSAGE);
        }
    }
}
