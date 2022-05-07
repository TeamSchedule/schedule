package com.schedule.team.model.request;

import lombok.Data;

@Data
public class PatchTeamRequest {
    private String newName;
    private String color;
}
