package it.polimi.tiw.utils.credentials;

import it.polimi.tiw.utils.ControllerUtils;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

public class RegistrationCredentials extends LoginCredentials {
    @Getter private final String confirmPassword;
    @Getter private final String email;

    public RegistrationCredentials(HttpServletRequest req) {
        super(req);

        confirmPassword = req.getParameter("passwordconfirm");
        email = req.getParameter("email");
    }

    @Override
    public boolean areStringsInvalid() {
        return super.areStringsInvalid()
                || ControllerUtils.stringInvalid(email)
                || ControllerUtils.stringInvalid(confirmPassword);
    }

    public boolean passwordAndConfirmMatch() {
        return password.endsWith(confirmPassword);
    }
}
