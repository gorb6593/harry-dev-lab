package harry.backend.rab.jpaLevel3.repository;

import harry.backend.rab.jpaLevel3.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
