package com.schedule.team.service.team.community;

import com.schedule.team.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublicTeamExistsByIdServiceImpl implements PublicTeamExistsByIdService {
    private final TeamRepository teamRepository;

    @Override
    public boolean exists(Long id) {
        return teamRepository.existsById(id);
    }
}
