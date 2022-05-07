package com.schedule.team.model.dto.team_invite;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schedule.team.model.TeamInviteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamInviteDTO {
    private Long id;
    private Long invitingId;
    private Long invitedId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime date;
    private TeamInviteStatus inviteStatus;
    private TeamInviteTeamDTO team;
}
