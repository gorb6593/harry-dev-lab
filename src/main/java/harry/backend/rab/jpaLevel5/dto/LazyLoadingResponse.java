package harry.backend.rab.jpaLevel5.dto;

public record LazyLoadingResponse(
        String question,
        Long memberId,
        boolean teamLoadedBeforeAccess,
        boolean teamLoadedAfterAccess,
        String teamName,
        String observation,
        String nextQuestion
) {
}
