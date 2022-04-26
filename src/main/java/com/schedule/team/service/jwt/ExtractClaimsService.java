package com.schedule.team.service.jwt;

import com.schedule.team.model.UserClaims;

public interface ExtractClaimsService {
    UserClaims extract(String token);
}
