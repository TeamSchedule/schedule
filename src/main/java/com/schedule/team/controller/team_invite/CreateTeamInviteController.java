package com.schedule.team.controller.team_invite;

import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.request.CreateTeamInviteRequest;
import com.schedule.team.service.jwt.ExtractClaimsFromRequestService;
import com.schedule.team.service.team.GetTeamByIdService;
import com.schedule.team.service.team_invite.CreateTeamInviteService;
import com.schedule.team.service.user.GetUserByIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/team/invite")
public class CreateTeamInviteController {
    private final ExtractClaimsFromRequestService extractClaimsFromRequestService;
    private final GetTeamByIdService getTeamByIdService;
    private final GetUserByIdService getUserByIdService;
    private final CreateTeamInviteService createTeamInviteService;

    @Autowired
    public CreateTeamInviteController(
            @Qualifier("extractClaimsFromRequestServiceCreateUserIfAbsent")
                    ExtractClaimsFromRequestService extractClaimsFromRequestService,
            GetTeamByIdService getTeamByIdService,
            GetUserByIdService getUserByIdService,
            CreateTeamInviteService createTeamInviteService
    ) {
        this.extractClaimsFromRequestService = extractClaimsFromRequestService;
        this.getTeamByIdService = getTeamByIdService;
        this.getUserByIdService = getUserByIdService;
        this.createTeamInviteService = createTeamInviteService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody CreateTeamInviteRequest createTeamInviteRequest,
            HttpServletRequest request
    ) {
        Team team = getTeamByIdService.get(createTeamInviteRequest.getTeamId());
        User inviting = getUserByIdService.get(
                extractClaimsFromRequestService.extract(request).getId()
        );
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
}
