package com.schedule.team.service.team_invite;

import com.schedule.team.model.GetTeamInviteCriteria;
import com.schedule.team.model.TeamInviteStatus;
import com.schedule.team.model.entity.TeamInvite;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.PublicTeam;
import com.schedule.team.repository.TeamInviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamInviteServiceImpl implements TeamInviteService {
    private final TeamInviteRepository teamInviteRepository;

    @Override
    public TeamInvite create(
            PublicTeam team,
            User inviting,
            User invited,
            LocalDateTime time
    ) {
        return teamInviteRepository.save(
                new TeamInvite(
                        team,
                        invited,
                        inviting,
                        time
                )
        );
    }

    @Override
    public TeamInvite getById(Long id) {
        return teamInviteRepository.getById(id);
    }

    @Override
    public List<TeamInvite> get(
            Long userId,
            GetTeamInviteCriteria criteria,
            TeamInviteStatus status,
            Long teamId
    ) {
        if (GetTeamInviteCriteria.INVITED.equals(criteria)) {
            return teamInviteRepository.getAllByInvitedIdAndTeamIdAndInviteStatus(
                    userId,
                    teamId,
                    status
            );
        } else {
            return teamInviteRepository.getAllByInvitingIdAndTeamIdAndInviteStatus(
                    userId,
                    teamId,
                    status
            );
        }
    }

    @Override
    public List<TeamInvite> get(
            Long userId,
            GetTeamInviteCriteria criteria,
            TeamInviteStatus status
    ) {
        if (GetTeamInviteCriteria.INVITED.equals(criteria)) {
            return teamInviteRepository.getAllByInvitedIdAndInviteStatus(
                    userId,
                    status
            );
        } else {
            return teamInviteRepository.getAllByInvitingIdAndInviteStatus(
                    userId,
                    status
            );
        }
    }

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

    @Override
    @Transactional
    public void update(
            TeamInvite teamInvite,
            TeamInviteStatus teamInviteStatus
    ) {
        teamInvite.setInviteStatus(teamInviteStatus);
    }
}
