package com.schedule.team.service.team;

import com.schedule.team.model.entity.team.DefaultTeam;
import com.schedule.team.repository.team.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CreateDefaultTeamServiceImpl implements CreateDefaultTeamService {
    private final TeamRepository teamRepository;
    private final String defaultTeamColor;

    @Autowired
    public CreateDefaultTeamServiceImpl(
            TeamRepository teamRepository,
            @Value("${app.team.color.default}")
                    String defaultTeamColor
    ) {
        this.teamRepository = teamRepository;
        this.defaultTeamColor = defaultTeamColor;
    }

    @Override
    public DefaultTeam create() {
        return teamRepository.save(new DefaultTeam(defaultTeamColor));
    }
}
