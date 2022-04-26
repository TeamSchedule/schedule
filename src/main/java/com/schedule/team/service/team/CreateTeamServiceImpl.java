package com.schedule.team.service.team;

import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.User;
import com.schedule.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateTeamServiceImpl implements CreateTeamService {
    private final TeamRepository teamRepository;

    @Override
    public Team create(String name, LocalDate creationDate, User user) {
        return teamRepository.save(
                new Team(
                        name,
                        creationDate,
                        user
                )
        );
    }
}
