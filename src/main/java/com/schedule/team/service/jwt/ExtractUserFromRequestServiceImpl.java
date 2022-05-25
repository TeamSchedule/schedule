package com.schedule.team.service.jwt;

import com.schedule.team.model.entity.User;
import com.schedule.team.service.user.GetUserByIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class ExtractUserFromRequestServiceImpl implements ExtractUserFromRequestService {
    private final ExtractClaimsFromRequestService extractClaimsFromRequestService;
    private final GetUserByIdService getUserByIdService;

    @Override
    public User extract(HttpServletRequest request) {
        return getUserByIdService.get(extractClaimsFromRequestService.extract(request).getId());
    }
}
