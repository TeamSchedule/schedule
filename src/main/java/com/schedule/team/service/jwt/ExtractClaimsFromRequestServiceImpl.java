package com.schedule.team.service.jwt;

import com.schedule.team.model.UserClaims;
import com.schedule.team.service.user.CreateUserService;
import com.schedule.team.service.user.UserExistsByIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class ExtractClaimsFromRequestServiceImpl implements ExtractClaimsFromRequestService {
    private final ExtractClaimsFromTokenService extractClaimsFromTokenService;
    private final ExtractTokenService extractTokenService;
    private final UserExistsByIdService userExistsByIdService;
    private final CreateUserService createUserService;

    @Override
    public UserClaims extract(HttpServletRequest request) {
        UserClaims claims = extractClaimsFromTokenService.extract(
                extractTokenService.extract(
                        request
                )
        );
        // TODO: remove
        if (!userExistsByIdService.exists(claims.getId())) {
            createUserService.create(claims.getId());
        }
        return claims;
    }
}
