package org.example.backend.service;


import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.StudentCreateUpdateDto;
import org.example.backend.dto.StudentDto;
import org.example.backend.entity.Student;
import org.example.backend.enums.Level;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.mapper.StudentMapper;
import org.example.backend.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public Page<StudentDto> getAllStudents(String searchTerm, Level level, Pageable pageable) {
        if (searchTerm != null && !searchTerm.isEmpty() && level != null) {
            Long id = null;
            try {
                id = Long.parseLong(searchTerm);
            } catch (NumberFormatException e) {
                // Ignore
            }
            return studentRepository.findByIdAndLevelOrUsernameContainingIgnoreCaseAndLevel(id, level, searchTerm, level, pageable).map(studentMapper::toDto);
        } else if (searchTerm != null && !searchTerm.isEmpty()) {
            Long id = null;
            try {
                id = Long.parseLong(searchTerm);
            } catch (NumberFormatException e) {
                // Ignore
            }
            return studentRepository.findByIdOrUsernameContainingIgnoreCase(id, searchTerm, pageable).map(studentMapper::toDto);
        } else if (level != null) {
            return studentRepository.findByLevel(level, pageable).map(studentMapper::toDto);
        } else {
            return studentRepository.findAll(pageable).map(studentMapper::toDto);
        }
    }

    @Override
    public StudentDto getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
    }

    @Override
    public StudentDto createStudent(StudentCreateUpdateDto dto) {
        Student student = studentMapper.toEntity(dto);
        return studentMapper.toDto(studentRepository.save(student));
    }

    @Override
    public StudentDto updateStudent(Long id, StudentCreateUpdateDto dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
        studentMapper.updateFromDto(dto, student);
        return studentMapper.toDto(studentRepository.save(student));
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id " + id);
        }
        studentRepository.deleteById(id);
    }

    @Override
    public void exportStudents(Writer writer) {
        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            csvWriter.writeNext(new String[]{"ID", "Username", "Level"});
            studentRepository.findAll().forEach(student ->
                    csvWriter.writeNext(new String[]{
                            String.valueOf(student.getId()),
                            student.getUsername(),
                            student.getLevel().name()
                    })
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to export students to CSV", e);
        }
    }

    @Override
    public void importStudents(MultipartFile file) {
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] nextRecord;
            List<Student> students = new ArrayList<>();
            csvReader.readNext(); // Skip header
            while ((nextRecord = csvReader.readNext()) != null) {
                Student student = new Student();
                student.setUsername(nextRecord[1]);
                student.setLevel(Level.valueOf(nextRecord[2]));
                students.add(student);
            }
            studentRepository.saveAll(students);
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Failed to import students from CSV", e);
        }
    }
}
