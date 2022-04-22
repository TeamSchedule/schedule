package com.schedule.team.repository;

import com.schedule.team.model.entity.TeamInvite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamInviteRepository extends CrudRepository<TeamInvite, Long> {
    List<TeamInvite> getAllByInvitedId(Long invitedId);

    List<TeamInvite> getAllByInvitingId(Long invitingId);
}
