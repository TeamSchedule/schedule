package com.schedule.team.service.team_invite;

import com.schedule.team.model.TeamInviteStatus;
import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.TeamInvite;
import com.schedule.team.model.entity.User;
import com.schedule.team.repository.TeamInviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateTeamInviteServiceImpl implements CreateTeamInviteService {
    private final TeamInviteRepository teamInviteRepository;

    @Override
    public TeamInvite create(Team team, User inviting, User invited, LocalDateTime time) {
        return teamInviteRepository.save(
                new TeamInvite(
                        team,
                        invited,
                        inviting,
                        time,
                        TeamInviteStatus.OPEN
                )
        );
    }
}
