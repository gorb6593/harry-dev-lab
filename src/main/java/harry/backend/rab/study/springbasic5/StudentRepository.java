package harry.backend.rab.study.springbasic5;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

// Repository는 'DB에 어떻게 저장하고 조회하는가'를 담당한다.
// Controller와 Service는 SQL이나 JDBC 객체를 몰라도 된다.
@Repository
public class StudentRepository {
    private final JdbcTemplate jdbcTemplate;

    public StudentRepository(JdbcTemplate jdbcTemplate) {
        // Spring Boot가 application.yml의 DataSource를 이용해 만든 JdbcTemplate을 주입한다.
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Student> findAll() {
        // query(): SELECT 결과의 각 행을 RowMapper로 Student 객체로 바꾼다.
        return jdbcTemplate.query("""
                SELECT id, student_number, student_name, created_at
                FROM springbasic5_student
                ORDER BY id
                """, studentRowMapper());
    }

    public Optional<Student> findById(Long id) {
        // ? 자리에 id를 바인딩한다. 문자열을 직접 이어 붙이지 않아 SQL Injection을 막는다.
        List<Student> students = jdbcTemplate.query("""
                SELECT id, student_number, student_name, created_at
                FROM springbasic5_student
                WHERE id = ?
                """, studentRowMapper(), id);
        return students.stream().findFirst();
    }

    public Student save(String studentNumber, String studentName) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // update(): INSERT를 실행한다. DB가 생성한 AUTO_INCREMENT ID도 함께 요청한다.
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement("""
                        INSERT INTO springbasic5_student (student_number, student_name)
                        VALUES (?, ?)
                        """, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, studentNumber);
                statement.setString(2, studentName);
                return statement;
            }, keyHolder);
        } catch (DuplicateKeyException exception) {
            // UNIQUE 제약 조건 위반이라는 DB 예외를 애플리케이션 예외로 변환한다.
            throw new StudentNumberAlreadyExistsException(studentNumber);
        }

        Long generatedId = keyHolder.getKey().longValue();
        return findById(generatedId).orElseThrow();
    }

    public boolean update(Long id, String studentNumber, String studentName) {
        // update()의 반환값은 실제로 수정된 행의 개수다.
        // 0이면 해당 id의 학생이 없다고 판단할 수 있다.
        try {
            int changedRows = jdbcTemplate.update("""
                    UPDATE springbasic5_student
                    SET student_number = ?, student_name = ?
                    WHERE id = ?
                    """, studentNumber, studentName, id);
            return changedRows == 1;
        } catch (DuplicateKeyException exception) {
            throw new StudentNumberAlreadyExistsException(studentNumber);
        }
    }

    public boolean delete(Long id) {
        // DELETE 역시 삭제된 행 수로 대상 존재 여부를 판단한다.
        return jdbcTemplate.update(
                "DELETE FROM springbasic5_student WHERE id = ?", id) == 1;
    }

    private org.springframework.jdbc.core.RowMapper<Student> studentRowMapper() {
        // ResultSet은 DB의 한 행이다. 컬럼명을 Java 필드에 직접 매핑한다.
        return (resultSet, rowNumber) -> new Student(
                resultSet.getLong("id"),
                resultSet.getString("student_number"),
                resultSet.getString("student_name"),
                resultSet.getTimestamp("created_at").toLocalDateTime());
    }
}
