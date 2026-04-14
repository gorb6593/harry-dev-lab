package harry.backend.rab.jpaLevel2.dto;

import harry.backend.rab.jpaLevel2.entity.Article;

public record ArticleResponse(
        Long id,
        String title,
        String content
) {
    public static ArticleResponse from(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent()
        );
    }
}
