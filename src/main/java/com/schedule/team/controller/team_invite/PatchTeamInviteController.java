package com.schedule.team.controller.team_invite;

import com.schedule.team.model.TeamInviteStatus;
import com.schedule.team.model.entity.TeamInvite;
import com.schedule.team.model.request.PatchTeamInviteRequest;
import com.schedule.team.service.team.JoinTeamService;
import com.schedule.team.service.team_invite.GetTeamInviteByIdService;
import com.schedule.team.service.team_invite.PatchTeamInviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team/invite")
@RequiredArgsConstructor
public class PatchTeamInviteController {
    private final PatchTeamInviteService patchTeamInviteService;
    private final JoinTeamService joinTeamService;
    private final GetTeamInviteByIdService getTeamInviteByIdService;

    @PatchMapping
    public ResponseEntity<?> patch(
            @RequestBody PatchTeamInviteRequest patchTeamInviteRequest
    ) {
        TeamInvite teamInvite = getTeamInviteByIdService
                .get(patchTeamInviteRequest.getId());
        // TODO: check if invited or inviting
        patchTeamInviteService.patch(
                teamInvite,
                patchTeamInviteRequest.getStatus()
        );

        if(TeamInviteStatus.ACCEPTED.equals(patchTeamInviteRequest.getStatus())) {
            joinTeamService.join(
                    teamInvite.getTeam(),
                    teamInvite.getInvited()
            );
        }

        return ResponseEntity.ok().build();
    }
}
