package com.schedule.team.controller.team_invite;

import com.schedule.team.model.GetTeamInviteCriteria;
import com.schedule.team.model.dto.team_invite.TeamInviteDTO;
import com.schedule.team.model.dto.team_invite.TeamInviteTeamDTO;
import com.schedule.team.model.entity.TeamInvite;
import com.schedule.team.model.response.GetTeamInvitesResponse;
import com.schedule.team.service.jwt.ExtractClaimsFromRequestService;
import com.schedule.team.service.team_invite.GetTeamInvitesByUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/team/invite")
public class GetTeamInvitesController {
    private final ExtractClaimsFromRequestService extractClaimsFromRequestService;
    private final GetTeamInvitesByUserService getTeamInvitesByUserService;

    @Autowired
    public GetTeamInvitesController(
            @Qualifier("extractClaimsFromRequestServiceCreateUserIfAbsent")
                    ExtractClaimsFromRequestService extractClaimsFromRequestService,
            GetTeamInvitesByUserService getTeamInvitesByUserService
    ) {
        this.extractClaimsFromRequestService = extractClaimsFromRequestService;
        this.getTeamInvitesByUserService = getTeamInvitesByUserService;
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
}
