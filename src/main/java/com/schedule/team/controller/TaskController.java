package com.schedule.team.controller;

import com.schedule.team.model.dto.TaskDTO;
import com.schedule.team.model.entity.Task;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.Team;
import com.schedule.team.model.request.CreateTaskRequest;
import com.schedule.team.model.request.PatchTaskRequest;
import com.schedule.team.model.response.CreateTaskResponse;
import com.schedule.team.model.response.GetTasksResponse;
import com.schedule.team.service.jwt.ExtractClaimsFromRequestService;
import com.schedule.team.service.task.*;
import com.schedule.team.service.team.TeamService;
import com.schedule.team.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final ExtractClaimsFromRequestService extractClaimsFromRequestService;
    private final TeamService teamService;
    private final BuildTaskDtoService buildTaskDtoService;
    private final TaskService taskService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<CreateTaskResponse> create(
            @RequestBody CreateTaskRequest createTaskRequest,
            HttpServletRequest request
    ) {
        User creator = extractClaimsFromRequestService.extractUser(request);
        User assignee = userService.getById(createTaskRequest.getAssigneeId());
        Team team = teamService.getById(createTaskRequest.getTeamId().orElse(creator.getDefaultTeam().getId()));

        Task task = taskService.create(
                createTaskRequest.getName(),
                creator,
                assignee,
                team,
                createTaskRequest.getDescription(),
                LocalDateTime.now(),
                createTaskRequest.getExpirationTime()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CreateTaskResponse(
                        task.getId()
                )
        );
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getById(
            @PathVariable Long taskId
    ) {
        Task task = taskService.getById(taskId);
        return ResponseEntity.ok().body(
                buildTaskDtoService.build(task)
        );
    }

    @GetMapping
    public ResponseEntity<GetTasksResponse> getTasksInRange(
            HttpServletRequest request,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam("teams") List<Long> teamsIds,
            @RequestParam(value = "private", defaultValue = "false") boolean addPrivate
    ) {
        User user = extractClaimsFromRequestService.extractUser(request);
        if (addPrivate) {
            teamsIds.add(user.getDefaultTeam().getId());
        }
        List<Team> teams = teamService.getListByIds(teamsIds);

        return ResponseEntity.ok().body(
                new GetTasksResponse(
                        taskService
                                .getInTimeRange(
                                        from,
                                        to,
                                        user.getId(),
                                        teams
                                )
                                .stream()
                                .map(buildTaskDtoService::build)
                                .toList()
                )
        );
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> delete(
            @PathVariable Long taskId
    ) {
        taskService.deleteById(taskId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<?> patchTask(
            @RequestBody PatchTaskRequest patchTaskRequest,
            @PathVariable Long taskId
    ) {
        taskService.update(
                taskService.getById(taskId),
                patchTaskRequest.getName(),
                patchTaskRequest.getDescription(),
                patchTaskRequest.getExpirationDate(),
                patchTaskRequest.getClosed()
        );
        return ResponseEntity.ok().build();
    }
}
