package com.schedule.team.controller.team;

import com.schedule.team.model.entity.Team;
import com.schedule.team.model.request.PatchTeamRequest;
import com.schedule.team.service.jwt.ExtractClaimsFromRequestService;
import com.schedule.team.service.team.GetTeamByIdService;
import com.schedule.team.service.team.UpdateTeamService;
import com.schedule.team.service.team_color.UpdateTeamColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/team")
public class PatchTeamController {
    private final ExtractClaimsFromRequestService extractClaimsFromRequestService;
    private final GetTeamByIdService getTeamByIdService;
    private final UpdateTeamService updateTeamService;
    private final UpdateTeamColorService updateTeamColorService;

    @Autowired
    public PatchTeamController(
            @Qualifier("extractClaimsFromRequestServiceImpl")
                    ExtractClaimsFromRequestService extractClaimsFromRequestService,
            GetTeamByIdService getTeamByIdService,
            UpdateTeamService updateTeamService,
            UpdateTeamColorService updateTeamColorService
    ) {
        this.extractClaimsFromRequestService = extractClaimsFromRequestService;
        this.getTeamByIdService = getTeamByIdService;
        this.updateTeamService = updateTeamService;
        this.updateTeamColorService = updateTeamColorService;
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
