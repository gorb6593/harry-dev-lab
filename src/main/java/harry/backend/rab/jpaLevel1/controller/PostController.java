package harry.backend.rab.jpaLevel1.controller;

import harry.backend.rab.jpaLevel1.dto.PostRequest;
import harry.backend.rab.jpaLevel1.dto.PostResponse;
import harry.backend.rab.jpaLevel1.dto.PostSaveFlowRequest;
import harry.backend.rab.jpaLevel1.dto.PostSaveFlowResponse;
import harry.backend.rab.jpaLevel1.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jpaLevel1/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<PostResponse> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest request) {
        PostResponse response = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.updatePost(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public List<PostResponse> searchPosts(@RequestParam String keyword) {
        return postService.searchPosts(keyword);
    }

    @PostMapping("/saveFlow")
    public ResponseEntity<PostSaveFlowResponse> inspectSaveFlow(@RequestBody PostSaveFlowRequest request) {
        PostSaveFlowResponse response = postService.inspectSaveFlow(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
