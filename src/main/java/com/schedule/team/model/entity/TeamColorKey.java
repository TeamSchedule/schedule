package com.schedule.team.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamColorKey implements Serializable {
    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "user_id")
    private Long userId;
}
