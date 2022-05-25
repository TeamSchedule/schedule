package com.schedule.team.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Task {
    @Id
    @SequenceGenerator(
            name = "task_sequence",
            sequenceName = "task_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "task_sequence"
    )
    private Long id;

    private String name;

    @ManyToOne
    private User author;

    @ManyToOne
    private User assignee;

    @ManyToOne
    private Team team;

    private String description;

    private LocalDateTime creationTime;
    private LocalDateTime expirationTime;
    private boolean closed;

    public Task(
            String name,
            User author,
            User assignee,
            Team team,
            String description,
            LocalDateTime creationTime,
            LocalDateTime expirationTime
    ) {
        this.name = name;
        this.author = author;
        this.assignee = assignee;
        this.team = team;
        this.description = description;
        this.creationTime = creationTime;
        this.expirationTime = expirationTime;
    }
}
