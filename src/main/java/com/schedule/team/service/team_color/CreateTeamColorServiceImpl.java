package com.schedule.team.service.team_color;

import com.schedule.team.model.entity.TeamColor;
import com.schedule.team.model.entity.TeamColorKey;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.PublicTeam;
import com.schedule.team.repository.TeamColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTeamColorServiceImpl implements CreateTeamColorService {
    private final TeamColorRepository teamColorRepository;
    @Value("${app.team.color.default}")
    private String defaultTeamColor;

    @Override
    public void create(PublicTeam team, User user) {
        teamColorRepository.save(
                new TeamColor(
                        new TeamColorKey(
                                team.getId(),
                                user.getId()
                        ),
                        team,
                        user,
                        defaultTeamColor
                )
        );
    }
}
