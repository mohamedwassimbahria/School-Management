package org.example.backend;


import org.example.backend.dto.StudentCreateUpdateDto;
import org.example.backend.dto.StudentDto;
import org.example.backend.entity.Student;
import org.example.backend.enums.Level;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.mapper.StudentMapper;
import org.example.backend.repository.StudentRepository;
import org.example.backend.service.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private StudentDto studentDto;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setUsername("student");
        student.setLevel(Level.FIRST_GRADE);

        studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setUsername("student");
        studentDto.setLevel(Level.FIRST_GRADE);
    }

    @Test
    void getAllStudents_shouldReturnAllStudents() {
        Pageable pageable = Pageable.unpaged();
        Page<Student> studentPage = new PageImpl<>(Collections.singletonList(student));
        when(studentRepository.findAll(pageable)).thenReturn(studentPage);
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        studentService.getAllStudents(null, null, pageable);

        verify(studentRepository).findAll(pageable);
    }

    @Test
    void getStudentById_shouldReturnStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        studentService.getStudentById(1L);

        verify(studentRepository).findById(1L);
    }

    @Test
    void getStudentById_shouldThrowResourceNotFoundException_whenStudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(1L));
    }

    @Test
    void createStudent_shouldCreateStudent() {
        StudentCreateUpdateDto dto = new StudentCreateUpdateDto();
        dto.setUsername("student");
        dto.setLevel(Level.FIRST_GRADE);
        when(studentMapper.toEntity(dto)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        studentService.createStudent(dto);

        verify(studentRepository).save(student);
    }

    @Test
    void updateStudent_shouldUpdateStudent() {
        StudentCreateUpdateDto dto = new StudentCreateUpdateDto();
        dto.setUsername("student");
        dto.setLevel(Level.FIRST_GRADE);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        studentService.updateStudent(1L, dto);

        verify(studentRepository).save(student);
    }

    @Test
    void updateStudent_shouldThrowResourceNotFoundException_whenStudentNotFound() {
        StudentCreateUpdateDto dto = new StudentCreateUpdateDto();
        dto.setUsername("student");
        dto.setLevel(Level.FIRST_GRADE);
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.updateStudent(1L, dto));
    }

    @Test
    void deleteStudent_shouldDeleteStudent() {
        when(studentRepository.existsById(1L)).thenReturn(true);

        studentService.deleteStudent(1L);

        verify(studentRepository).deleteById(1L);
    }

    @Test
    void deleteStudent_shouldThrowResourceNotFoundException_whenStudentNotFound() {
        when(studentRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent(1L));
    }
}
