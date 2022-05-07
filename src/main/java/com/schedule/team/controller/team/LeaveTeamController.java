package com.schedule.team.controller.team;

import com.schedule.team.service.jwt.ExtractClaimsFromRequestService;
import com.schedule.team.service.team.GetTeamByIdService;
import com.schedule.team.service.team.LeaveTeamService;
import com.schedule.team.service.user.GetUserByIdService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/team")
public class LeaveTeamController {
    private final LeaveTeamService leaveTeamService;
    private final ExtractClaimsFromRequestService extractClaimsFromRequestService;
    private final GetTeamByIdService getTeamByIdService;
    private final GetUserByIdService getUserByIdService;

    public LeaveTeamController(
            LeaveTeamService leaveTeamService,
            @Qualifier("extractClaimsFromRequestServiceImpl")
                    ExtractClaimsFromRequestService extractClaimsFromRequestService,
            GetTeamByIdService getTeamByIdService,
            GetUserByIdService getUserByIdService
    ) {
        this.leaveTeamService = leaveTeamService;
        this.extractClaimsFromRequestService = extractClaimsFromRequestService;
        this.getTeamByIdService = getTeamByIdService;
        this.getUserByIdService = getUserByIdService;
    }

    @DeleteMapping("/{teamId}/user")
    public ResponseEntity<?> leave(
            @PathVariable Long teamId,
            HttpServletRequest request
    ) {
        leaveTeamService.leave(
                getTeamByIdService.get(teamId),
                getUserByIdService.get(
                        extractClaimsFromRequestService.extract(
                                request
                        ).getId()
                )
        );
        return ResponseEntity.noContent().build();
    }
}
