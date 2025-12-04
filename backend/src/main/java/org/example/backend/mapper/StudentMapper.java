package org.example.backend.mapper;


import org.example.backend.dto.StudentCreateUpdateDto;
import org.example.backend.dto.StudentDto;
import org.example.backend.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentDto toDto(Student student);
    Student toEntity(StudentCreateUpdateDto dto);
    void updateFromDto(StudentCreateUpdateDto dto, @MappingTarget Student student);
}
