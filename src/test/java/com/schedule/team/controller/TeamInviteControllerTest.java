package com.schedule.team.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedule.team.IntegrationTest;
import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.TeamInvite;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.request.CreateTeamInviteRequest;
import com.schedule.team.model.response.CreateTeamInviteResponse;
import com.schedule.team.repository.TeamColorRepository;
import com.schedule.team.repository.TeamInviteRepository;
import com.schedule.team.repository.TeamRepository;
import com.schedule.team.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class TeamInviteControllerTest extends IntegrationTest {
    private final MockMvc mockMvc;
    private final TeamInviteRepository teamInviteRepository;
    private final ObjectMapper objectMapper;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamColorRepository teamColorRepository;
    private final String tokenHeaderName;
    private final String tokenValue;

    @Autowired
    public TeamInviteControllerTest(
            MockMvc mockMvc,
            TeamInviteRepository teamInviteRepository,
            ObjectMapper objectMapper,
            TeamRepository teamRepository,
            UserRepository userRepository,
            TeamColorRepository teamColorRepository,
            @Value("${app.jwt.token.headerName}")
                    String tokenHeaderName,
            @Value("${app.jwt.token.test}")
                    String tokenValue
    ) {
        this.mockMvc = mockMvc;
        this.teamInviteRepository = teamInviteRepository;
        this.objectMapper = objectMapper;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.teamColorRepository = teamColorRepository;
        this.tokenHeaderName = tokenHeaderName;
        this.tokenValue = tokenValue;
    }

    @AfterEach
    public void afterEach() {
        clearDb();
    }

    @Test
    void createTeamInviteTest() throws Exception {
        User inviting = userRepository.save(new User(1L));
        Team team = teamRepository.save(new Team(
                "team", LocalDate.now(), inviting
        ));

        User invited = userRepository.save(new User(2L));
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
}
