package com.schedule.team.service.team_invite;

import com.schedule.team.model.entity.TeamInvite;
import com.schedule.team.repository.TeamInviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTeamInviteByIdServiceImpl implements GetTeamInviteByIdService {
    private final TeamInviteRepository teamInviteRepository;

    @Override
    public TeamInvite get(Long id) {
        return teamInviteRepository.getById(id);
    }
}
