package com.schedule.team.service.team.community;

import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.PublicTeam;
import com.schedule.team.service.team_color.CreateTeamColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class JoinTeamServiceImpl implements JoinTeamService {
    private final CreateTeamColorService createTeamColorService;

    @Override
    @Transactional
    public void join(PublicTeam team, User user) {
        createTeamColorService.create(team, user);
    }
}
