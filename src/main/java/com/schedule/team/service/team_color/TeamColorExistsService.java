package com.schedule.team.service.team_color;

import java.util.List;

public interface TeamColorExistsService {
    boolean exists(
            Long teamId,
            List<Long> invitedIds
    );
}
