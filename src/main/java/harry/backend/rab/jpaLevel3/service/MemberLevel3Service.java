package harry.backend.rab.jpaLevel3.service;

import harry.backend.rab.jpaLevel3.dto.MemberCreateRequest;
import harry.backend.rab.jpaLevel3.dto.MemberDirtyCheckingRequest;
import harry.backend.rab.jpaLevel3.dto.MemberDirtyCheckingResponse;
import harry.backend.rab.jpaLevel3.dto.MemberResponse;
import harry.backend.rab.jpaLevel3.entity.Member;
import harry.backend.rab.jpaLevel3.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberLevel3Service {

    private final MemberRepository memberRepository;
    private final EntityManager entityManager;

    public MemberResponse createMember(MemberCreateRequest request) {
        Member member = new Member(request.username());
        Member savedMember = memberRepository.save(member);
        return MemberResponse.from(savedMember);
    }

    public MemberDirtyCheckingResponse inspectDirtyChecking(Long memberId, MemberDirtyCheckingRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found. id=" + memberId));

        String beforeUsername = member.getUsername();
        member.changeUsername(request.newUsername());

        if (request.flushImmediately()) {
            entityManager.flush();
        }

        return new MemberDirtyCheckingResponse(
                "영속 상태 엔티티의 값을 바꾸면 왜 update SQL은 즉시가 아니라 flush 시점에 나가는가?",
                memberId,
                beforeUsername,
                member.getUsername(),
                entityManager.contains(member),
                request.flushImmediately(),
                "값 변경 시점에는 메모리의 영속 엔티티 상태만 바뀐다. update SQL은 flush 시점에 dirty checking으로 감지되어 실행된다. flush를 생략해도 트랜잭션 commit 직전에 자동 flush가 일어난다.",
                "다음 질문: detach()를 호출하면 왜 변경 감지가 더 이상 반영되지 않는가?"
        );
    }
}
