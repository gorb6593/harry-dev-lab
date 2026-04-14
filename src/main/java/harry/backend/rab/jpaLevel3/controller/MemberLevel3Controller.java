package harry.backend.rab.jpaLevel3.controller;

import harry.backend.rab.jpaLevel3.dto.MemberCreateRequest;
import harry.backend.rab.jpaLevel3.dto.MemberDirtyCheckingRequest;
import harry.backend.rab.jpaLevel3.dto.MemberDirtyCheckingResponse;
import harry.backend.rab.jpaLevel3.dto.MemberResponse;
import harry.backend.rab.jpaLevel3.service.MemberLevel3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jpaLevel3/members")
public class MemberLevel3Controller {

    private final MemberLevel3Service memberLevel3Service;

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberCreateRequest request) {
        MemberResponse response = memberLevel3Service.createMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{memberId}/dirtyChecking")
    public MemberDirtyCheckingResponse inspectDirtyChecking(
            @PathVariable Long memberId,
            @RequestBody MemberDirtyCheckingRequest request
    ) {
        return memberLevel3Service.inspectDirtyChecking(memberId, request);
    }
}
