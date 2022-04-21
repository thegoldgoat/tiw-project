package it.polimi.tiw.controllers.authentication;

import it.polimi.tiw.exceptions.InvalidCredentialsException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static it.polimi.tiw.utils.ControllerUtils.sendBadGateway;
import static it.polimi.tiw.utils.ControllerUtils.sendBadRequest;

@WebServlet("/login")
public class LoginController extends BaseAuthController {
    protected TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        super.init();

        templateEngine = getTemplateEngine();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (req.getSession().getAttribute("UserPk") != null) {
            res.sendRedirect(getRedirectURL("/home"));
        }

        String msg = req.getParameter("msg");

        WebContext ctx = createWebContext(req, res);

        ctx.setVariable("msg", msg);
        processTemplate("WEB-INF/login.html", templateEngine, ctx, res);
    }

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
            res.sendRedirect(getRedirectURL("/login?msg=Invalid+Credentials"));
            return;
        }

        req.getSession().setAttribute("UserPk", loggedId);
        res.sendRedirect(getRedirectURL("/home"));
    }
}
