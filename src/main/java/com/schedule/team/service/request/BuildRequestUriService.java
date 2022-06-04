package com.schedule.team.service.request;

import javax.servlet.http.HttpServletRequest;

public interface BuildRequestUriService {
    String build(HttpServletRequest request);
}
