package com.juancarlos.ryclibros.web.rest.errors;

public class LoginAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public LoginAlreadyUsedException() {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "El nombre de inicio de sesión ya se usó!", "userManagement", "userexists");
    }
}
