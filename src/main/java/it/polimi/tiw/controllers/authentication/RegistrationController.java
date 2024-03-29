package it.polimi.tiw.controllers.authentication;

import it.polimi.tiw.exceptions.InvalidEmailException;
import it.polimi.tiw.exceptions.MissingCredentialsException;
import it.polimi.tiw.exceptions.PasswordConfirmNotMatchException;
import it.polimi.tiw.exceptions.UsernameAlreadyUsedException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static it.polimi.tiw.utils.ControllerUtils.sendBadGateway;

@WebServlet("/register")
public class RegistrationController extends BaseAuthController {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            tryRegister(req);
            res.sendRedirect(getRedirectURL("/home"));
        } catch (MissingCredentialsException e) {
            res.sendRedirect(getRedirectURL("/login?msg=Missing+Credentials"));
        } catch (PasswordConfirmNotMatchException e) {
            res.sendRedirect(getRedirectURL("/login?msg=Password+and+confirm+must+match"));
        } catch (InvalidEmailException e) {
            res.sendRedirect(getRedirectURL("/login?msg=Invalid+Email"));
        } catch (UsernameAlreadyUsedException e) {
            res.sendRedirect(getRedirectURL("/login?msg=Username+already+taken"));
        } catch (SQLException e) {
            sendBadGateway(res);
        }
    }
}
