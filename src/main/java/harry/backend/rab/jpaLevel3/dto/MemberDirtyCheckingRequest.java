package harry.backend.rab.jpaLevel3.dto;

public record MemberDirtyCheckingRequest(
        String newUsername,
        boolean flushImmediately
) {
}
