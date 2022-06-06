package com.schedule.team.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DefaultErrorResponse {
    private List<String> errors;
}
