package com.schedule.team.model.entity.team;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "default_team")
@Getter
@Setter
@NoArgsConstructor
public class DefaultTeam extends Team {
    @Column(name = "color")
    private String color;

    public DefaultTeam(String color) {
        this.color = color;
    }
}
