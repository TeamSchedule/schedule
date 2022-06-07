package com.schedule.team.service.team_color;

import com.schedule.team.model.entity.TeamColorKey;
import com.schedule.team.repository.TeamColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamColorExistsByTeamIdAndUserIdListServiceImpl implements TeamColorExistsByTeamIdAndUserIdListService {
    private final TeamColorRepository teamColorRepository;

    @Override
    public boolean exists(
            Long teamId,
            List<Long> userIds
    ) {
        List<TeamColorKey> teamColorKeys = userIds
                .stream()
                .map(
                        userId -> new TeamColorKey(teamId, userId)
                )
                .toList();
        return teamColorRepository.existsByTeamColorKeyIn(teamColorKeys);
    }
}
