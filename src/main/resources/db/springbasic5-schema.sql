CREATE TABLE IF NOT EXISTS springbasic5_student (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_number VARCHAR(30) NOT NULL,
    student_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_springbasic5_student_number UNIQUE (student_number)
);
