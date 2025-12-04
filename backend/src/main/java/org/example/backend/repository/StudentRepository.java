package org.example.backend.repository;

import org.example.backend.entity.Student;
import org.example.backend.enums.Level;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    Page<Student> findByLevel(Level level, Pageable pageable);
    Page<Student> findByUsernameContainingIgnoreCaseAndLevel(String username, Level level, Pageable pageable);
    Page<Student> findByIdOrUsernameContainingIgnoreCase(Long id, String username, Pageable pageable);
    Page<Student> findByIdAndLevelOrUsernameContainingIgnoreCaseAndLevel(Long id, Level level, String username, Level level2, Pageable pageable);
}
