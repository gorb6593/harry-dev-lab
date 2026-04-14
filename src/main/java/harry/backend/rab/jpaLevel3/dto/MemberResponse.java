package harry.backend.rab.jpaLevel3.dto;

import harry.backend.rab.jpaLevel3.entity.Member;

public record MemberResponse(
        Long id,
        String username
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getUsername());
    }
}
