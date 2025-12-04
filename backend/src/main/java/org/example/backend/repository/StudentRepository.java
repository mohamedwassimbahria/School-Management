package org.example.backend.repository;

import org.example.backend.entity.Student;
import org.example.backend.enums.Level;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    Page<Student> findByLevel(Level level, Pageable pageable);
    Page<Student> findByUsernameContainingIgnoreCaseAndLevel(String username, Level level, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE (:searchTerm IS NULL OR CAST(s.id AS string) = :searchTerm OR LOWER(s.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Student> findByIdOrUsernameContainingIgnoreCase(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE (:level IS NULL OR s.level = :level) AND (:searchTerm IS NULL OR CAST(s.id AS string) = :searchTerm OR LOWER(s.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Student> findBySearchTermAndLevel(@Param("searchTerm") String searchTerm, @Param("level") Level level, Pageable pageable);
}
