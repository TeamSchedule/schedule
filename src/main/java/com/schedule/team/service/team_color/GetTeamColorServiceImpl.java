package com.schedule.team.service.team_color;

import com.schedule.team.model.entity.TeamColor;
import com.schedule.team.repository.TeamColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTeamColorServiceImpl implements GetTeamColorService {
    private final TeamColorRepository teamColorRepository;

    @Override
    public TeamColor get(Long teamId, Long userId) {
        return teamColorRepository.findByTeamIdAndUserId(teamId, userId);
    }
}
