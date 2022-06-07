package com.schedule.team.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedule.team.IntegrationTest;
import com.schedule.team.model.dto.team.TeamDTO;
import com.schedule.team.model.dto.team.TeamDescriptionDTO;
import com.schedule.team.model.entity.TeamColor;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.PublicTeam;
import com.schedule.team.model.request.CreateDefaultTeamRequest;
import com.schedule.team.model.request.CreateTeamRequest;
import com.schedule.team.model.request.UpdateTeamRequest;
import com.schedule.team.model.response.CreateTeamResponse;
import com.schedule.team.model.response.DefaultErrorResponse;
import com.schedule.team.model.response.GetTeamByIdResponse;
import com.schedule.team.model.response.GetTeamsResponse;
import com.schedule.team.repository.TeamColorRepository;
import com.schedule.team.repository.UserRepository;
import com.schedule.team.repository.team.PublicTeamRepository;
import com.schedule.team.repository.team.TeamRepository;
import com.schedule.team.service.team.community.JoinTeamService;
import com.schedule.team.service.user.CreateUserService;
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
    private final String defaultTeamColor;
    private final PublicTeamRepository publicTeamRepository;
    private final CreateUserService createUserService;

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
                    String tokenValue,
            @Value("${app.team.color.default}")
                    String defaultTeamColor,
            PublicTeamRepository publicTeamRepository, CreateUserService createUserService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.joinTeamService = joinTeamService;
        this.teamColorRepository = teamColorRepository;
        this.tokenHeaderName = tokenHeaderName;
        this.tokenValue = tokenValue;
        this.defaultTeamColor = defaultTeamColor;
        this.publicTeamRepository = publicTeamRepository;
        this.createUserService = createUserService;
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

        mockMvc
                .perform(
                        post("/team/default")
                                .contentType(APPLICATION_JSON)
                                .content(createDefaultTeamRequestBody)
                )
                .andExpect(status().isCreated());

        User user = userRepository.findById(userId).get();
        Assertions.assertNotNull(user);
        Assertions.assertEquals(userId, user.getId());
        Assertions.assertNotNull(user.getDefaultTeam());
        Assertions.assertEquals(defaultTeamColor, user.getDefaultTeam().getColor());
    }

    @Test
    void createTeamTest() throws Exception {
        User user = createUserService.create(1L);

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

        PublicTeam team = publicTeamRepository.findById(createTeamResponse.getTeamId()).get();
        Assertions.assertNotNull(team);
        Assertions.assertEquals(teamName, team.getName());
        Assertions.assertTrue(teamColorRepository.existsByTeamAndUser(team, user));
    }

    @Test
    void getTeamByIdTest() throws Exception {
        User admin = createUserService.create(1L);
        User member = createUserService.create(2L);

        String teamName = "test";
        LocalDate creationDate = LocalDate.of(10, 10, 10);
        PublicTeam team = teamRepository.save(new PublicTeam(teamName, creationDate, admin));
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
    void getPersonalTeamsTest() throws Exception {
        User admin = createUserService.create(1L);

        String firstTeamName = "test";
        LocalDate firstTeamCreationDate = LocalDate.of(10, 10, 10);
        PublicTeam firstTeam = teamRepository.save(new PublicTeam(firstTeamName, firstTeamCreationDate, admin));
        joinTeamService.join(firstTeam, admin);

        String secondTeamName = "team2";
        LocalDate secondTeamCreationDate = LocalDate.of(11, 11, 11);
        PublicTeam secondTeam = teamRepository.save(new PublicTeam(secondTeamName, secondTeamCreationDate, admin));
        joinTeamService.join(secondTeam, admin);

        String response = mockMvc
                .perform(
                        get("/team/")
                                .header(tokenHeaderName, tokenValue)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        GetTeamsResponse getTeamsResponse = objectMapper.readValue(response, GetTeamsResponse.class);

        List<TeamDescriptionDTO> teamDescriptionDTOS = getTeamsResponse.getTeams();
        Assertions.assertNotNull(teamDescriptionDTOS);
        Assertions.assertEquals(2, teamDescriptionDTOS.size());

        TeamDescriptionDTO firstTeamDescription = teamDescriptionDTOS.get(0);
        Assertions.assertEquals(firstTeam.getId(), firstTeamDescription.getId());
        Assertions.assertEquals(firstTeamName, firstTeamDescription.getName());
        Assertions.assertEquals(firstTeamCreationDate, firstTeamDescription.getCreationDate());
        Assertions.assertEquals(admin.getId(), firstTeamDescription.getAdminId());
        Assertions.assertEquals(defaultTeamColor, firstTeamDescription.getColor());

        TeamDescriptionDTO secondTeamDescription = teamDescriptionDTOS.get(1);
        Assertions.assertEquals(secondTeam.getId(), secondTeamDescription.getId());
        Assertions.assertEquals(secondTeamName, secondTeamDescription.getName());
        Assertions.assertEquals(secondTeamCreationDate, secondTeamDescription.getCreationDate());
        Assertions.assertEquals(admin.getId(), secondTeamDescription.getAdminId());
        Assertions.assertEquals(defaultTeamColor, secondTeamDescription.getColor());
    }

    @Test
    void leaveTeamTest() throws Exception {
        User admin = createUserService.create(1L);

        String teamName = "test";
        LocalDate creationDate = LocalDate.of(10, 10, 10);
        PublicTeam team = teamRepository.save(new PublicTeam(teamName, creationDate, admin));
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
    void updateTeamTest() throws Exception {
        User admin = createUserService.create(1L);

        String teamName = "test";
        LocalDate creationDate = LocalDate.of(10, 10, 10);
        PublicTeam team = teamRepository.save(new PublicTeam(teamName, creationDate, admin));
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

        PublicTeam updatedTeam = publicTeamRepository.findById(team.getId()).get();
        TeamColor updatedTeamColor = teamColorRepository.findByUserIdAndTeamId(admin.getId(), team.getId());

        Assertions.assertEquals(newName, updatedTeam.getName());
        Assertions.assertEquals(newColor, updatedTeamColor.getColor());
    }

    @Test
    void getTeamBadRequestTeamNotFoundTest() throws Exception {
        String response = mockMvc
                .perform(
                        get("/team/1")
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
                "Team with specified id is not present",
                defaultErrorResponse.getErrors().get(0)
        );
    }

    @Test
    void updateTeamBadRequestTeamNotFoundTest() throws Exception {
        String newName = "new name";
        String newColor = "new color";
        UpdateTeamRequest updateTeamRequest = new UpdateTeamRequest(newName, newColor);
        String requestBody = objectMapper.writeValueAsString(updateTeamRequest);

        String response = mockMvc
                .perform(
                        patch("/team/1")
                                .header(tokenHeaderName, tokenValue)
                                .contentType(APPLICATION_JSON)
                                .content(requestBody)
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
                "Team with specified id is not present",
                defaultErrorResponse.getErrors().get(0)
        );
    }

    @Test
    void leaveTeamBadRequestTeamNotFoundTest() throws Exception {
        String response = mockMvc
                .perform(
                        delete("/team/1/user")
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
                "Team with specified id is not present",
                defaultErrorResponse.getErrors().get(0)
        );
    }
}
