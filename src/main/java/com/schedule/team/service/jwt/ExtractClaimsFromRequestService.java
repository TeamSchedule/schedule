package com.schedule.team.service.jwt;

import com.schedule.team.model.UserClaims;
import com.schedule.team.model.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface ExtractClaimsFromRequestService {
    User extractUser(HttpServletRequest request);
    UserClaims extractClaims(HttpServletRequest request);
}
