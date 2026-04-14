package harry.backend.rab.jpaLevel2.controller;

import harry.backend.rab.jpaLevel2.dto.ArticleCreateRequest;
import harry.backend.rab.jpaLevel2.dto.ArticleIdentityResponse;
import harry.backend.rab.jpaLevel2.dto.ArticleResponse;
import harry.backend.rab.jpaLevel2.service.ArticleLevel2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jpaLevel2/articles")
public class ArticleLevel2Controller {

    private final ArticleLevel2Service articleLevel2Service;

    @PostMapping
    public ResponseEntity<ArticleResponse> createArticle(@RequestBody ArticleCreateRequest request) {
        ArticleResponse response = articleLevel2Service.createArticle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{articleId}/identity")
    public ArticleIdentityResponse inspectSameInstance(@PathVariable Long articleId) {
        return articleLevel2Service.inspectSameInstance(articleId);
    }
}
