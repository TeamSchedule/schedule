package com.schedule.team.model.entity;

import com.schedule.team.model.entity.team.DefaultTeam;
import lombok.*;

import javax.persistence.*;

@Entity(name = "app_user")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private Long id;

    @OneToOne
    private DefaultTeam defaultTeam;
}
