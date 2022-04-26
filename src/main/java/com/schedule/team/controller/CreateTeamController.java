package com.schedule.team.controller;

import com.schedule.team.model.UserClaims;
import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.request.CreateTeamRequest;
import com.schedule.team.model.response.CreateTeamResponse;
import com.schedule.team.service.jwt.ExtractClaimsFromRequestService;
import com.schedule.team.service.team.CreateTeamService;
import com.schedule.team.service.user.GetUserByIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class CreateTeamController {
    private final ExtractClaimsFromRequestService extractClaimsFromRequestService;
    private final GetUserByIdService getUserByIdService;
    private final CreateTeamService createTeamService;

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody CreateTeamRequest createTeamRequest,
            HttpServletRequest request
    ) {
        UserClaims claims = extractClaimsFromRequestService.extract(request);
        User creator = getUserByIdService.get(claims.getId());
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
}
