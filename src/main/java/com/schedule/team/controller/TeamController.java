package com.schedule.team.controller;

import com.schedule.team.model.dto.TeamDTO;
import com.schedule.team.model.dto.TeamDescriptionDTO;
import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.TeamColor;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.request.CreateTeamRequest;
import com.schedule.team.model.request.PatchTeamRequest;
import com.schedule.team.model.response.CreateTeamResponse;
import com.schedule.team.model.response.GetTeamByIdResponse;
import com.schedule.team.model.response.GetTeamsResponse;
import com.schedule.team.service.jwt.ExtractClaimsFromRequestService;
import com.schedule.team.service.jwt.ExtractUserFromRequestService;
import com.schedule.team.service.team.CreateTeamService;
import com.schedule.team.service.team.GetTeamByIdService;
import com.schedule.team.service.team.LeaveTeamService;
import com.schedule.team.service.team.UpdateTeamService;
import com.schedule.team.service.team_color.GetTeamColorByUserIdAndTeamIdService;
import com.schedule.team.service.team_color.GetTeamColorsByUserIdService;
import com.schedule.team.service.team_color.UpdateTeamColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {
    private final ExtractClaimsFromRequestService extractClaimsFromRequestService;
    private final GetTeamColorsByUserIdService getTeamColorsByUserIdService;
    private final GetTeamColorByUserIdAndTeamIdService getTeamColorByUserIdAndTeamIdService;
    private final ExtractUserFromRequestService extractUserFromRequestService;
    private final CreateTeamService createTeamService;
    private final LeaveTeamService leaveTeamService;
    private final GetTeamByIdService getTeamByIdService;
    private final UpdateTeamService updateTeamService;
    private final UpdateTeamColorService updateTeamColorService;

    @PostMapping
    public ResponseEntity<CreateTeamResponse> create(
            @RequestBody CreateTeamRequest createTeamRequest,
            HttpServletRequest request
    ) {
        User creator = extractUserFromRequestService.extract(request);
        Team team = createTeamService.create(
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
            @PathVariable Long teamId,
            HttpServletRequest request
    ) {
        Long userId = extractUserFromRequestService.extract(request).getId();
        TeamColor teamColor = getTeamColorByUserIdAndTeamIdService.get(userId, teamId);
        Team team = teamColor.getTeam();

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
        Long userId = extractClaimsFromRequestService.extract(request).getId();
        List<TeamColor> teamColors = getTeamColorsByUserIdService.get(userId);

        List<TeamDescriptionDTO> teamDescriptionDTOS = teamColors
                .stream()
                .map(teamColor -> new TeamDescriptionDTO(
                        teamColor.getTeam().getId(),
                        teamColor.getTeam().getName(),
                        teamColor.getTeam().getCreationDate(),
                        teamColor.getTeam().getAdmin(),
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
            @PathVariable Long teamId,
            HttpServletRequest request
    ) {
        leaveTeamService.leave(
                getTeamByIdService.get(teamId),
                extractUserFromRequestService.extract(request)
        );
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{teamId}")
    public ResponseEntity<?> patch(
            @PathVariable Long teamId,
            @RequestBody PatchTeamRequest patchTeamRequest,
            HttpServletRequest request
    ) {
        Team team = getTeamByIdService.get(teamId);

        if (patchTeamRequest.getNewName() != null) {
            updateTeamService.update(team, patchTeamRequest.getNewName());
        }

        if (patchTeamRequest.getColor() != null) {
            Long userId = extractClaimsFromRequestService.extract(request).getId();
            updateTeamColorService.update(teamId, userId, patchTeamRequest.getColor());
        }

        return ResponseEntity.ok().build();
    }
}
