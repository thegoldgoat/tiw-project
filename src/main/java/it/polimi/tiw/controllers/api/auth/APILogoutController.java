package it.polimi.tiw.controllers.api.auth;

import it.polimi.tiw.controllers.BaseServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/api/logout")
public class APILogoutController extends BaseServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        res.setStatus(200);
    }
}
