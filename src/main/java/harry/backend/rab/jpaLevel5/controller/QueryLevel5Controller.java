package harry.backend.rab.jpaLevel5.controller;

import harry.backend.rab.jpaLevel5.dto.LazyLoadingResponse;
import harry.backend.rab.jpaLevel5.service.QueryLevel5Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jpaLevel5/teamMembers")
public class QueryLevel5Controller {

    private final QueryLevel5Service queryLevel5Service;

    @GetMapping("/{memberId}/lazyLoading")
    public LazyLoadingResponse inspectLazyLoading(@PathVariable Long memberId) {
        return queryLevel5Service.inspectLazyLoading(memberId);
    }
}
