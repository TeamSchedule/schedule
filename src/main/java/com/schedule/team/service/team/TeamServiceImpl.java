package com.schedule.team.service.team;

import com.schedule.team.model.entity.team.Team;
import com.schedule.team.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;

    @Override
    public Team getById(Long id) {
        return teamRepository.getById(id);
    }

    @Override
    public List<Team> getListByIds(List<Long> teamsIds) {
        return teamRepository.findAllById(teamsIds);
    }
}
