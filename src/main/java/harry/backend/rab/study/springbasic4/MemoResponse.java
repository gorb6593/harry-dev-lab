package harry.backend.rab.study.springbasic4;

import java.time.LocalDateTime;

public record MemoResponse(Long id, String content, LocalDateTime createdAt) {
    public static MemoResponse from(Memo memo) {
        return new MemoResponse(memo.id(), memo.content(), memo.createdAt());
    }
}
