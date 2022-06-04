package com.schedule.team.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedule.team.IntegrationTest;
import com.schedule.team.model.dto.TaskDTO;
import com.schedule.team.model.entity.Task;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.PublicTeam;
import com.schedule.team.model.response.GetTasksResponse;
import com.schedule.team.repository.TaskRepository;
import com.schedule.team.repository.team.TeamRepository;
import com.schedule.team.service.task.BuildTaskDtoService;
import com.schedule.team.service.user.CreateUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class GetTasksInRangeTest extends IntegrationTest {
    private final MockMvc mockMvc;
    private final TaskRepository taskRepository;
    private final CreateUserService createUserService;
    private final TeamRepository teamRepository;
    private final ObjectMapper objectMapper;
    private final String tokenHeaderName;
    private final String tokenValue;
    private final LocalDateTime creationTime;
    private final LocalDateTime startExpirationTime;
    private final LocalDateTime endExpirationTIme;
    private final BuildTaskDtoService buildTaskDtoService;
    private List<Task> tasks;
    private List<Long> teamsIds;
    private Long privateTaskId;

    @Autowired
    public GetTasksInRangeTest(
            MockMvc mockMvc,
            TaskRepository taskRepository,
            CreateUserService createUserService,
            TeamRepository teamRepository,
            ObjectMapper objectMapper,
            @Value("${app.jwt.token.headerName}")
                    String tokenHeaderName,
            @Value("${app.jwt.token.test}")
                    String tokenValue,
            BuildTaskDtoService buildTaskDtoService
    ) {
        this.mockMvc = mockMvc;
        this.taskRepository = taskRepository;
        this.createUserService = createUserService;
        this.teamRepository = teamRepository;
        this.objectMapper = objectMapper;
        this.tokenHeaderName = tokenHeaderName;
        this.tokenValue = tokenValue;
        this.buildTaskDtoService = buildTaskDtoService;
        this.creationTime = LocalDateTime.of(10, 10, 10, 10, 10, 10);
        this.startExpirationTime = LocalDateTime.of(10, 10, 10, 10, 10, 10);
        this.endExpirationTIme = startExpirationTime.plus(Duration.ofHours(8));
    }

    @AfterEach
    public void afterEach() {
        clearDb();
    }

    @BeforeEach
    public void beforeEach() {
        User author = createUserService.create(1L);
        PublicTeam team = teamRepository.save(new PublicTeam("test", LocalDate.now(), author));

        String taskName = "test";
        String taskDescription = "description";

        List<Task> tasks = new ArrayList<>();
        for (
                LocalDateTime expirationTime = startExpirationTime;
                expirationTime.isBefore(endExpirationTIme);
                expirationTime = expirationTime.plus(Duration.ofHours(1))
        ) {
            Task task = taskRepository.save(
                    new Task(
                            taskName,
                            author,
                            author,
                            team,
                            taskDescription,
                            creationTime,
                            expirationTime
                    )
            );
            tasks.add(task);
        }
        Task privateTask = taskRepository.save(
                new Task(
                        taskName,
                        author,
                        author,
                        author.getDefaultTeam(),
                        taskDescription,
                        creationTime,
                        startExpirationTime
                )
        );
        this.privateTaskId = privateTask.getId();
        tasks.add(privateTask);
        this.tasks = tasks;
        this.teamsIds = List.of(team.getId());
    }

    @Test
    void getPrivateTaskTest() throws Exception {
        String from = startExpirationTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String to = endExpirationTIme.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String teams = "";

        String response = mockMvc
                .perform(
                        get("/task/")
                                .queryParam("from", from)
                                .queryParam("to", to)
                                .queryParam("teams", teams)
                                .queryParam("private", Boolean.TRUE.toString())
                                .header(tokenHeaderName, tokenValue)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GetTasksResponse getTasksResponse = objectMapper.readValue(response, GetTasksResponse.class);
        List<TaskDTO> expectedTasksDTOs = tasks
                .stream()
                .filter(task -> task.getId().equals(privateTaskId))
                .map(buildTaskDtoService::build)
                .toList();
        Assertions.assertTrue(
                expectedTasksDTOs.size() == getTasksResponse.getTasks().size()
                        && expectedTasksDTOs.containsAll(getTasksResponse.getTasks())
                        && getTasksResponse.getTasks().containsAll(expectedTasksDTOs)
        );
    }

    @Test
    void getAllTasksExcludePrivateTest() throws Exception {
        String from = startExpirationTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String to = endExpirationTIme.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String teams = teamsIds.stream().map(String::valueOf).collect(Collectors.joining(","));

        String response = mockMvc
                .perform(
                        get("/task/")
                                .queryParam("from", from)
                                .queryParam("to", to)
                                .queryParam("teams", teams)
                                .queryParam("private", Boolean.FALSE.toString())
                                .header(tokenHeaderName, tokenValue)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GetTasksResponse getTasksResponse = objectMapper.readValue(response, GetTasksResponse.class);
        List<TaskDTO> expectedTasksDTOs = tasks
                .stream()
                .filter(task -> !task.getId().equals(privateTaskId))
                .map(buildTaskDtoService::build)
                .toList();
        Assertions.assertTrue(
                expectedTasksDTOs.size() == getTasksResponse.getTasks().size()
                        && expectedTasksDTOs.containsAll(getTasksResponse.getTasks())
                        && getTasksResponse.getTasks().containsAll(expectedTasksDTOs)
        );
    }

    @Test
    void getAllTasksInRangeTest() throws Exception {
        String from = startExpirationTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String to = endExpirationTIme.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String teams = teamsIds.stream().map(String::valueOf).collect(Collectors.joining(","));

        String response = mockMvc
                .perform(
                        get("/task/")
                                .queryParam("from", from)
                                .queryParam("to", to)
                                .queryParam("teams", teams)
                                .queryParam("private", Boolean.TRUE.toString())
                                .header(tokenHeaderName, tokenValue)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GetTasksResponse getTasksResponse = objectMapper.readValue(response, GetTasksResponse.class);
        List<TaskDTO> expectedTasksDTOs = tasks.stream().map(buildTaskDtoService::build).toList();
        Assertions.assertTrue(
                expectedTasksDTOs.size() == getTasksResponse.getTasks().size()
                        && expectedTasksDTOs.containsAll(getTasksResponse.getTasks())
                        && getTasksResponse.getTasks().containsAll(expectedTasksDTOs)
        );
    }

    @Test
    void getFirstPartTasksInRangeTest() throws Exception {
        String from = startExpirationTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime expirationTime = endExpirationTIme.minus(Duration.ofHours(4));
        String to = expirationTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String teams = teamsIds.stream().map(String::valueOf).collect(Collectors.joining(","));

        String response = mockMvc
                .perform(
                        get("/task/")
                                .queryParam("from", from)
                                .queryParam("to", to)
                                .queryParam("teams", teams)
                                .queryParam("private", Boolean.TRUE.toString())
                                .header(tokenHeaderName, tokenValue)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GetTasksResponse getTasksResponse = objectMapper.readValue(response, GetTasksResponse.class);
        List<TaskDTO> expectedTasksDTOs = tasks
                .stream()
                .map(buildTaskDtoService::build)
                .filter(taskDTO -> !taskDTO.getExpirationTime().isAfter(expirationTime))
                .toList();
        Assertions.assertTrue(
                expectedTasksDTOs.size() == getTasksResponse.getTasks().size()
                        && expectedTasksDTOs.containsAll(getTasksResponse.getTasks())
                        && getTasksResponse.getTasks().containsAll(expectedTasksDTOs)
        );
    }

    @Test
    void getSecondPartTasksInRangeTest() throws Exception {
        LocalDateTime expirationTime = startExpirationTime.plus(Duration.ofHours(4));
        String from = expirationTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String to = endExpirationTIme.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String teams = teamsIds.stream().map(String::valueOf).collect(Collectors.joining(","));

        String response = mockMvc
                .perform(
                        get("/task/")
                                .queryParam("from", from)
                                .queryParam("to", to)
                                .queryParam("teams", teams)
                                .queryParam("private", Boolean.TRUE.toString())
                                .header(tokenHeaderName, tokenValue)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GetTasksResponse getTasksResponse = objectMapper.readValue(response, GetTasksResponse.class);
        List<TaskDTO> expectedTasksDTOs = tasks
                .stream()
                .map(buildTaskDtoService::build)
                .filter(taskDTO -> !taskDTO.getExpirationTime().isBefore(expirationTime))
                .toList();
        Assertions.assertTrue(
                expectedTasksDTOs.size() == getTasksResponse.getTasks().size()
                        && expectedTasksDTOs.containsAll(getTasksResponse.getTasks())
                        && getTasksResponse.getTasks().containsAll(expectedTasksDTOs)
        );
    }
}
