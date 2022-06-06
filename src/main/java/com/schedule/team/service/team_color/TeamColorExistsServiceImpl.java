package com.schedule.team.service.team_color;

import com.schedule.team.model.entity.TeamColorKey;
import com.schedule.team.repository.TeamColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamColorExistsServiceImpl implements TeamColorExistsService {
    private final TeamColorRepository teamColorRepository;

    @Override
    public boolean exists(
            Long teamId,
            List<Long> invitedIds
    ) {
        List<TeamColorKey> teamColorKeys = invitedIds
                .stream()
                .map(
                        invitedId -> new TeamColorKey(teamId, invitedId)
                )
                .toList();
        return teamColorRepository.existsByTeamColorKeyIn(teamColorKeys);
    }
}
