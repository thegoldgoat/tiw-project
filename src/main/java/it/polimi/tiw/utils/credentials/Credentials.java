package it.polimi.tiw.utils.credentials;

import it.polimi.tiw.utils.ControllerUtils;
import lombok.Getter;

public abstract class Credentials {
    @Getter protected final String username;
    @Getter protected final String password;

    protected Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean areStringsInvalid() {
        return ControllerUtils.stringInvalid(username) || ControllerUtils.stringInvalid(password);
    }
}
