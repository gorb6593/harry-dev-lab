package harry.backend.rab.study.springbasic2;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// @Service: 메모를 저장하고 조회하는 기능을 담당하는 Bean이다.
@Service("springBasic2MemoService")
public class MemoService {

    private final AtomicLong idGenerator = new AtomicLong(1);
    private final ConcurrentHashMap<Long, Memo> memos = new ConcurrentHashMap<>();

    // GET 요청에서 호출할 조회 기능
    public List<Memo> findAll() {
        return memos.values().stream().toList();
    }

    // POST 요청에서 호출할 생성 기능
    public Memo create(String content) {
        Long id = idGenerator.getAndIncrement();
        Memo memo = new Memo(id, content);
        memos.put(id, memo);
        return memo;
    }
}
