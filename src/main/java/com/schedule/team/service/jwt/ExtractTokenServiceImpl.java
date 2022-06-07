package com.schedule.team.service.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
class ExtractTokenServiceImpl implements ExtractTokenService {
    @Value("${app.jwt.token.headerName}")
    private String tokenHeaderName;
    @Value("${app.jwt.token.prefix}")
    private String tokenStart;

    @Override
    public String extract(HttpServletRequest request) {
        return request
                .getHeader(tokenHeaderName)
                .substring(tokenStart.length());
    }
}
