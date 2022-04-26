package com.schedule.team.controller;

import com.schedule.team.model.TeamDTO;
import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.TeamColor;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.response.GetTeamByIdResponse;
import com.schedule.team.service.jwt.ExtractClaimsFromRequestService;
import com.schedule.team.service.team.GetTeamByIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/team")
public class GetTeamByIdController {
    private final GetTeamByIdService getTeamByIdService;
    private final ExtractClaimsFromRequestService extractClaimsFromRequestService;

    @Autowired
    public GetTeamByIdController(
            GetTeamByIdService getTeamByIdService,
            @Qualifier("extractClaimsFromRequestServiceCreateUserIfAbsent")
                    ExtractClaimsFromRequestService extractClaimsFromRequestService
    ) {
        this.getTeamByIdService = getTeamByIdService;
        this.extractClaimsFromRequestService = extractClaimsFromRequestService;
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<GetTeamByIdResponse> get(
            @PathVariable Long teamId,
            HttpServletRequest request
    ) {
        Team team = getTeamByIdService.get(teamId);
        Long userId = extractClaimsFromRequestService
                .extract(request)
                .getId();

        List<User> members = new ArrayList<>();
        String color = null;
        for (TeamColor teamColor : team.getTeamColors()) {
            if (teamColor.getUser().getId().equals(userId)) {
                color = teamColor.getColor();
            }
            members.add(teamColor.getUser());
        }

        return ResponseEntity.ok().body(
                new GetTeamByIdResponse(
                        new TeamDTO(
                                team.getId(),
                                team.getName(),
                                team.getCreationDate(),
                                team.getAdmin(),
                                members
                        ),
                        color
                )
        );
    }
}
