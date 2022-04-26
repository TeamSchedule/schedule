package com.schedule.team.service.request;

import javax.servlet.http.HttpServletRequest;

public interface ExtractTokenService {
    String extract(HttpServletRequest request);
}
