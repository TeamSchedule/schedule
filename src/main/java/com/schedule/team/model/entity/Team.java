package com.schedule.team.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Team {
    @Id
    @SequenceGenerator(
            name = "team_sequence",
            sequenceName = "team_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "team_sequence"
    )
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @ManyToOne
    private User admin;

    @OneToMany(mappedBy = "team")
    private Set<TeamColor> teamColors;

    public Team(String name, LocalDate creationDate, User admin) {
        this.name = name;
        this.creationDate = creationDate;
        this.admin = admin;
        this.teamColors = new HashSet<>();
    }
}
