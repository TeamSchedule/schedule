package com.schedule.team.service.team;

import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.PublicTeam;
import com.schedule.team.repository.team.PublicTeamRepository;
import com.schedule.team.service.team_color.TeamColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PublicTeamServiceImpl implements PublicTeamService {
    private final PublicTeamRepository publicTeamRepository;
    private final TeamColorService teamColorService;

    @Override
    public PublicTeam create(String name, LocalDate creationDate, User user) {
        PublicTeam team = publicTeamRepository.save(
                new PublicTeam(
                        name,
                        creationDate,
                        user
                )
        );
        join(team, user);
        return team;
    }

    @Override
    @Transactional
    public void join(PublicTeam team, User user) {
        teamColorService.create(team, user);
    }

    @Override
    public PublicTeam getById(Long id) {
        return publicTeamRepository.getById(id);
    }

    @Override
    @Transactional
    public void leave(PublicTeam team, User user) {
        teamColorService.deleteByTeamAndUser(team, user);
    }

    @Override
    public boolean existsById(Long id) {
        return publicTeamRepository.existsById(id);
    }

    @Override
    @Transactional
    public void update(PublicTeam team, String newName) {
        team.setName(newName);
    }
}
