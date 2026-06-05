package ru.chekel.notes.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.chekel.notes.domain.entity.Note;
import ru.chekel.notes.dto.NoteRequest;
import ru.chekel.notes.dto.NoteResponse;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface NoteMapper {
    NoteResponse toResponse(Note note);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Note toEntity(NoteRequest request);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(NoteRequest request, @MappingTarget Note note);
}
