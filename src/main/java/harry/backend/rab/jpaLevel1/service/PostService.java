package harry.backend.rab.jpaLevel1.service;

import harry.backend.rab.jpaLevel1.dto.PostRequest;
import harry.backend.rab.jpaLevel1.dto.PostResponse;
import harry.backend.rab.jpaLevel1.dto.PostSaveFlowRequest;
import harry.backend.rab.jpaLevel1.dto.PostSaveFlowResponse;
import harry.backend.rab.jpaLevel1.entity.Post;
import harry.backend.rab.jpaLevel1.repository.PostRepository;
import harry.backend.rab.jpaLevel6.exception.PostNotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        return postRepository.findById(id)
                .map(PostResponse::from)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    public PostResponse createPost(PostRequest request) {
        Post post = new Post(request.title(), request.content());
        Post saved = postRepository.save(post);
        return PostResponse.from(saved);
    }

    public PostSaveFlowResponse inspectSaveFlow(PostSaveFlowRequest request) {
        Post post = new Post(request.title(), request.content());
        Long idBeforeSave = post.getId();

        Post savedPost = postRepository.save(post);
        Long idAfterSave = savedPost.getId();
        boolean managedAfterSave = entityManager.contains(savedPost);

        if (request.flushImmediately()) {
            entityManager.flush();
        }

        return new PostSaveFlowResponse(
                "save() 이후 insert SQL은 언제 나가는가?",
                idBeforeSave,
                idAfterSave,
                managedAfterSave,
                request.flushImmediately(),
                "현재 Post 엔티티는 IDENTITY 전략이므로, 보통 PK를 얻기 위해 save 시점에 insert SQL이 먼저 실행된다. 콘솔 SQL 로그로 바로 확인한다.",
                "다음 질문: flush()를 명시적으로 호출하면 무엇이 달라지는가?"
        );
    }

    public PostResponse updatePost(Long id, PostRequest request) {
        return postRepository.findById(id)
                .map(post -> {
                    post.update(request.title(), request.content());
                    return PostResponse.from(post);
                })
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    public void deletePost(Long id) {
        postRepository.findById(id)
                .ifPresent(postRepository::delete);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> searchPosts(String keyword) {
        return postRepository.findByTitleContaining(keyword).stream()
                .map(PostResponse::from)
                .toList();
    }
}
