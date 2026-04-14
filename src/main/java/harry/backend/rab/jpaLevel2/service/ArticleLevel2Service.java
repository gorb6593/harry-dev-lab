package harry.backend.rab.jpaLevel2.service;

import harry.backend.rab.jpaLevel2.dto.ArticleCreateRequest;
import harry.backend.rab.jpaLevel2.dto.ArticleIdentityResponse;
import harry.backend.rab.jpaLevel2.dto.ArticleResponse;
import harry.backend.rab.jpaLevel2.entity.Article;
import harry.backend.rab.jpaLevel2.repository.ArticleRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleLevel2Service {

    private final ArticleRepository articleRepository;
    private final EntityManager entityManager;

    public ArticleResponse createArticle(ArticleCreateRequest request) {
        Article article = new Article(request.title(), request.content());
        Article savedArticle = articleRepository.save(article);
        return ArticleResponse.from(savedArticle);
    }

    @Transactional(readOnly = true)
    public ArticleIdentityResponse inspectSameInstance(Long articleId) {
        Article firstLoad = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found. id=" + articleId));
        Article secondLoad = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found. id=" + articleId));

        return new ArticleIdentityResponse(
                "같은 트랜잭션에서 같은 엔티티를 두 번 조회하면 왜 같은 객체처럼 보이는가?",
                articleId,
                firstLoad == secondLoad,
                System.identityHashCode(firstLoad),
                System.identityHashCode(secondLoad),
                entityManager.contains(firstLoad),
                entityManager.contains(secondLoad),
                "같은 트랜잭션 안에서는 같은 영속성 컨텍스트가 같은 PK의 엔티티를 1차 캐시에 보관하므로 같은 인스턴스를 반환한다.",
                "다음 질문: clear()를 호출하면 왜 다시 select가 나가는가?"
        );
    }
}
