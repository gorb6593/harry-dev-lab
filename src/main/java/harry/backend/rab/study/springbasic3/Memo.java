package harry.backend.rab.study.springbasic3;

// 서버 내부에서 보관하는 메모 데이터다.
// 외부에 그대로 노출하지 않고, 응답할 때는 MemoResponse로 변환한다.
public record Memo(
        Long id,
        String content
) {
    // record는 불변이므로 내용을 바꾸려면 새 객체를 만든다.
    public Memo withContent(String newContent) {
        return new Memo(id, newContent);
    }
}
