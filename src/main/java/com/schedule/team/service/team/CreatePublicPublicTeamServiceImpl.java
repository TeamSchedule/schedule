package com.schedule.team.service.team;

import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.PublicTeam;
import com.schedule.team.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreatePublicPublicTeamServiceImpl implements CreatePublicTeamService {
    private final TeamRepository teamRepository;
    private final JoinTeamService joinTeamService;

    @Override
    public PublicTeam create(String name, LocalDate creationDate, User user) {
        PublicTeam team = teamRepository.save(
                new PublicTeam(
                        name,
                        creationDate,
                        user
                )
        );
        joinTeamService.join(team, user);
        return team;
    }
}
