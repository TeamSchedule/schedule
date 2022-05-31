package com.schedule.team.repository;

import com.schedule.team.model.TeamInviteStatus;
import com.schedule.team.model.entity.TeamInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamInviteRepository extends JpaRepository<TeamInvite, Long> {
    List<TeamInvite> getAllByInvitedIdAndInviteStatus(Long invitedId, TeamInviteStatus inviteStatus);

    List<TeamInvite> getAllByInvitingIdAndInviteStatus(Long invitingId, TeamInviteStatus inviteStatus);

    List<TeamInvite> getAllByInvitedIdAndTeamIdAndInviteStatus(
            Long invitedId,
            Long teamId,
            TeamInviteStatus inviteStatus
    );

    List<TeamInvite> getAllByInvitingIdAndTeamIdAndInviteStatus(
            Long invitingId,
            Long teamId,
            TeamInviteStatus inviteStatus
    );
}
