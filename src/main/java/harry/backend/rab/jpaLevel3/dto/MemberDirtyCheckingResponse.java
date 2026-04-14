package harry.backend.rab.jpaLevel3.dto;

public record MemberDirtyCheckingResponse(
        String question,
        Long memberId,
        String beforeUsername,
        String changedUsernameInMemory,
        boolean managedBeforeFlush,
        boolean flushCalled,
        String observation,
        String nextQuestion
) {
}
