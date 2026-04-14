package harry.backend.rab.jpaLevel1.dto;

public record PostSaveFlowRequest(
        String title,
        String content,
        boolean flushImmediately
) {
}
