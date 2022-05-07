package com.schedule.team.service.team_color;

import com.schedule.team.model.entity.TeamColor;
import com.schedule.team.repository.TeamColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateTeamColorServiceImpl implements UpdateTeamColorService {
    private final TeamColorRepository teamColorRepository;

    @Override
    @Transactional
    public void update(Long teamId, Long userId, String newColor) {
        TeamColor teamColor = teamColorRepository.findByUserIdAndTeamId(
                userId,
                teamId
        );
        teamColor.setColor(newColor);
    }
}
