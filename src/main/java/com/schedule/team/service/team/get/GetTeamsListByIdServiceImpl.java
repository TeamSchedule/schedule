package com.schedule.team.service.team.get;

import com.schedule.team.model.entity.team.Team;
import com.schedule.team.repository.team.TeamRepository;
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
