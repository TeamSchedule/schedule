package com.schedule.team.service.team.get;

import com.schedule.team.model.entity.team.Team;
import com.schedule.team.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTeamByIdServiceImpl implements GetTeamByIdService {
    private final TeamRepository teamRepository;

    @Override
    public Team get(Long id) {
        return teamRepository.getById(id);
    }
}
