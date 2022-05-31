package com.schedule.team.controller;

import com.schedule.team.model.GetTeamInviteCriteria;
import com.schedule.team.model.TeamInviteStatus;
import com.schedule.team.model.dto.TeamInviteDTO;
import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.TeamInvite;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.request.CreateTeamInviteRequest;
import com.schedule.team.model.request.PatchTeamInviteRequest;
import com.schedule.team.model.response.CreateTeamInviteResponse;
import com.schedule.team.model.response.GetTeamInvitesResponse;
import com.schedule.team.service.jwt.ExtractClaimsFromRequestService;
import com.schedule.team.service.jwt.ExtractUserFromRequestService;
import com.schedule.team.service.team.GetTeamByIdService;
import com.schedule.team.service.team.JoinTeamService;
import com.schedule.team.service.team_invite.*;
import com.schedule.team.service.user.GetUserByIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/team/invite")
@RequiredArgsConstructor
public class TeamInviteController {
    private final ExtractClaimsFromRequestService extractClaimsFromRequestService;
    private final GetTeamByIdService getTeamByIdService;
    private final ExtractUserFromRequestService extractUserFromRequestService;
    private final GetUserByIdService getUserByIdService;
    private final CreateTeamInviteService createTeamInviteService;
    private final GetTeamInviteByIdService getTeamInviteByIdService;
    private final UpdateTeamInviteService updateTeamInviteService;
    private final JoinTeamService joinTeamService;
    private final BuildTeamInviteDTOService buildTeamInviteDTOService;
    private final GetTeamInvitesService getTeamInvitesService;

    @PostMapping
    public ResponseEntity<CreateTeamInviteResponse> create(
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
        List<Long> ids = invitedList
                .stream()
                .map(
                        invited -> createTeamInviteService.create(
                                team,
                                inviting,
                                invited,
                                LocalDateTime.now()
                        )
                )
                .map(TeamInvite::getId).toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CreateTeamInviteResponse(
                        ids
                )
        );
    }

    @GetMapping
    public ResponseEntity<GetTeamInvitesResponse> get(
            @RequestParam(name = "criteria") GetTeamInviteCriteria criteria,
            @RequestParam(name = "status") TeamInviteStatus status,
            @RequestParam(name = "teamId", required = false) Optional<Long> teamId,
            HttpServletRequest request
    ) {
        Long userId = extractClaimsFromRequestService.extract(request).getId();
        List<TeamInvite> teamInvites;
        if (teamId.isPresent()) {
            teamInvites = getTeamInvitesService.get(userId, criteria, status, teamId.get());
        } else {
            teamInvites = getTeamInvitesService.get(userId, criteria, status);
        }
        
        List<TeamInviteDTO> teamInviteDTOS = teamInvites
                .stream()
                .map(buildTeamInviteDTOService::build)
                .toList();
        return ResponseEntity.ok().body(
                new GetTeamInvitesResponse(
                        teamInviteDTOS
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
