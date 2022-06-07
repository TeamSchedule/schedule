package com.schedule.team.service.team.community;

import com.schedule.team.model.entity.team.PublicTeam;
import com.schedule.team.repository.team.PublicTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPublicTeamByIdServiceImpl implements GetPublicTeamByIdService {
    private final PublicTeamRepository publicTeamRepository;

    @Override
    public PublicTeam get(Long id) {
        return publicTeamRepository.getById(id);
    }
}
