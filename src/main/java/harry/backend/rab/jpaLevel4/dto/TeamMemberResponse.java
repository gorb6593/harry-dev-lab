package harry.backend.rab.jpaLevel4.dto;

import harry.backend.rab.jpaLevel4.entity.TeamMember;

public record TeamMemberResponse(
        Long id,
        String username,
        Long teamId,
        String teamName
) {
    public static TeamMemberResponse from(TeamMember teamMember) {
        Long teamId = teamMember.getTeam() != null ? teamMember.getTeam().getId() : null;
        String teamName = teamMember.getTeam() != null ? teamMember.getTeam().getName() : null;

        return new TeamMemberResponse(
                teamMember.getId(),
                teamMember.getUsername(),
                teamId,
                teamName
        );
    }
}
