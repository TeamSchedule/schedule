package com.schedule.team.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedule.team.IntegrationTest;
import com.schedule.team.model.dto.team.TeamDTO;
import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.TeamColor;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.request.CreateDefaultTeamRequest;
import com.schedule.team.model.request.CreateTeamRequest;
import com.schedule.team.model.request.UpdateTeamRequest;
import com.schedule.team.model.response.CreateTeamResponse;
import com.schedule.team.model.response.GetTeamByIdResponse;
import com.schedule.team.repository.TeamColorRepository;
import com.schedule.team.repository.TeamRepository;
import com.schedule.team.repository.UserRepository;
import com.schedule.team.service.team.JoinTeamService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class TeamControllerTest extends IntegrationTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final JoinTeamService joinTeamService;
    private final TeamColorRepository teamColorRepository;
    private final String tokenHeaderName;
    private final String tokenValue;

    @Autowired
    public TeamControllerTest(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            TeamRepository teamRepository,
            UserRepository userRepository,
            JoinTeamService joinTeamService,
            TeamColorRepository teamColorRepository,
            @Value("${app.jwt.token.headerName}")
                    String tokenHeaderName,
            @Value("${app.jwt.token.test}")
                    String tokenValue
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.joinTeamService = joinTeamService;
        this.teamColorRepository = teamColorRepository;
        this.tokenHeaderName = tokenHeaderName;
        this.tokenValue = tokenValue;
    }

    @AfterEach
    public void afterEach() {
        clearDb();
    }

    @Test
    void createDefaultTeamTest() throws Exception {
        Long userId = 1L;
        CreateDefaultTeamRequest createDefaultTeamRequest = new CreateDefaultTeamRequest(userId);
        String createDefaultTeamRequestBody = objectMapper.writeValueAsString(createDefaultTeamRequest);

        String response = mockMvc
                .perform(
                        post("/team/default")
                                .contentType(APPLICATION_JSON)
                                .content(createDefaultTeamRequestBody)
                                .header(tokenHeaderName, tokenValue)
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        CreateTeamResponse createTeamResponse = objectMapper.readValue(response, CreateTeamResponse.class);

        User user = userRepository.findById(userId).get();
        Assertions.assertNotNull(user);
        Assertions.assertEquals(userId, user.getId());

        Team team = teamRepository.findById(createTeamResponse.getTeamId()).get();
        Assertions.assertNotNull(team);
        Assertions.assertEquals(String.valueOf(userId), team.getName());
    }

    @Test
    void createTeamTest() throws Exception {
        userRepository.save(new User(1L));

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

    @Test
    void getTeamTest() throws Exception {
        User admin = userRepository.save(new User(1L));
        User member = userRepository.save(new User(2L));

        String teamName = "test";
        LocalDate creationDate = LocalDate.of(10, 10, 10);
        Team team = teamRepository.save(new Team(teamName, creationDate, admin));
        joinTeamService.join(team, admin);
        joinTeamService.join(team, member);

        String response = mockMvc
                .perform(
                        get("/team/" + team.getId())
                                .header(tokenHeaderName, tokenValue)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        GetTeamByIdResponse getTeamByIdResponse = objectMapper.readValue(response, GetTeamByIdResponse.class);

        TeamDTO teamDTO = getTeamByIdResponse.getTeam();
        Assertions.assertNotNull(teamDTO);
        Assertions.assertEquals(teamName, teamDTO.getName());
        Assertions.assertEquals(creationDate, teamDTO.getCreationDate());
        Assertions.assertEquals(admin.getId(), teamDTO.getAdminId());
        List<Long> expectedMembersIds = List.of(admin.getId(), member.getId());
        Assertions.assertTrue(
                expectedMembersIds.size() == teamDTO.getMembersIds().size()
                        && expectedMembersIds.containsAll(teamDTO.getMembersIds())
                        && teamDTO.getMembersIds().containsAll(expectedMembersIds)
        );
    }

    @Test
    void leaveTeamTest() throws Exception {
        User admin = userRepository.save(new User(1L));

        String teamName = "test";
        LocalDate creationDate = LocalDate.of(10, 10, 10);
        Team team = teamRepository.save(new Team(teamName, creationDate, admin));
        joinTeamService.join(team, admin);

        mockMvc
                .perform(
                        delete("/team/" + team.getId() + "/user")
                                .header(tokenHeaderName, tokenValue)
                )
                .andExpect(status().isNoContent());

        Assertions.assertFalse(teamColorRepository.existsByTeamAndUser(team, admin));
    }

    @Test
    void patchTeamTest() throws Exception {
        User admin = userRepository.save(new User(1L));

        String teamName = "test";
        LocalDate creationDate = LocalDate.of(10, 10, 10);
        Team team = teamRepository.save(new Team(teamName, creationDate, admin));
        joinTeamService.join(team, admin);

        String newName = "new name";
        String newColor = "new color";
        UpdateTeamRequest updateTeamRequest = new UpdateTeamRequest(newName, newColor);
        String requestBody = objectMapper.writeValueAsString(updateTeamRequest);

        mockMvc
                .perform(
                        patch("/team/" + team.getId())
                                .header(tokenHeaderName, tokenValue)
                                .contentType(APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk());

        Team updatedTeam = teamRepository.findById(team.getId()).get();
        TeamColor updatedTeamColor = teamColorRepository.findByUserIdAndTeamId(admin.getId(), team.getId());

        Assertions.assertEquals(newName, updatedTeam.getName());
        Assertions.assertEquals(newColor, updatedTeamColor.getColor());
    }
}
