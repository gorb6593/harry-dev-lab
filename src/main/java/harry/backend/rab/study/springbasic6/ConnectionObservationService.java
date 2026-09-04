package harry.backend.rab.study.springbasic6;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

// 이 클래스는 DB 비즈니스 로직이 아니라 '커넥션 생명주기 관찰'을 위한 실습 도구다.
@Service("springBasic6ConnectionObservationService")
public class ConnectionObservationService {
    private final JdbcTemplate jdbcTemplate;
    private final HikariDataSource hikariDataSource;

    public ConnectionObservationService(
            JdbcTemplate jdbcTemplate,
            HikariDataSource hikariDataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.hikariDataSource = hikariDataSource;
    }

    public ConnectionObservationResponse observe() {
        int before = activeConnections();

        // execute()에 전달한 콜백이 실행될 때 JdbcTemplate이 DataSource에서 커넥션을 빌린다.
        // 콜백이 끝나면 JdbcTemplate이 커넥션을 정리하고 HikariCP에 반환한다.
        ConnectionObservationResponse result = jdbcTemplate.execute((ConnectionCallback<ConnectionObservationResponse>) connection -> {
            int inside = activeConnections();
            DatabaseMetaData metadata = connection.getMetaData();

            return new ConnectionObservationResponse(
                    connectionIdentity(connection),
                    metadata.getURL(),
                    metadata.getDatabaseProductName(),
                    metadata.getDatabaseProductVersion(),
                    connection.getCatalog(),
                    connection.getAutoCommit(),
                    connection.isReadOnly(),
                    isolationName(connection.getTransactionIsolation()),
                    before,
                    inside,
                    -1 // 콜백이 끝난 뒤의 풀 상태는 아래에서 다시 읽는다.
            );
        });

        // 이 시점에는 JdbcTemplate의 콜백이 끝났으므로 커넥션이 풀에 반환된 상태다.
        return new ConnectionObservationResponse(
                result.connectionIdentity(),
                result.jdbcUrl(),
                result.databaseProduct(),
                result.databaseVersion(),
                result.catalog(),
                result.autoCommit(),
                result.readOnly(),
                result.transactionIsolation(),
                result.activeConnectionsBefore(),
                result.activeConnectionsInside(),
                activeConnections());
    }

    public void holdConnection(int seconds) {
        if (seconds < 1 || seconds > 30) {
            throw new IllegalArgumentException("seconds는 1부터 30까지 입력할 수 있습니다.");
        }

        // 커넥션을 빌린 상태에서 잠시 멈춘다.
        // 다른 요청을 동시에 보내면 커넥션 풀의 active 개수 변화를 관찰할 수 있다.
        jdbcTemplate.execute((Connection connection) -> {
            try {
                TimeUnit.SECONDS.sleep(seconds);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("커넥션 점유 실습이 중단되었습니다.", exception);
            }
            return null;
        });
    }

    public int activeConnections() {
        // HikariPoolMXBean은 현재 풀에서 사용 중인 커넥션 수를 보여준다.
        return hikariDataSource.getHikariPoolMXBean().getActiveConnections();
    }

    public int idleConnections() {
        return hikariDataSource.getHikariPoolMXBean().getIdleConnections();
    }

    private String connectionIdentity(Connection connection) {
        return connection.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(connection));
    }

    private String isolationName(int isolation) {
        return switch (isolation) {
            case Connection.TRANSACTION_NONE -> "NONE";
            case Connection.TRANSACTION_READ_UNCOMMITTED -> "READ_UNCOMMITTED";
            case Connection.TRANSACTION_READ_COMMITTED -> "READ_COMMITTED";
            case Connection.TRANSACTION_REPEATABLE_READ -> "REPEATABLE_READ";
            case Connection.TRANSACTION_SERIALIZABLE -> "SERIALIZABLE";
            default -> "UNKNOWN(" + isolation + ")";
        };
    }
}
