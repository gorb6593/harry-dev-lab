package harry.backend.rab.jpaLevel4.controller;

import harry.backend.rab.jpaLevel4.dto.RelationOwnerResponse;
import harry.backend.rab.jpaLevel4.dto.TeamCreateRequest;
import harry.backend.rab.jpaLevel4.dto.TeamMemberCreateRequest;
import harry.backend.rab.jpaLevel4.dto.TeamMemberResponse;
import harry.backend.rab.jpaLevel4.dto.TeamResponse;
import harry.backend.rab.jpaLevel4.service.RelationLevel4Service;
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
@RequestMapping("/api/jpaLevel4")
public class RelationLevel4Controller {

    private final RelationLevel4Service relationLevel4Service;

    @PostMapping("/teams")
    public ResponseEntity<TeamResponse> createTeam(@RequestBody TeamCreateRequest request) {
        TeamResponse response = relationLevel4Service.createTeam(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/teamMembers")
    public ResponseEntity<TeamMemberResponse> createTeamMember(@RequestBody TeamMemberCreateRequest request) {
        TeamMemberResponse response = relationLevel4Service.createTeamMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/teamMembers/{memberId}/owner")
    public RelationOwnerResponse inspectOwner(@PathVariable Long memberId) {
        return relationLevel4Service.inspectOwner(memberId);
    }
}
