package harry.backend.rab.jpaLevel4.repository;

import harry.backend.rab.jpaLevel4.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
}
