package com.schedule.team.service.jwt;

import javax.servlet.http.HttpServletRequest;

public interface ExtractTokenService {
    String extract(HttpServletRequest request);
}
