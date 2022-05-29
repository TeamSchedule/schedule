package com.schedule.team.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedule.team.IntegrationTest;
import com.schedule.team.model.entity.Team;
import com.schedule.team.model.request.CreateTeamRequest;
import com.schedule.team.model.response.CreateTeamResponse;
import com.schedule.team.repository.TeamRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class TeamControllerTest extends IntegrationTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final TeamRepository teamRepository;
    private final String tokenHeaderName;
    private final String tokenValue;

    @Autowired
    public TeamControllerTest(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            TeamRepository teamRepository,
            @Value("${app.jwt.token.headerName}")
                    String tokenHeaderName,
            @Value("${app.jwt.token.test}")
                    String tokenValue
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.teamRepository = teamRepository;
        this.tokenHeaderName = tokenHeaderName;
        this.tokenValue = tokenValue;
    }

    @Test
    void createTeamTest() throws Exception {
        String teamName = "name";
        CreateTeamRequest createTeamRequest = new CreateTeamRequest(teamName);
        String createTeamRequestBody = objectMapper.writeValueAsString(createTeamRequest);

        String response = mockMvc
                .perform(
                        post("/team/")
                                .contentType(APPLICATION_JSON)
                                .content(createTeamRequestBody)
                                .header(tokenHeaderName, tokenValue)
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        CreateTeamResponse createTeamResponse = objectMapper.readValue(response, CreateTeamResponse.class);

        Team team = teamRepository.findById(createTeamResponse.getTeamId()).get();
        Assertions.assertNotNull(team);
        Assertions.assertEquals(teamName, team.getName());
    }
}
