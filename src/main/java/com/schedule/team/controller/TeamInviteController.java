package com.schedule.team.controller;

import com.schedule.team.model.GetTeamInviteCriteria;
import com.schedule.team.model.TeamInviteStatus;
import com.schedule.team.model.dto.team_invite.TeamInviteDTO;
import com.schedule.team.model.dto.team_invite.TeamInviteTeamDTO;
import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.TeamInvite;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.request.CreateTeamInviteRequest;
import com.schedule.team.model.request.PatchTeamInviteRequest;
import com.schedule.team.model.response.GetTeamInvitesResponse;
import com.schedule.team.service.jwt.ExtractClaimsFromRequestService;
import com.schedule.team.service.jwt.ExtractUserFromRequestService;
import com.schedule.team.service.team.GetTeamByIdService;
import com.schedule.team.service.team.JoinTeamService;
import com.schedule.team.service.team_invite.CreateTeamInviteService;
import com.schedule.team.service.team_invite.GetTeamInviteByIdService;
import com.schedule.team.service.team_invite.GetTeamInvitesByUserService;
import com.schedule.team.service.team_invite.UpdateTeamInviteService;
import com.schedule.team.service.user.GetUserByIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/team/invite")
@RequiredArgsConstructor
public class TeamInviteController {
    private final ExtractClaimsFromRequestService extractClaimsFromRequestService;
    private final GetTeamByIdService getTeamByIdService;
    private final ExtractUserFromRequestService extractUserFromRequestService;
    private final GetUserByIdService getUserByIdService;
    private final CreateTeamInviteService createTeamInviteService;
    private final GetTeamInvitesByUserService getTeamInvitesByUserService;
    private final GetTeamInviteByIdService getTeamInviteByIdService;
    private final UpdateTeamInviteService updateTeamInviteService;
    private final JoinTeamService joinTeamService;

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody CreateTeamInviteRequest createTeamInviteRequest,
            HttpServletRequest request
    ) {
        Team team = getTeamByIdService.get(createTeamInviteRequest.getTeamId());
        User inviting = extractUserFromRequestService.extract(request);

        List<User> invitedList = createTeamInviteRequest
                .getInvitedIds()
                .stream()
                .map(getUserByIdService::get)
                .toList();
        for (User invited : invitedList) {
            createTeamInviteService.create(
                    team,
                    inviting,
                    invited,
                    LocalDateTime.now()
            );
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<?> get(
            @RequestParam GetTeamInviteCriteria criteria,
            HttpServletRequest request
    ) {
        Long userId = extractClaimsFromRequestService.extract(request).getId();

        List<TeamInvite> teamInvites = getTeamInvitesByUserService.get(userId, criteria);

        return ResponseEntity.ok().body(
                new GetTeamInvitesResponse(
                        teamInvites
                                .stream()
                                .map(
                                        teamInvite -> new TeamInviteDTO(
                                                teamInvite.getId(),
                                                teamInvite.getInviting().getId(),
                                                teamInvite.getInvited().getId(),
                                                teamInvite.getDate(),
                                                teamInvite.getInviteStatus(),
                                                new TeamInviteTeamDTO(
                                                        teamInvite.getTeam().getId(),
                                                        teamInvite.getTeam().getName()
                                                )
                                        )
                                )
                                .toList()
                )
        );
    }

    @PatchMapping
    public ResponseEntity<?> patch(
            @RequestBody PatchTeamInviteRequest patchTeamInviteRequest
    ) {
        TeamInvite teamInvite = getTeamInviteByIdService.get(patchTeamInviteRequest.getId());
        // TODO: check if invited or inviting
        updateTeamInviteService.patch(
                teamInvite,
                patchTeamInviteRequest.getStatus()
        );

        if (TeamInviteStatus.ACCEPTED.equals(patchTeamInviteRequest.getStatus())) {
            joinTeamService.join(
                    teamInvite.getTeam(),
                    teamInvite.getInvited()
            );
        }

        return ResponseEntity.ok().build();
    }
}
