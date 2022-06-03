package com.schedule.team.model.entity;

import com.schedule.team.model.entity.team.PublicTeam;
import com.schedule.team.model.entity.team.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamColor {
    @EmbeddedId
    private TeamColorKey teamColorKey;

    @ManyToOne
    @MapsId("teamId")
    @JoinColumn(name = "team_id")
    private PublicTeam team;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "color")
    private String color;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TeamColor teamColor)) {
            return false;
        }
        return teamColorKey.equals(teamColor.getTeamColorKey());
    }
}
