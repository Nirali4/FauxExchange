package com.g4.fauxexchange.service.impl;

public interface SecurityService {
    public abstract String findLoggedInUsername();
    public abstract void autoLogin(String email, String password);
}
