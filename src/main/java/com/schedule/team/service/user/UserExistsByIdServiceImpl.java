package com.schedule.team.service.user;

import com.schedule.team.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserExistsByIdServiceImpl implements UserExistsByIdService {
    private final UserRepository userRepository;

    @Override
    public boolean exists(Long id) {
        return userRepository.existsById(id);
    }
}
