package com.schedule.team.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedule.team.IntegrationTest;
import com.schedule.team.model.dto.TaskDTO;
import com.schedule.team.model.entity.Task;
import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.request.CreateTaskRequest;
import com.schedule.team.model.request.PatchTaskRequest;
import com.schedule.team.model.response.CreateTaskResponse;
import com.schedule.team.repository.TaskRepository;
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
import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class TaskControllerTest extends IntegrationTest {
    private final MockMvc mockMvc;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ObjectMapper objectMapper;
    private final String tokenHeaderName;
    private final String tokenValue;

    @Autowired
    public TaskControllerTest(
            MockMvc mockMvc,
            TaskRepository taskRepository,
            UserRepository userRepository,
            TeamRepository teamRepository,
            ObjectMapper objectMapper,
            @Value("${app.jwt.token.headerName}")
                    String tokenHeaderName,
            @Value("${app.jwt.token.test}")
                    String tokenValue
    ) {
        this.mockMvc = mockMvc;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.objectMapper = objectMapper;
        this.tokenHeaderName = tokenHeaderName;
        this.tokenValue = tokenValue;
    }

    @AfterEach
    public void afterEach() {
        clearDb();
    }

    @Test
    void createTaskTest() throws Exception {
        User author = userRepository.save(new User(1L));
        User assignee = userRepository.save(new User(2L));

        Team team = teamRepository.save(new Team("test", LocalDate.now(), author));

        String taskName = "test";
        String taskDescription = "description";
        LocalDateTime taskExpirationTime = LocalDateTime.of(12, 12, 12, 12, 12, 12);

        CreateTaskRequest createTaskRequest = new CreateTaskRequest(
                taskName,
                taskDescription,
                taskExpirationTime,
                team.getId(),
                assignee.getId()
        );
        String requestBody = objectMapper.writeValueAsString(createTaskRequest);

        String response = mockMvc
                .perform(
                        post("/task/")
                                .header(tokenHeaderName, tokenValue)
                                .contentType(APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CreateTaskResponse createTaskResponse = objectMapper.readValue(response, CreateTaskResponse.class);
        Task task = taskRepository.findById(createTaskResponse.getId()).get();
        Assertions.assertNotNull(task);
        Assertions.assertEquals(taskName, task.getName());
        Assertions.assertEquals(taskDescription, task.getDescription());
        Assertions.assertEquals(taskExpirationTime, task.getExpirationTime());
        Assertions.assertEquals(team.getId(), task.getTeam().getId());
        Assertions.assertEquals(assignee.getId(), task.getAssignee().getId());
    }

    @Test
    void getTaskByIdTest() throws Exception {
        User author = userRepository.save(new User(1L));
        User assignee = userRepository.save(new User(2L));
        Team team = teamRepository.save(new Team("test", LocalDate.now(), author));

        String taskName = "test";
        String taskDescription = "description";
        LocalDateTime creationTime = LocalDateTime.of(10, 10, 10, 10, 10, 10);
        LocalDateTime expirationTime = LocalDateTime.of(12, 12, 12, 12, 12, 12);

        Task task = taskRepository.save(
                new Task(
                        taskName,
                        author,
                        assignee,
                        team,
                        taskDescription,
                        creationTime,
                        expirationTime
                )
        );

        String response = mockMvc
                .perform(
                        get("/task/"+task.getId())
                                .header(tokenHeaderName, tokenValue)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TaskDTO taskDTO = objectMapper.readValue(response, TaskDTO.class);
        Assertions.assertNotNull(taskDTO);
        Assertions.assertEquals(task.getId(), taskDTO.getId());
        Assertions.assertEquals(task.getName(), taskDTO.getName());
        Assertions.assertEquals(task.getDescription(), taskDTO.getDescription());
        Assertions.assertEquals(task.getCreationTime(), taskDTO.getCreationTime());
        Assertions.assertEquals(task.getExpirationTime(), taskDTO.getExpirationTime());
        Assertions.assertEquals(task.getAssignee().getId(), taskDTO.getAssigneeId());
        Assertions.assertEquals(task.getAuthor().getId(), taskDTO.getAuthorId());
        Assertions.assertEquals(task.getTeam().getId(), taskDTO.getTeam().getId());
    }

    @Test
    void deleteTaskTest() throws Exception {
        User author = userRepository.save(new User(1L));
        User assignee = userRepository.save(new User(2L));
        Team team = teamRepository.save(new Team("test", LocalDate.now(), author));

        String taskName = "test";
        String taskDescription = "description";
        LocalDateTime creationTime = LocalDateTime.of(10, 10, 10, 10, 10, 10);
        LocalDateTime expirationTime = LocalDateTime.of(12, 12, 12, 12, 12, 12);

        Task task = taskRepository.save(
                new Task(
                        taskName,
                        author,
                        assignee,
                        team,
                        taskDescription,
                        creationTime,
                        expirationTime
                )
        );

        mockMvc
                .perform(
                        delete("/task/"+task.getId())
                                .header(tokenHeaderName, tokenValue)
                )
                .andExpect(status().isOk());
        Assertions.assertFalse(taskRepository.existsById(task.getId()));
    }

    @Test
    void updateTaskTest() throws Exception {
        User author = userRepository.save(new User(1L));
        User assignee = userRepository.save(new User(2L));
        Team team = teamRepository.save(new Team("test", LocalDate.now(), author));

        Task task = taskRepository.save(
                new Task(
                        "test",
                        author,
                        assignee,
                        team,
                        "description",
                        LocalDateTime.of(10, 10, 10, 10, 10, 10),
                        LocalDateTime.of(12, 12, 12, 12, 12, 12)
                )
        );

        String newTaskName = "new name";
        String newTaskDescription = "new description";
        Boolean newTaskClosed = true;
        LocalDateTime newTaskExpirationTime = LocalDateTime.of(7, 7, 7, 7, 7, 7);

        PatchTaskRequest patchTaskRequest = new PatchTaskRequest(
                newTaskName,
                newTaskDescription,
                newTaskExpirationTime,
                newTaskClosed
        );
        String requestBody = objectMapper.writeValueAsString(patchTaskRequest);

        mockMvc
                .perform(
                        patch("/task/"+task.getId())
                                .header(tokenHeaderName, tokenValue)
                                .contentType(APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk());

        Task updatedTask = taskRepository.findById(task.getId()).get();
        Assertions.assertNotNull(updatedTask);
        Assertions.assertEquals(newTaskName, updatedTask.getName());
        Assertions.assertEquals(newTaskDescription, updatedTask.getDescription());
        Assertions.assertEquals(newTaskExpirationTime, updatedTask.getExpirationTime());
        Assertions.assertEquals(newTaskClosed, updatedTask.isClosed());
    }
}
