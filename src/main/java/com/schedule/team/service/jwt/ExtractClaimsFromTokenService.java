package com.schedule.team.service.jwt;

import com.schedule.team.model.UserClaims;

interface ExtractClaimsFromTokenService {
    UserClaims extract(String token);
}
