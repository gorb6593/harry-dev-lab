package harry.backend.rab.jpaLevel4.dto;

public record TeamMemberCreateRequest(
        String username,
        Long teamId
) {
}
