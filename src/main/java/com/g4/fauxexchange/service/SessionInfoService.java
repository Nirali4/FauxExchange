package com.g4.fauxexchange.service.impl;

import java.util.List;
import javax.servlet.http.HttpSession;

import com.g4.fauxexchange.model.SessionInfo;

public interface SessionInfoService {
    public abstract SessionInfo createSession(String sid, String email);
    public abstract SessionInfo createSession(HttpSession session);
    public abstract SessionInfo resetSesssion();
}
