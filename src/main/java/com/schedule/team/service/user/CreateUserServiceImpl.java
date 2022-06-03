package com.schedule.team.service.user;

import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.DefaultTeam;
import com.schedule.team.repository.UserRepository;
import com.schedule.team.service.team.CreateDefaultTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserServiceImpl implements CreateUserService {
    private final UserRepository userRepository;
    private final CreateDefaultTeamService createDefaultTeamService;

    @Override
    public User create(Long id) {
        DefaultTeam defaultTeam = createDefaultTeamService.create();
        return userRepository.save(
                new User(id, defaultTeam)
        );
    }
}
