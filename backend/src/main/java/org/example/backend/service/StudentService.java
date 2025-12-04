package org.example.backend.service;


import org.example.backend.dto.StudentCreateUpdateDto;
import org.example.backend.dto.StudentDto;
import org.example.backend.enums.Level;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.Writer;

public interface StudentService {
    Page<StudentDto> getAllStudents(String searchTerm, Level level, Pageable pageable);
    StudentDto getStudentById(Long id);
    StudentDto createStudent(StudentCreateUpdateDto dto);
    StudentDto updateStudent(Long id, StudentCreateUpdateDto dto);
    void deleteStudent(Long id);
    void exportStudents(Writer writer);
    void importStudents(MultipartFile file);
}
