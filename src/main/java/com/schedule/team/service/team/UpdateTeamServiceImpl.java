package com.schedule.team.service.team;

import com.schedule.team.model.entity.team.PublicTeam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateTeamServiceImpl implements UpdateTeamService {
    @Override
    @Transactional
    public void update(PublicTeam team, String newName) {
        team.setName(newName);
    }
}
