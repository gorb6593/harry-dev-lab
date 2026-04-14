package harry.backend.rab.jpaLevel1.dto;

public record PostSaveFlowResponse(
        String question,
        Long idBeforeSave,
        Long idAfterSave,
        boolean managedAfterSave,
        boolean flushCalled,
        String observation,
        String nextQuestion
) {
}
