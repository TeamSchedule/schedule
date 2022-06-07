package com.schedule.team.service.team.community;

import com.schedule.team.model.entity.team.PublicTeam;
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
    public void leave(PublicTeam team, User user) {
        teamColorRepository.deleteByTeamAndUser(team, user);
    }
}
