package com.schedule.team.service.jwt;

import javax.servlet.http.HttpServletRequest;

interface ExtractTokenService {
    String extract(HttpServletRequest request);
}
