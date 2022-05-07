package com.schedule.team.service.team;

import com.schedule.team.model.entity.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateTeamServiceImpl implements UpdateTeamService {
    @Override
    @Transactional
    public void update(Team team, String newName) {
        team.setName(newName);
    }
}
