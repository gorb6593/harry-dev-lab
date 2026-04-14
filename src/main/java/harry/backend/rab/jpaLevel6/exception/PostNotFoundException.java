package harry.backend.rab.jpaLevel6.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(Long postId) {
        super("Post not found. id=" + postId);
    }
}
