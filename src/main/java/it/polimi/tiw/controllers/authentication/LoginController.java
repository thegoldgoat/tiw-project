package it.polimi.tiw.controllers.authentication;

import it.polimi.tiw.exceptions.InvalidCredentialsException;
import it.polimi.tiw.exceptions.MissingCredentialsException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static it.polimi.tiw.utils.ControllerUtils.sendBadGateway;

@WebServlet(urlPatterns = {"", "/login"})
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
        try {
            tryLogin(req);

            res.sendRedirect(getRedirectURL("/home"));
        } catch (InvalidCredentialsException e) {
            res.sendRedirect(getRedirectURL("/login?msg=Invalid+Credentials"));
        } catch (SQLException e) {
            sendBadGateway(res);
        } catch (MissingCredentialsException e) {
            res.sendRedirect(getRedirectURL("/login?msg=Missing+Credentials"));
        }
    }
}
