package com.schedule.team.service.team_color;

import java.util.List;

public interface TeamColorExistsByTeamIdAndUserIdListService {
    boolean exists(
            Long teamId,
            List<Long> userIds
    );
}
