package com.schedule.team.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schedule.team.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDescriptionDTO {
    private Long id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDate;
    private User admin;
    private String color;
}
