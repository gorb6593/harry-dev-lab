package harry.backend.rab.jpaLevel4.dto;

import harry.backend.rab.jpaLevel4.entity.Team;

public record TeamResponse(
        Long id,
        String name
) {
    public static TeamResponse from(Team team) {
        return new TeamResponse(team.getId(), team.getName());
    }
}
