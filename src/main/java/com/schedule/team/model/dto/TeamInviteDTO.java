package com.schedule.team.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schedule.team.model.TeamInviteStatus;
import com.schedule.team.model.dto.team.TeamShortDescriptionDTO;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime date;
    private TeamInviteStatus inviteStatus;
    private TeamShortDescriptionDTO team;
}
