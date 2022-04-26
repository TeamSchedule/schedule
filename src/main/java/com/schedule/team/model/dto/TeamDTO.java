package com.schedule.team.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schedule.team.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {
    private Long id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDate;
    private User admin;
    private List<User> members;
}
