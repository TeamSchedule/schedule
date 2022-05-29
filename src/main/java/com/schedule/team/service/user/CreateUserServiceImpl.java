package com.schedule.team.service.user;

import com.schedule.team.model.entity.User;
import com.schedule.team.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserServiceImpl implements CreateUserService {
    private final UserRepository userRepository;

    @Override
    public User create(Long id) {
        return userRepository.save(
                new User(id)
        );
    }
}
