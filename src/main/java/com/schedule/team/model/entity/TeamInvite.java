package com.schedule.team.model.entity;

import com.schedule.team.model.TeamInviteStatus;
import com.schedule.team.model.entity.team.PublicTeam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TeamInvite {
    @Id
    @SequenceGenerator(
            name = "team_invite_sequence",
            sequenceName = "team_invite_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "team_invite_sequence"
    )
    private Long id;

    @ManyToOne
    private PublicTeam team;

    @ManyToOne
    private User invited;

    @ManyToOne
    private User inviting;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "invite_status")
    @Enumerated(value = EnumType.STRING)
    private TeamInviteStatus inviteStatus;

    public TeamInvite(
            PublicTeam team,
            User invited,
            User inviting,
            LocalDateTime date
    ) {
        this.team = team;
        this.invited = invited;
        this.inviting = inviting;
        this.date = date;
        this.inviteStatus = TeamInviteStatus.OPEN;
    }
}
