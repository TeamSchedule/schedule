package com.schedule.team.service.team_color;

import javax.transaction.Transactional;

public interface UpdateTeamColorService {
    @Transactional
    void update(Long teamId, Long userId, String newColor);
}
