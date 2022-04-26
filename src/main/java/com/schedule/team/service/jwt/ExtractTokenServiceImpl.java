package com.schedule.team.service.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class ExtractTokenServiceImpl implements ExtractTokenService {
    @Value("${app.jwt.tokenHeaderName}")
    private String tokenHeaderName;
    @Value("${app.jwt.tokenStart}")
    private String tokenStart;

    @Override
    public String extract(HttpServletRequest request) {
        return request
                .getHeader(tokenHeaderName)
                .substring(tokenStart.length());
    }
}
