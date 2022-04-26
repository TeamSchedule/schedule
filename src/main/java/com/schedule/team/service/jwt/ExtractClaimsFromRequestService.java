package com.schedule.team.service.jwt;

import com.schedule.team.model.UserClaims;

import javax.servlet.http.HttpServletRequest;

public interface ExtractClaimsFromRequestService {
    UserClaims extract(HttpServletRequest request);
}
