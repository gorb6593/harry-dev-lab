package harry.backend.rab.study.springbasic6;

import com.fasterxml.jackson.annotation.JsonProperty;

// Controller가 반환하는 관찰 결과다.
// 내부 Java 이름은 camelCase로 유지하고 API JSON은 camel_case로 표현한다.
public record ConnectionObservationResponse(
        @JsonProperty("connection_identity") String connectionIdentity,
        @JsonProperty("jdbc_url") String jdbcUrl,
        @JsonProperty("database_product") String databaseProduct,
        @JsonProperty("database_version") String databaseVersion,
        @JsonProperty("catalog") String catalog,
        @JsonProperty("auto_commit") boolean autoCommit,
        @JsonProperty("read_only") boolean readOnly,
        @JsonProperty("transaction_isolation") String transactionIsolation,
        @JsonProperty("active_connections_before") int activeConnectionsBefore,
        @JsonProperty("active_connections_inside") int activeConnectionsInside,
        @JsonProperty("active_connections_after") int activeConnectionsAfter
) {
}
