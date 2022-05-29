package com.schedule.team.model.response;

import com.schedule.team.model.dto.TaskDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTasksResponse {
    private List<TaskDTO> tasks;
}
