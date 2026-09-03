package harry.backend.rab.study.springbasic3;

// "없는 메모"라는 상황을 표현하는 예외.
// Service는 HTTP를 모르므로 상태 코드를 여기서 정하지 않는다.
// HTTP 404로 바꾸는 일은 MemoExceptionHandler가 담당한다.
public class MemoNotFoundException extends RuntimeException {

    private final Long id;

    public MemoNotFoundException(Long id) {
        super("메모를 찾을 수 없습니다. id=" + id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
