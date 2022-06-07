package com.schedule.team.service.team_color;

import com.schedule.team.model.entity.TeamColor;
import com.schedule.team.model.entity.TeamColorKey;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.PublicTeam;
import com.schedule.team.repository.TeamColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamColorServiceImpl implements TeamColorService {
    private final TeamColorRepository teamColorRepository;
    @Value("${app.team.color.default}")
    private String defaultTeamColor;

    @Override
    public TeamColor create(PublicTeam team, User user) {
        return teamColorRepository.save(
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

    @Override
    public TeamColor getByUserIdAndTeamId(Long userId, Long teamId) {
        return teamColorRepository.findByUserIdAndTeamId(userId, teamId);
    }

    @Override
    public List<TeamColor> getByUserId(Long userId) {
        return teamColorRepository.findAllByUserId(userId);
    }

    @Override
    public boolean anyExistsByTeamIdAndUserIds(
            Long teamId,
            List<Long> userIds
    ) {
        List<TeamColorKey> teamColorKeys = userIds
                .stream()
                .map(
                        userId -> new TeamColorKey(teamId, userId)
                )
                .toList();
        return teamColorRepository.existsByTeamColorKeyIn(teamColorKeys);
    }

    @Override
    @Transactional
    public void update(TeamColor teamColor, String newColor) {
        teamColor.setColor(newColor);
    }

    @Override
    public void deleteByTeamAndUser(PublicTeam team, User user) {
        teamColorRepository.deleteByTeamAndUser(team, user);
    }
}
