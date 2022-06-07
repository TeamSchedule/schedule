package com.schedule.team.service.jwt;

import com.schedule.team.model.UserClaims;
import com.schedule.team.model.entity.User;
import com.schedule.team.service.user.GetUserByIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class ExtractClaimsFromRequestServiceImpl implements ExtractClaimsFromRequestService {
    private final ExtractClaimsFromTokenService extractClaimsFromTokenService;
    private final ExtractTokenService extractTokenService;
    private final GetUserByIdService getUserByIdService;

    @Override
    public User extractUser(HttpServletRequest request) {
        return getUserByIdService.get(extractClaims(request).getId());
    }

    @Override
    public UserClaims extractClaims(HttpServletRequest request) {
        return extractClaimsFromTokenService.extract(
                extractTokenService.extract(
                        request
                )
        );
    }
}
