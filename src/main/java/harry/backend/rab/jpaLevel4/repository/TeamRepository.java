package harry.backend.rab.jpaLevel4.repository;

import harry.backend.rab.jpaLevel4.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
