package com.schedule.team.service.jwt;

import com.schedule.team.model.entity.User;
import com.schedule.team.service.user.GetUserByIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class ExtractUserFromRequestServiceImpl implements ExtractUserFromRequestService {
    private final ExtractClaimsFromRequestService extractClaimsFromRequestService;
    private final GetUserByIdService getUserByIdService;

    @Autowired
    public ExtractUserFromRequestServiceImpl(
            @Qualifier("extractClaimsFromRequestServiceCreateUserIfAbsent")
                    ExtractClaimsFromRequestService extractClaimsFromRequestService,
            GetUserByIdService getUserByIdService
    ) {
        this.extractClaimsFromRequestService = extractClaimsFromRequestService;
        this.getUserByIdService = getUserByIdService;
    }

    @Override
    public User extract(HttpServletRequest request) {
        return getUserByIdService.get(extractClaimsFromRequestService.extract(request).getId());
    }
}
