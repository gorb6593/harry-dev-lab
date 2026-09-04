package harry.backend.rab.study.springbasic6;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

// HTTP 요청을 관찰 실습 코드로 연결하는 입구다.
@RestController("springBasic6ConnectionObservationController")
@RequestMapping("/study/spring-basic6/connection")
public class ConnectionObservationController {
    private final ConnectionObservationService service;

    public ConnectionObservationController(ConnectionObservationService service) {
        this.service = service;
    }

    @GetMapping("/observe")
    public ConnectionObservationResponse observe() {
        return service.observe();
    }

    @PostMapping("/hold")
    public ResponseEntity<Map<String, Object>> hold(@RequestParam int seconds) {
        int activeBefore = service.activeConnections();
        service.holdConnection(seconds);
        int activeAfter = service.activeConnections();

        return ResponseEntity.ok(Map.of(
                "message", "커넥션을 점유했다가 반환했습니다.",
                "seconds", seconds,
                "active_connections_before", activeBefore,
                "active_connections_after", activeAfter));
    }

    @GetMapping("/pool")
    public Map<String, Object> pool() {
        return Map.of(
                "active_connections", service.activeConnections(),
                "idle_connections", service.idleConnections());
    }
}
