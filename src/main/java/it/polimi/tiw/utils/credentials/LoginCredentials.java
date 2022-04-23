package it.polimi.tiw.utils.credentials;

import javax.servlet.http.HttpServletRequest;

public class LoginCredentials extends Credentials {
    public LoginCredentials(HttpServletRequest req) {
        super(req.getParameter("username"), req.getParameter("password"));
    }
}
