package harry.backend.rab.jpaLevel2.repository;

import harry.backend.rab.jpaLevel2.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
