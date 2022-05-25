package com.schedule.team.service.jwt;

import com.schedule.team.model.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface ExtractUserFromRequestService {
    User extract(HttpServletRequest request);
}
