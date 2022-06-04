package com.schedule.team.filter;

import com.schedule.team.service.request.BuildRequestUriService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class RequestExecutionTimeFilter implements Filter {
    private final BuildRequestUriService buildRequestUriService;

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        String requestUri = buildRequestUriService.build((HttpServletRequest) servletRequest);
        log.info("START: " + requestUri);
        long startTime = System.currentTimeMillis();
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            log.info(
                    "END: " + requestUri
                            + " | STATUS: " + ((HttpServletResponse) servletResponse).getStatus()
                            + " | EXECUTION TIME: " + executionTime + " ms"
            );
        }
    }
}

