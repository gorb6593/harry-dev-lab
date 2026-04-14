package harry.backend.rab.jpaLevel4.dto;

public record RelationOwnerResponse(
        String question,
        Long memberId,
        String memberUsername,
        Long teamId,
        String teamName,
        String foreignKeyColumn,
        String ownerSide,
        String observation,
        String nextQuestion
) {
}
