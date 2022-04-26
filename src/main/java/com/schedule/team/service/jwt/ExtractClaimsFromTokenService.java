package com.schedule.team.service.jwt;

import com.schedule.team.model.UserClaims;

public interface ExtractClaimsFromTokenService {
    UserClaims extract(String token);
}
