package com.schedule.team.controller;

import com.schedule.team.model.dto.TeamDTO;
import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.TeamColor;
import com.schedule.team.model.response.GetTeamsResponse;
import com.schedule.team.service.jwt.ExtractClaimsFromRequestService;
import com.schedule.team.service.team_color.GetTeamColorsByUserIdService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/team")
public class GetTeamsController {
    private final ExtractClaimsFromRequestService extractClaimsFromRequestService;
    private final GetTeamColorsByUserIdService getTeamColorsByUserIdService;

    public GetTeamsController(
            @Qualifier("extractClaimsFromRequestServiceCreateUserIfAbsent")
                    ExtractClaimsFromRequestService extractClaimsFromRequestService,
            GetTeamColorsByUserIdService getTeamColorsByUserIdService
    ) {
        this.extractClaimsFromRequestService = extractClaimsFromRequestService;
        this.getTeamColorsByUserIdService = getTeamColorsByUserIdService;
    }

    @GetMapping
    public ResponseEntity<GetTeamsResponse> get(HttpServletRequest request) {
        Long userId = extractClaimsFromRequestService.extract(request).getId();
        List<TeamColor> teamColors = getTeamColorsByUserIdService.get(userId);
        List<TeamDTO> teamColorDTOS = teamColors
                .stream()
                .map(teamColor -> {
                    Team team = teamColor.getTeam();
                    return new TeamDTO(
                            team.getId(),
                            team.getName(),
                            team.getCreationDate(),
                            team.getAdmin(),
                            team.getTeamColors().stream().map(TeamColor::getUser).toList(),
                            teamColor.getColor()
                    );
                }).toList();
        return ResponseEntity.ok().body(
                new GetTeamsResponse(
                        teamColorDTOS
                )
        );
    }
}
