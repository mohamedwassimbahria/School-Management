package org.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.backend.enums.Level;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Student data transfer object")
public class StudentDto {

    @Schema(description = "Student ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+$",
            message = "Username can only contain letters, numbers, dots, underscores and hyphens")
    @Schema(description = "Unique username", example = "john.doe", required = true)
    private String username;

    @NotNull(message = "Level is required")
    @Schema(description = "Student level", example = "INTERMEDIATE", required = true)
    private Level level;

}
