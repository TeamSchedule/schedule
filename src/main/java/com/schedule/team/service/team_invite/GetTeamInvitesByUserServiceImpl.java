package com.schedule.team.service.team_invite;

import com.schedule.team.model.GetTeamInviteCriteria;
import com.schedule.team.model.entity.TeamInvite;
import com.schedule.team.repository.TeamInviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTeamInvitesByUserServiceImpl implements GetTeamInvitesByUserService {
    private final TeamInviteRepository teamInviteRepository;

    @Override
    public List<TeamInvite> get(Long userId, GetTeamInviteCriteria getTeamInviteCriteria) {
        if (GetTeamInviteCriteria.INVITED.equals(getTeamInviteCriteria)) {
            return teamInviteRepository.getAllByInvitedId(userId);
        } else {
            return teamInviteRepository.getAllByInvitingId(userId);
        }
    }
}
