package com.schedule.team.service.request;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class BuildRequestUriServiceImpl implements BuildRequestUriService {
    @Override
    public String build(HttpServletRequest request) {
        return request.getMethod() +
                ": " +
                request.getRequestURI() +
                (request.getQueryString() != null ? "?" + request.getQueryString() : "");
    }
}
