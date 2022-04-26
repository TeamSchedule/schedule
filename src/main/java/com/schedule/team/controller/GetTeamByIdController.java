package com.schedule.team.controller;

import com.schedule.team.model.entity.TeamColor;
import com.schedule.team.model.response.GetTeamByIdResponse;
import com.schedule.team.service.jwt.ExtractClaimsFromRequestService;
import com.schedule.team.service.team_color.GetTeamColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class GetTeamByIdController {
    private final GetTeamColorService getTeamColorService;
    private final ExtractClaimsFromRequestService extractClaimsFromRequestService;

    @GetMapping("/{teamId}")
    public ResponseEntity<?> get(
            @PathVariable Long teamId,
            HttpServletRequest request
    ) {
        TeamColor teamColor = getTeamColorService.get(
                teamId,
                extractClaimsFromRequestService
                        .extract(request)
                        .getId()
        );
        return ResponseEntity.ok().body(
                new GetTeamByIdResponse(
                        teamColor.getTeam(),
                        teamColor.getColor()
                )
        );
    }
}
