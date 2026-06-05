package ru.chekel.notes.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.chekel.notes.dto.NoteRequest;
import ru.chekel.notes.dto.NoteResponse;

import java.util.UUID;

public interface NoteService {
    NoteResponse getNoteById(UUID id);

    Page<NoteResponse> getNotes(String tag, Pageable pageable);

    NoteResponse createNote(NoteRequest request);

    NoteResponse updateNote(UUID id, NoteRequest request);

    void deleteNote(UUID id);
}
