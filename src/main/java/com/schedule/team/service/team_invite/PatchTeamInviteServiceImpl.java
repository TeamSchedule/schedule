package com.schedule.team.service.team_invite;

import com.schedule.team.model.TeamInviteStatus;
import com.schedule.team.model.entity.TeamInvite;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PatchTeamInviteServiceImpl implements PatchTeamInviteService {
    @Override
    @Transactional
    public void patch(TeamInvite teamInvite, TeamInviteStatus teamInviteStatus) {
        teamInvite.setInviteStatus(teamInviteStatus);
    }
}
