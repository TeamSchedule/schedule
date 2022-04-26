package com.schedule.team.service.jwt;

import com.schedule.team.model.UserClaims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class ExtractClaimsFromRequestServiceImpl implements ExtractClaimsFromRequestService {
    private final ExtractClaimsFromTokenService extractClaimsFromTokenService;
    private final ExtractTokenService extractTokenService;

    @Override
    public UserClaims extract(HttpServletRequest request) {
        return extractClaimsFromTokenService.extract(
                extractTokenService.extract(
                        request
                )
        );
    }
}
