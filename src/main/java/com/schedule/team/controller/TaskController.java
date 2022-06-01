package com.schedule.team.controller;

import com.schedule.team.model.dto.TaskDTO;
import com.schedule.team.model.entity.Task;
import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.request.CreateTaskRequest;
import com.schedule.team.model.request.PatchTaskRequest;
import com.schedule.team.model.response.CreateTaskResponse;
import com.schedule.team.model.response.GetTasksResponse;
import com.schedule.team.service.jwt.ExtractClaimsFromRequestService;
import com.schedule.team.service.jwt.ExtractUserFromRequestService;
import com.schedule.team.service.task.*;
import com.schedule.team.service.team.GetTeamByIdService;
import com.schedule.team.service.team.GetTeamsListByIdService;
import com.schedule.team.service.user.GetUserByIdService;
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
    private final ExtractUserFromRequestService extractUserFromRequestService;
    private final GetUserByIdService getUserByIdService;
    private final CreateTaskService createTaskService;
    private final GetTeamByIdService getTeamByIdService;
    private final GetTaskByIdService getTaskByIdService;
    private final GetTasksInRangeService getTasksInRangeService;
    private final DeleteTaskByIdService deleteTaskByIdService;
    private final UpdateTaskService updateTaskService;
    private final BuildTaskDtoService buildTaskDtoService;
    private final GetTeamsListByIdService getTeamsListByIdService;

    @PostMapping
    public ResponseEntity<CreateTaskResponse> create(
            @RequestBody CreateTaskRequest createTaskRequest,
            HttpServletRequest request
    ) {
        User creator = extractUserFromRequestService.extract(request);
        User assignee = getUserByIdService.get(createTaskRequest.getAssigneeId());
        Team team = getTeamByIdService.get(createTaskRequest.getTeamId());

        Task task = createTaskService.create(
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
        Task task = getTaskByIdService.get(taskId);
        return ResponseEntity.ok().body(
                buildTaskDtoService.build(task)
        );
    }

    @GetMapping
    public ResponseEntity<GetTasksResponse> getTasksInRange(
            HttpServletRequest request,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam("teams") List<Long> teamsIds
    ) {
        List<Team> teams = getTeamsListByIdService.get(teamsIds);

        return ResponseEntity.ok().body(
                new GetTasksResponse(
                        getTasksInRangeService
                                .getTasksInRange(
                                        from,
                                        to,
                                        extractClaimsFromRequestService.extract(request).getId(),
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
        deleteTaskByIdService.delete(taskId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<?> patchTask(
            @RequestBody PatchTaskRequest patchTaskRequest,
            @PathVariable Long taskId
    ) {
        updateTaskService.patchTask(
                getTaskByIdService.get(taskId),
                patchTaskRequest.getName(),
                patchTaskRequest.getDescription(),
                patchTaskRequest.getExpirationDate(),
                patchTaskRequest.getClosed()
        );
        return ResponseEntity.ok().build();
    }
}
