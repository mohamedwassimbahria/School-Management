package org.example.backend.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.StudentCreateUpdateDto;
import org.example.backend.dto.StudentDto;
import org.example.backend.enums.Level;
import org.example.backend.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "Get all students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of students"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public Page<StudentDto> getAllStudents(@RequestParam(required = false) String searchTerm,
                                           @RequestParam(required = false) Level level,
                                           Pageable pageable) {
        return studentService.getAllStudents(searchTerm, level, pageable);
    }

    @Operation(summary = "Get a student by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved student"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @Operation(summary = "Create a new student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody StudentCreateUpdateDto dto) {
        return new ResponseEntity<>(studentService.createStudent(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentCreateUpdateDto dto) {
        return ResponseEntity.ok(studentService.updateStudent(id, dto));
    }

    @Operation(summary = "Delete a student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Export students to CSV")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully exported students"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/export")
    public void exportStudents(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=students.csv");
        studentService.exportStudents(response.getWriter());
    }

    @Operation(summary = "Import students from CSV")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully imported students"),
            @ApiResponse(responseCode = "400", description = "Invalid file format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/import")
    public ResponseEntity<Void> importStudents(@RequestParam("file") MultipartFile file) {
        studentService.importStudents(file);
        return ResponseEntity.ok().build();
    }
}
