package harry.backend.rab.study.springbasic3;

// 응답 DTO. 내부 Memo를 그대로 내보내지 않고 응답 전용 형태로 변환한다.
// 지금은 필드가 같지만, 실무에서는 내부 필드(비밀번호, 내부 상태 등)를 감추는 경계가 된다.
public record MemoResponse(
        Long id,
        String content
) {
    public static MemoResponse from(Memo memo) {
        return new MemoResponse(memo.id(), memo.content());
    }
}
