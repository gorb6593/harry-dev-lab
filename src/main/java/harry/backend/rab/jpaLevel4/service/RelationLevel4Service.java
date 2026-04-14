package harry.backend.rab.jpaLevel4.service;

import harry.backend.rab.jpaLevel4.dto.RelationOwnerResponse;
import harry.backend.rab.jpaLevel4.dto.TeamCreateRequest;
import harry.backend.rab.jpaLevel4.dto.TeamMemberCreateRequest;
import harry.backend.rab.jpaLevel4.dto.TeamMemberResponse;
import harry.backend.rab.jpaLevel4.dto.TeamResponse;
import harry.backend.rab.jpaLevel4.entity.Team;
import harry.backend.rab.jpaLevel4.entity.TeamMember;
import harry.backend.rab.jpaLevel4.repository.TeamMemberRepository;
import harry.backend.rab.jpaLevel4.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RelationLevel4Service {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    public TeamResponse createTeam(TeamCreateRequest request) {
        Team savedTeam = teamRepository.save(Team.create(request.name()));
        return TeamResponse.from(savedTeam);
    }

    public TeamMemberResponse createTeamMember(TeamMemberCreateRequest request) {
        Team team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new IllegalArgumentException("Team not found. id=" + request.teamId()));

        TeamMember teamMember = TeamMember.create(request.username());
        teamMember.assignTeam(team);

        TeamMember savedTeamMember = teamMemberRepository.save(teamMember);
        return TeamMemberResponse.from(savedTeamMember);
    }

    @Transactional(readOnly = true)
    public RelationOwnerResponse inspectOwner(Long memberId) {
        TeamMember teamMember = teamMemberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("TeamMember not found. id=" + memberId));

        Team team = teamMember.getTeam();

        return new RelationOwnerResponse(
                "연관관계 주인은 왜 외래 키가 있는 쪽인가?",
                teamMember.getId(),
                teamMember.getUsername(),
                team != null ? team.getId() : null,
                team != null ? team.getName() : null,
                "team_id",
                "TeamMember.team",
                "DB 외래 키는 member 테이블의 team_id 컬럼에 있으므로, 그 컬럼을 직접 가지는 TeamMember 쪽이 연관관계 주인이 된다. 즉 연관관계 변경은 외래 키를 가진 쪽에서 관리해야 실제 DB 값과 매핑 책임이 일치한다.",
                "다음 질문: 양방향으로 열면 mappedBy는 왜 필요하고, 왜 주인이 아닌 쪽은 읽기 전용처럼 동작하는가?"
        );
    }
}
