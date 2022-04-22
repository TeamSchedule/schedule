package com.schedule.team.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamColorKey implements Serializable {
    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "user_id")
    private Long userId;
}
