package harry.backend.rab.study.springbasic4;

import org.springframework.stereotype.Service;

import java.util.List;

@Service("springBasic4MemoService")
public class MemoService {
    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    public List<Memo> findAll() {
        return memoRepository.findAll();
    }

    public Memo findById(Long id) {
        return memoRepository.findById(id).orElseThrow(() -> new MemoNotFoundException(id));
    }

    public Memo create(String content) {
        return memoRepository.save(content);
    }

    public Memo update(Long id, String content) {
        if (!memoRepository.update(id, content)) {
            throw new MemoNotFoundException(id);
        }
        return findById(id);
    }

    public void delete(Long id) {
        if (!memoRepository.delete(id)) {
            throw new MemoNotFoundException(id);
        }
    }
}
