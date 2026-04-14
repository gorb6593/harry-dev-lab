package harry.backend.rab.jpaLevel1.repository;

import harry.backend.rab.jpaLevel1.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitleContaining(String keyword);
}
