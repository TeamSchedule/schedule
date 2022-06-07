package com.schedule.team.service.team_color;

import com.schedule.team.model.entity.TeamColor;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.PublicTeam;

import javax.transaction.Transactional;
import java.util.List;

public interface TeamColorService {
    TeamColor create(PublicTeam team, User user);

    TeamColor getByUserIdAndTeamId(Long userId, Long teamId);

    List<TeamColor> getByUserId(Long userId);

    boolean anyExistsByTeamIdAndUserIds(
            Long teamId,
            List<Long> userIds
    );

    @Transactional
    void update(TeamColor teamColor, String newColor);

    void deleteByTeamAndUser(PublicTeam team, User user);
}
