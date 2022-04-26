package com.schedule.team.service.user;

import com.schedule.team.model.entity.User;
import com.schedule.team.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserByIdServiceImpl implements GetUserByIdService {
    private final UserRepository userRepository;

    @Override
    public User get(Long id) {
        return userRepository.getById(id);
    }
}
