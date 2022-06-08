package com.schedule.team.service.user;

import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.DefaultTeam;
import com.schedule.team.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(Long id, DefaultTeam defaultTeam) {
        return userRepository.save(
                new User(id, defaultTeam)
        );
    }

    @Override
    public User getById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
