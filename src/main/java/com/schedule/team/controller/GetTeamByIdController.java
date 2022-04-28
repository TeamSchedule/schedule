package com.schedule.team.controller;

import com.schedule.team.model.dto.TeamDTO;
import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.TeamColor;
import com.schedule.team.model.response.GetTeamByIdResponse;
import com.schedule.team.service.jwt.ExtractClaimsFromRequestService;
import com.schedule.team.service.team_color.GetTeamColorByUserIdAndTeamIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/team")
public class GetTeamByIdController {
    private final GetTeamColorByUserIdAndTeamIdService getTeamColorByUserIdAndTeamIdService;
    private final ExtractClaimsFromRequestService extractClaimsFromRequestService;

    @Autowired
    public GetTeamByIdController(
            GetTeamColorByUserIdAndTeamIdService getTeamColorByUserIdAndTeamIdService,
            @Qualifier("extractClaimsFromRequestServiceCreateUserIfAbsent")
                    ExtractClaimsFromRequestService extractClaimsFromRequestService
    ) {
        this.getTeamColorByUserIdAndTeamIdService = getTeamColorByUserIdAndTeamIdService;
        this.extractClaimsFromRequestService = extractClaimsFromRequestService;
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<GetTeamByIdResponse> get(
            @PathVariable Long teamId,
            HttpServletRequest request
    ) {
        Long userId = extractClaimsFromRequestService.extract(request).getId();
        TeamColor teamColor = getTeamColorByUserIdAndTeamIdService.get(teamId, userId);
        Team team = teamColor.getTeam();

        return ResponseEntity.ok().body(
                new GetTeamByIdResponse(
                        new TeamDTO(
                                team.getId(),
                                team.getName(),
                                team.getCreationDate(),
                                team.getAdmin(),
                                team.getTeamColors().stream().map(TeamColor::getUser).toList(),
                                teamColor.getColor()
                        )
                )
        );
    }
}
