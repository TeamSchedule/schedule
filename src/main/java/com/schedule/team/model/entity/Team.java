package com.schedule.team.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Team {
    @Id
    @SequenceGenerator(
            name = "team_sequence",
            sequenceName = "team_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "team_sequence"
    )
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @ManyToOne
    private User admin;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "team_to_user",
            joinColumns = {
                    @JoinColumn(name = "team_id", referencedColumnName = "id"),
            },
            foreignKey = @ForeignKey(name = "team_fkey"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseForeignKey = @ForeignKey(name = "user_fkey")
    )
    private Set<User> users;

    public Team(String name, LocalDate creationDate, User admin) {
        this.name = name;
        this.creationDate = creationDate;
        this.admin = admin;
        this.users = new HashSet<>();
        this.users.add(admin);
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }

    public boolean hasUser(User user) {
        return this.users.contains(user);
    }
}
