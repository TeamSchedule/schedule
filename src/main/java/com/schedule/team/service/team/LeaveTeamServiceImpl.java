package com.schedule.team.service.team;

import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.User;
import com.schedule.team.repository.TeamColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class LeaveTeamServiceImpl implements LeaveTeamService {
    private final TeamColorRepository teamColorRepository;

    @Override
    @Transactional
    public void leave(Team team, User user) {
        teamColorRepository.deleteByTeamAndUser(team, user);
    }
}
