package com.schedule.team.service.team;

import com.schedule.team.model.entity.Team;
import com.schedule.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTeamsListByIdServiceImpl implements GetTeamsListByIdService {
    private final TeamRepository teamRepository;

    @Override
    public List<Team> get(List<Long> teamsIds) {
        return teamRepository.findAllById(teamsIds);
    }
}
