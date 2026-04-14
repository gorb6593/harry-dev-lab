package harry.backend.rab.jpaLevel2.dto;

public record ArticleIdentityResponse(
        String question,
        Long articleId,
        boolean sameInstanceInTransaction,
        int firstIdentityHash,
        int secondIdentityHash,
        boolean firstManaged,
        boolean secondManaged,
        String observation,
        String nextQuestion
) {
}
