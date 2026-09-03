package harry.backend.rab.study.springbasic4;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class MemoRepository {
    private final JdbcTemplate jdbcTemplate;

    public MemoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Memo> findAll() {
        return jdbcTemplate.query("""
                SELECT id, content, created_at
                FROM springbasic4_memo
                ORDER BY id
                """, memoRowMapper());
    }

    public Optional<Memo> findById(Long id) {
        return jdbcTemplate.query("""
                SELECT id, content, created_at
                FROM springbasic4_memo
                WHERE id = ?
                """, memoRowMapper(), id).stream().findFirst();
    }

    public Memo save(String content) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO springbasic4_memo (content) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, content);
            return statement;
        }, keyHolder);
        return findById(keyHolder.getKey().longValue()).orElseThrow();
    }

    public boolean update(Long id, String content) {
        return jdbcTemplate.update("""
                UPDATE springbasic4_memo
                SET content = ?
                WHERE id = ?
                """, content, id) == 1;
    }

    public boolean delete(Long id) {
        return jdbcTemplate.update("DELETE FROM springbasic4_memo WHERE id = ?", id) == 1;
    }

    private org.springframework.jdbc.core.RowMapper<Memo> memoRowMapper() {
        return (rs, rowNum) -> new Memo(
                rs.getLong("id"),
                rs.getString("content"),
                rs.getTimestamp("created_at").toLocalDateTime());
    }
}
