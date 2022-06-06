package com.schedule.team.service.team_invite;

import java.util.Collection;

public interface TeamInviteExistsService {
    boolean exists(
            Long teamId,
            Collection<Long> invitedIds
    );
}
