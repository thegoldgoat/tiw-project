package it.polimi.tiw.controllers.api.auth;

import it.polimi.tiw.controllers.authentication.BaseAuthController;
import it.polimi.tiw.exceptions.InvalidEmailException;
import it.polimi.tiw.exceptions.MissingCredentialsException;
import it.polimi.tiw.exceptions.PasswordConfirmNotMatchException;
import it.polimi.tiw.exceptions.UsernameAlreadyUsedException;
import it.polimi.tiw.utils.ControllerUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static it.polimi.tiw.utils.ControllerUtils.sendBadGateway;

@WebServlet("/api/register")
public class APIRegistrationController extends BaseAuthController {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            tryRegister(req);
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (MissingCredentialsException e) {
            ControllerUtils.sendBadRequest(res, "Missing Credentials");
        } catch (PasswordConfirmNotMatchException e) {
            ControllerUtils.sendBadRequest(res, "Password and Confirmation do not match");
        } catch (InvalidEmailException e) {
            ControllerUtils.sendBadRequest(res, "Invalid email format");
        } catch (UsernameAlreadyUsedException e) {
            ControllerUtils.sendBadRequest(res, "Username Already Taken");
        } catch (SQLException e) {
            sendBadGateway(res);
        }
    }
}
