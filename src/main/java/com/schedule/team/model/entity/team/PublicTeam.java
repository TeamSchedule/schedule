package com.schedule.team.model.entity.team;

import com.schedule.team.model.entity.TeamColor;
import com.schedule.team.model.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "public_team")
@Getter
@Setter
@NoArgsConstructor
public class PublicTeam extends Team {
    @Column(name = "name")
    private String name;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @ManyToOne
    private User admin;

    @OneToMany(mappedBy = "team")
    private Set<TeamColor> teamColors = new HashSet<>();

    public PublicTeam(
            String name,
            LocalDate creationDate,
            User admin
    ) {
        this.name = name;
        this.creationDate = creationDate;
        this.admin = admin;
    }
}
