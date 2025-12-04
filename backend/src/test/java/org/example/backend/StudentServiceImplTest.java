package org.example.backend;


import org.example.backend.dto.StudentCreateUpdateDto;
import org.example.backend.dto.StudentDto;
import org.example.backend.entity.Student;
import org.example.backend.enums.Level;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.repository.StudentRepository;
import org.example.backend.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class StudentServiceImplTest {


    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    @Test
    void getAllStudents_shouldReturnAllStudents() {
        Student student = new Student();
        student.setUsername("student");
        student.setLevel(Level.FIRST_GRADE);
        studentRepository.save(student);

        Page<StudentDto> students = studentService.getAllStudents(null, null, Pageable.unpaged());

        assertEquals(1, students.getTotalElements());
        assertEquals("student", students.getContent().get(0).getUsername());
    }

    @Test
    void getStudentById_shouldReturnStudent() {
        Student student = new Student();
        student.setUsername("student");
        student.setLevel(Level.FIRST_GRADE);
        student = studentRepository.save(student);

        StudentDto studentDto = studentService.getStudentById(student.getId());

        assertEquals("student", studentDto.getUsername());
    }

    @Test
    void getStudentById_shouldThrowResourceNotFoundException_whenStudentNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(1L));
    }

    @Test
    void createStudent_shouldCreateStudent() {
        StudentCreateUpdateDto dto = new StudentCreateUpdateDto();
        dto.setUsername("student");
        dto.setLevel(Level.FIRST_GRADE);

        StudentDto studentDto = studentService.createStudent(dto);

        assertEquals("student", studentDto.getUsername());
        assertTrue(studentRepository.existsById(studentDto.getId()));
    }

    @Test
    void updateStudent_shouldUpdateStudent() {
        Student student = new Student();
        student.setUsername("student");
        student.setLevel(Level.FIRST_GRADE);
        student = studentRepository.save(student);

        StudentCreateUpdateDto dto = new StudentCreateUpdateDto();
        dto.setUsername("updatedStudent");
        dto.setLevel(Level.SECOND_GRADE);

        StudentDto studentDto = studentService.updateStudent(student.getId(), dto);

        assertEquals("updatedStudent", studentDto.getUsername());
        assertEquals(Level.SECOND_GRADE, studentDto.getLevel());
    }

    @Test
    void updateStudent_shouldThrowResourceNotFoundException_whenStudentNotFound() {
        StudentCreateUpdateDto dto = new StudentCreateUpdateDto();
        dto.setUsername("student");
        dto.setLevel(Level.FIRST_GRADE);

        assertThrows(ResourceNotFoundException.class, () -> studentService.updateStudent(1L, dto));
    }

    @Test
    void deleteStudent_shouldDeleteStudent() {
        Student student = new Student();
        student.setUsername("student");
        student.setLevel(Level.FIRST_GRADE);
        student = studentRepository.save(student);

        studentService.deleteStudent(student.getId());

        assertFalse(studentRepository.existsById(student.getId()));
    }

    @Test
    void deleteStudent_shouldThrowResourceNotFoundException_whenStudentNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent(1L));
    }
}
