package harry.backend.rab.jpaLevel5.service;

import harry.backend.rab.jpaLevel4.entity.Team;
import harry.backend.rab.jpaLevel4.entity.TeamMember;
import harry.backend.rab.jpaLevel4.repository.TeamMemberRepository;
import harry.backend.rab.jpaLevel5.dto.LazyLoadingResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryLevel5Service {

    private final TeamMemberRepository teamMemberRepository;
    private final EntityManager entityManager;

    public LazyLoadingResponse inspectLazyLoading(Long memberId) {
        TeamMember teamMember = teamMemberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("TeamMember not found. id=" + memberId));

        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        Team team = teamMember.getTeam();

        boolean teamLoadedBeforeAccess = persistenceUnitUtil.isLoaded(team);
        String teamName = team.getName();
        boolean teamLoadedAfterAccess = persistenceUnitUtil.isLoaded(team);

        return new LazyLoadingResponse(
                "지연 로딩은 언제 실제 쿼리를 발생시키는가?",
                memberId,
                teamLoadedBeforeAccess,
                teamLoadedAfterAccess,
                teamName,
                "LAZY 연관관계는 조회 직후 실제 엔티티 대신 프록시일 수 있다. 연관 객체의 필드에 실제 접근하는 시점에 초기화가 일어나며, 그때 추가 select가 실행된다.",
                "다음 질문: 목록 조회에서 연관 객체를 반복 접근하면 왜 N+1 문제가 생기는가?"
        );
    }
}
