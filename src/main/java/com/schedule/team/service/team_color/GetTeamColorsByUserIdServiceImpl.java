package com.schedule.team.service.team_color;

import com.schedule.team.model.entity.TeamColor;
import com.schedule.team.repository.TeamColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTeamColorsByUserIdServiceImpl implements GetTeamColorsByUserIdService {
    private final TeamColorRepository teamColorRepository;

    @Override
    public List<TeamColor> get(Long userId) {
        return teamColorRepository.findAllByUserId(userId);
    }
}
