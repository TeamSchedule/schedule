package com.schedule.team.service.team_invite;

import com.schedule.team.model.TeamInviteStatus;
import com.schedule.team.repository.TeamInviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class TeamInviteExistsServiceImpl implements TeamInviteExistsService {
    private final TeamInviteRepository teamInviteRepository;

    @Override
    public boolean exists(
            Long teamId,
            Collection<Long> invitedIds
    ) {
        return teamInviteRepository.existsByTeamIdAndInvitedIdInAndInviteStatus(
                teamId,
                invitedIds,
                TeamInviteStatus.OPEN
        );
    }
}
