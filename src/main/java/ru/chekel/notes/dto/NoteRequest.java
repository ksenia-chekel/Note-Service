package ru.chekel.notes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;


public record NoteRequest(@NotBlank(message = "Title cannot be blank")
                          @Size(max = 255, message = "Title must be less than 255 characters")
                          String title,
                          @NotBlank(message = "Content cannot be blank")
                          @Size(max = 10000, message = "Content must be less than 10000 characters")
                          String content,
                          @NotEmpty(message = "At least one tag is required")
                          @Size(max = 10, message = "Maximum 10 tags allowed")
                          Set<String> tags
) {
}

