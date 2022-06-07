package com.schedule.team.controller;

import com.schedule.team.model.dto.team.TeamDTO;
import com.schedule.team.model.dto.team.TeamDescriptionDTO;
import com.schedule.team.model.entity.TeamColor;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.PublicTeam;
import com.schedule.team.model.entity.team.Team;
import com.schedule.team.model.request.CreateDefaultTeamRequest;
import com.schedule.team.model.request.CreateTeamRequest;
import com.schedule.team.model.request.UpdateTeamRequest;
import com.schedule.team.model.response.CreateTeamResponse;
import com.schedule.team.model.response.GetTeamByIdResponse;
import com.schedule.team.model.response.GetTeamsResponse;
import com.schedule.team.service.jwt.ExtractClaimsFromRequestService;
import com.schedule.team.service.team.community.*;
import com.schedule.team.service.team_color.TeamColorService;
import com.schedule.team.service.user.CreateUserService;
import com.schedule.team.validation.constraint.PublicTeamExistsByIdConstraint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {
    private final ExtractClaimsFromRequestService extractClaimsFromRequestService;
    private final CreatePublicTeamService createPublicTeamService;
    private final LeaveTeamService leaveTeamService;
    private final UpdateTeamService updateTeamService;
    private final CreateUserService createUserService;
    private final BuildTeamDescriptionDTOService buildTeamDescriptionDTOService;
    private final GetPublicTeamByIdService getPublicTeamByIdService;
    private final TeamColorService teamColorService;

    @PostMapping("/default")
    public ResponseEntity<?> createDefaultTeam(
            @RequestBody CreateDefaultTeamRequest createDefaultTeamRequest
    ) {
        createUserService.create(createDefaultTeamRequest.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping
    public ResponseEntity<CreateTeamResponse> create(
            @RequestBody CreateTeamRequest createTeamRequest,
            HttpServletRequest request
    ) {
        User creator = extractClaimsFromRequestService.extractUser(request);
        Team team = createPublicTeamService.create(
                createTeamRequest.getName(),
                LocalDate.now(),
                creator
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CreateTeamResponse(
                        team.getId()
                )
        );
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<GetTeamByIdResponse> get(
            @PublicTeamExistsByIdConstraint @PathVariable Long teamId,
            HttpServletRequest request
    ) {
        Long userId = extractClaimsFromRequestService.extractClaims(request).getId();
        TeamColor teamColor = teamColorService.getByUserIdAndTeamId(userId, teamId);
        PublicTeam team = teamColor.getTeam();

        return ResponseEntity.ok().body(
                new GetTeamByIdResponse(
                        new TeamDTO(
                                team.getId(),
                                team.getName(),
                                team.getCreationDate(),
                                team.getAdmin().getId(),
                                team.getTeamColors().stream().map(TeamColor::getUser).map(User::getId).toList(),
                                teamColor.getColor()
                        )
                )
        );
    }

    @GetMapping
    public ResponseEntity<GetTeamsResponse> get(HttpServletRequest request) {
        Long userId = extractClaimsFromRequestService.extractClaims(request).getId();
        List<TeamDescriptionDTO> teamDescriptionDTOS = teamColorService
                .getByUserId(userId)
                .stream()
                .map(teamColor -> buildTeamDescriptionDTOService.build(
                        teamColor.getTeam(),
                        teamColor.getColor()
                )).toList();
        return ResponseEntity.ok().body(
                new GetTeamsResponse(
                        teamDescriptionDTOS
                )
        );
    }

    @DeleteMapping("/{teamId}/user")
    public ResponseEntity<?> leave(
            @PublicTeamExistsByIdConstraint @PathVariable Long teamId,
            HttpServletRequest request
    ) {
        leaveTeamService.leave(
                getPublicTeamByIdService.get(teamId),
                extractClaimsFromRequestService.extractUser(request)
        );
        return ResponseEntity.noContent().build();
    }

    // TODO: security checks
    @PatchMapping("/{teamId}")
    public ResponseEntity<?> patch(
            @PublicTeamExistsByIdConstraint @PathVariable Long teamId,
            @RequestBody UpdateTeamRequest updateTeamRequest,
            HttpServletRequest request
    ) {
        PublicTeam team = getPublicTeamByIdService.get(teamId);

        if (updateTeamRequest.getNewName() != null) {
            updateTeamService.update(team, updateTeamRequest.getNewName());
        }

        if (updateTeamRequest.getColor() != null) {
            Long userId = extractClaimsFromRequestService.extractClaims(request).getId();
            teamColorService.update(
                    teamColorService.getByUserIdAndTeamId(userId, teamId),
                    updateTeamRequest.getColor()
            );
        }

        return ResponseEntity.ok().build();
    }
}
