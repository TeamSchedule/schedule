package com.schedule.team.service.team_invite;

import com.schedule.team.model.GetTeamInviteCriteria;
import com.schedule.team.model.TeamInviteStatus;
import com.schedule.team.model.entity.TeamInvite;
import com.schedule.team.repository.TeamInviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTeamInvitesServiceImpl implements GetTeamInvitesService {
    private final TeamInviteRepository teamInviteRepository;

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
}
