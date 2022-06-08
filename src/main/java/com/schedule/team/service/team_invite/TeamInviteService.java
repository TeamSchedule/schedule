package com.schedule.team.service.team_invite;

import com.schedule.team.model.GetTeamInviteCriteria;
import com.schedule.team.model.TeamInviteStatus;
import com.schedule.team.model.entity.TeamInvite;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.PublicTeam;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface TeamInviteService {
    TeamInvite create(
            PublicTeam team,
            User inviting,
            User invited,
            LocalDateTime time
    );

    TeamInvite getById(Long id);

    List<TeamInvite> get(
            Long userId,
            GetTeamInviteCriteria criteria,
            TeamInviteStatus status,
            Long teamId
    );

    List<TeamInvite> get(
            Long userId,
            GetTeamInviteCriteria criteria,
            TeamInviteStatus status
    );

    boolean exists(
            Long teamId,
            Collection<Long> invitedIds
    );

    @Transactional
    void update(
            TeamInvite teamInvite,
            TeamInviteStatus teamInviteStatus
    );
}
