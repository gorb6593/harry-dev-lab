package harry.backend.rab.study.springbasic3;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// 메모 저장·조회·수정·삭제를 담당한다. HTTP에 대해서는 전혀 모른다.
// springbasic2.MemoService와 Bean 이름(memoService)이 겹치므로 명시적으로 이름을 준다.
@Service("springBasic3MemoService")
public class MemoService {

    private final AtomicLong idGenerator = new AtomicLong(1);
    private final ConcurrentHashMap<Long, Memo> memos = new ConcurrentHashMap<>();

    public List<Memo> findAll() {
        return memos.values().stream()
                .sorted(Comparator.comparing(Memo::id))
                .toList();
    }

    public Memo findById(Long id) {
        Memo memo = memos.get(id);
        if (memo == null) {
            throw new MemoNotFoundException(id);
        }
        return memo;
    }

    public Memo create(String content) {
        Long id = idGenerator.getAndIncrement();
        Memo memo = new Memo(id, content);
        memos.put(id, memo);
        return memo;
    }

    public Memo update(Long id, String content) {
        Memo updated = findById(id).withContent(content);
        memos.put(id, updated);
        return updated;
    }

    public void delete(Long id) {
        findById(id); // 없으면 예외
        memos.remove(id);
    }
}
