package com.schedule.team.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedule.team.IntegrationTest;
import com.schedule.team.model.GetTeamInviteCriteria;
import com.schedule.team.model.TeamInviteStatus;
import com.schedule.team.model.dto.TeamInviteDTO;
import com.schedule.team.model.entity.TeamInvite;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.PublicTeam;
import com.schedule.team.model.request.CreateTeamInviteRequest;
import com.schedule.team.model.request.UpdateTeamInviteRequest;
import com.schedule.team.model.response.CreateTeamInviteResponse;
import com.schedule.team.model.response.DefaultErrorResponse;
import com.schedule.team.model.response.GetTeamInvitesResponse;
import com.schedule.team.repository.TeamColorRepository;
import com.schedule.team.repository.TeamInviteRepository;
import com.schedule.team.repository.team.TeamRepository;
import com.schedule.team.service.team.community.JoinTeamService;
import com.schedule.team.service.team_invite.CreateTeamInviteService;
import com.schedule.team.service.user.CreateUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class TeamInviteControllerTest extends IntegrationTest {
    private final MockMvc mockMvc;
    private final TeamInviteRepository teamInviteRepository;
    private final ObjectMapper objectMapper;
    private final TeamRepository teamRepository;
    private final TeamColorRepository teamColorRepository;
    private final String tokenHeaderName;
    private final String tokenValue;
    private final CreateUserService createUserService;
    private final JoinTeamService joinTeamService;
    private final CreateTeamInviteService createTeamInviteService;

    @Autowired
    public TeamInviteControllerTest(
            MockMvc mockMvc,
            TeamInviteRepository teamInviteRepository,
            ObjectMapper objectMapper,
            TeamRepository teamRepository,
            TeamColorRepository teamColorRepository,
            @Value("${app.jwt.token.headerName}")
                    String tokenHeaderName,
            @Value("${app.jwt.token.test}")
                    String tokenValue,
            CreateUserService createUserService,
            JoinTeamService joinTeamService,
            CreateTeamInviteService createTeamInviteService
    ) {
        this.mockMvc = mockMvc;
        this.teamInviteRepository = teamInviteRepository;
        this.objectMapper = objectMapper;
        this.teamRepository = teamRepository;
        this.teamColorRepository = teamColorRepository;
        this.tokenHeaderName = tokenHeaderName;
        this.tokenValue = tokenValue;
        this.createUserService = createUserService;
        this.joinTeamService = joinTeamService;
        this.createTeamInviteService = createTeamInviteService;
    }

    @AfterEach
    public void afterEach() {
        clearDb();
    }

    @Test
    void createTeamInviteTest() throws Exception {
        User inviting = createUserService.create(1L);
        PublicTeam team = teamRepository.save(new PublicTeam(
                "team", LocalDate.now(), inviting
        ));

        User invited = createUserService.create(2L);
        CreateTeamInviteRequest createTeamInviteRequest = new CreateTeamInviteRequest(
                team.getId(),
                List.of(invited.getId())
        );
        String requestBody = objectMapper.writeValueAsString(createTeamInviteRequest);

        String response = mockMvc
                .perform(
                        post("/team/invite")
                                .contentType(APPLICATION_JSON)
                                .content(requestBody)
                                .header(tokenHeaderName, tokenValue)
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CreateTeamInviteResponse createTeamInviteResponse = objectMapper.readValue(
                response,
                CreateTeamInviteResponse.class
        );

        Assertions.assertEquals(1, createTeamInviteResponse.getIds().size());

        TeamInvite teamInvite = teamInviteRepository.findById(createTeamInviteResponse.getIds().get(0)).get();
        Assertions.assertEquals(team.getId(), teamInvite.getTeam().getId());
        Assertions.assertEquals(inviting.getId(), teamInvite.getInviting().getId());
        Assertions.assertEquals(invited.getId(), teamInvite.getInvited().getId());
    }

    @Test
    void updateTeamInviteAcceptedTest() throws Exception {
        User inviting = createUserService.create(1L);
        PublicTeam team = teamRepository.save(new PublicTeam(
                "team", LocalDate.now(), inviting
        ));

        User invited = createUserService.create(2L);
        TeamInvite teamInvite = teamInviteRepository.save(new TeamInvite(
                team,
                invited,
                inviting,
                LocalDateTime.now()
        ));

        UpdateTeamInviteRequest updateTeamInviteRequest = new UpdateTeamInviteRequest(TeamInviteStatus.ACCEPTED);
        String requestBody = objectMapper.writeValueAsString(updateTeamInviteRequest);

        mockMvc
                .perform(
                        patch("/team/invite/" + teamInvite.getId())
                                .header(tokenHeaderName, tokenValue)
                                .contentType(APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk());

        TeamInvite updatedTeamInvite = teamInviteRepository.findById(teamInvite.getId()).get();
        Assertions.assertEquals(TeamInviteStatus.ACCEPTED, updatedTeamInvite.getInviteStatus());
        Assertions.assertTrue(teamColorRepository.existsByTeamAndUser(team, invited));
    }

    @Test
    void getOpenTeamInvitesTest() throws Exception {
        User inviting = createUserService.create(1L);
        User invited = createUserService.create(2L);

        PublicTeam firstTeam = teamRepository.save(new PublicTeam("test", LocalDate.of(10, 10, 10), inviting));
        PublicTeam secondTeam = teamRepository.save(new PublicTeam("test2", LocalDate.of(10, 10, 10), inviting));
        TeamInvite firstTeamInvite = teamInviteRepository.save(new TeamInvite(
                firstTeam, invited, inviting, LocalDateTime.now()
        ));
        TeamInvite secondTeamInvite = teamInviteRepository.save(new TeamInvite(
                secondTeam, invited, inviting, LocalDateTime.now()
        ));

        String response = mockMvc
                .perform(
                        get("/team/invite/")
                                .header(tokenHeaderName, tokenValue)
                                .queryParam("criteria", GetTeamInviteCriteria.INVITING.toString())
                                .queryParam("status", TeamInviteStatus.OPEN.toString())
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        GetTeamInvitesResponse getTeamInvitesResponse = objectMapper.readValue(response, GetTeamInvitesResponse.class);

        Assertions.assertEquals(2, getTeamInvitesResponse.getTeamInvites().size());

        TeamInviteDTO firstTeamInviteDTO = getTeamInvitesResponse.getTeamInvites().get(0);
        Assertions.assertEquals(firstTeamInvite.getId(), firstTeamInviteDTO.getId());
        Assertions.assertEquals(firstTeam.getId(), firstTeamInviteDTO.getTeam().getId());
        Assertions.assertEquals(firstTeam.getName(), firstTeamInviteDTO.getTeam().getName());
        Assertions.assertEquals(TeamInviteStatus.OPEN, firstTeamInviteDTO.getInviteStatus());
        Assertions.assertEquals(invited.getId(), firstTeamInviteDTO.getInvitedId());
        Assertions.assertEquals(inviting.getId(), firstTeamInviteDTO.getInvitingId());

        TeamInviteDTO secondTeamInviteDTO = getTeamInvitesResponse.getTeamInvites().get(1);
        Assertions.assertEquals(secondTeamInvite.getId(), secondTeamInviteDTO.getId());
        Assertions.assertEquals(secondTeam.getId(), secondTeamInviteDTO.getTeam().getId());
        Assertions.assertEquals(secondTeam.getName(), secondTeamInviteDTO.getTeam().getName());
        Assertions.assertEquals(TeamInviteStatus.OPEN, secondTeamInviteDTO.getInviteStatus());
        Assertions.assertEquals(invited.getId(), secondTeamInviteDTO.getInvitedId());
        Assertions.assertEquals(inviting.getId(), secondTeamInviteDTO.getInvitingId());
    }

    @Test
    void getOpenTeamInviteFromTeamTest() throws Exception {
        User invited = createUserService.create(1L);
        User inviting = createUserService.create(1L);

        PublicTeam firstTeam = teamRepository.save(new PublicTeam("test", LocalDate.of(10, 10, 10), inviting));
        PublicTeam secondTeam = teamRepository.save(new PublicTeam("test2", LocalDate.of(10, 10, 10), inviting));
        TeamInvite firstTeamInvite = teamInviteRepository.save(new TeamInvite(
                firstTeam, invited, inviting, LocalDateTime.now()
        ));
        teamInviteRepository.save(new TeamInvite(
                secondTeam, invited, inviting, LocalDateTime.now()
        ));

        String response = mockMvc
                .perform(
                        get("/team/invite/")
                                .header(tokenHeaderName, tokenValue)
                                .queryParam("criteria", GetTeamInviteCriteria.INVITED.toString())
                                .queryParam("status", TeamInviteStatus.OPEN.toString())
                                .queryParam("teamId", String.valueOf(firstTeam.getId()))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GetTeamInvitesResponse getTeamInvitesResponse = objectMapper.readValue(response, GetTeamInvitesResponse.class);
        Assertions.assertEquals(1, getTeamInvitesResponse.getTeamInvites().size());

        TeamInviteDTO firstTeamInviteDTO = getTeamInvitesResponse.getTeamInvites().get(0);
        Assertions.assertEquals(firstTeamInvite.getId(), firstTeamInviteDTO.getId());
        Assertions.assertEquals(firstTeam.getId(), firstTeamInviteDTO.getTeam().getId());
        Assertions.assertEquals(firstTeam.getName(), firstTeamInviteDTO.getTeam().getName());
        Assertions.assertEquals(TeamInviteStatus.OPEN, firstTeamInviteDTO.getInviteStatus());
        Assertions.assertEquals(invited.getId(), firstTeamInviteDTO.getInvitedId());
        Assertions.assertEquals(inviting.getId(), firstTeamInviteDTO.getInvitingId());
    }

    @Test
    void createTeamInviteBadRequestUserIsAlreadyAMemberTest() throws Exception {
        User inviting = createUserService.create(1L);
        PublicTeam team = teamRepository.save(new PublicTeam(
                "team", LocalDate.now(), inviting
        ));

        User invited = createUserService.create(2L);
        joinTeamService.join(team, invited);

        CreateTeamInviteRequest createTeamInviteRequest = new CreateTeamInviteRequest(
                team.getId(),
                List.of(invited.getId())
        );
        String requestBody = objectMapper.writeValueAsString(createTeamInviteRequest);

        String response = mockMvc
                .perform(
                        post("/team/invite")
                                .contentType(APPLICATION_JSON)
                                .content(requestBody)
                                .header(tokenHeaderName, tokenValue)
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DefaultErrorResponse defaultErrorResponse = objectMapper.readValue(
                response,
                DefaultErrorResponse.class
        );

        Assertions.assertEquals(1, defaultErrorResponse.getErrors().size());
        Assertions.assertEquals(
                "User is already a team member",
                defaultErrorResponse.getErrors().get(0)
        );
    }

    @Test
    void createTeamInviteBadRequestUserIsAlreadyInvitedTest() throws Exception {
        User inviting = createUserService.create(1L);
        PublicTeam team = teamRepository.save(new PublicTeam(
                "team", LocalDate.now(), inviting
        ));

        User invited = createUserService.create(2L);
        createTeamInviteService.create(
                team, inviting, invited, LocalDateTime.now()
        );

        CreateTeamInviteRequest createTeamInviteRequest = new CreateTeamInviteRequest(
                team.getId(),
                List.of(invited.getId())
        );
        String requestBody = objectMapper.writeValueAsString(createTeamInviteRequest);

        String response = mockMvc
                .perform(
                        post("/team/invite")
                                .contentType(APPLICATION_JSON)
                                .content(requestBody)
                                .header(tokenHeaderName, tokenValue)
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DefaultErrorResponse defaultErrorResponse = objectMapper.readValue(
                response,
                DefaultErrorResponse.class
        );

        Assertions.assertEquals(1, defaultErrorResponse.getErrors().size());
        Assertions.assertEquals(
                "User is already invited to this team",
                defaultErrorResponse.getErrors().get(0)
        );
    }
}
