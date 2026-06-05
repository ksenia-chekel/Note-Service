package ru.chekel.notes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.chekel.notes.domain.entity.Note;
import ru.chekel.notes.domain.repository.NoteRepository;
import ru.chekel.notes.dto.NoteRequest;
import ru.chekel.notes.dto.NoteResponse;
import ru.chekel.notes.exception.NoteNotFoundException;
import ru.chekel.notes.mapper.NoteMapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository repository;
    private final NoteMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<NoteResponse> getNotes(String tag, Pageable pageable) {
        Page<Note> notesPage;
        if (tag != null && !tag.isBlank()) {
            notesPage = repository.findByTagsContaining(tag, pageable);
        } else {
            notesPage = repository.findAll(pageable);
        }
        return notesPage.map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public NoteResponse getNoteById(UUID id) {
        Note note = repository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id));
        return mapper.toResponse(note);
    }

    @Transactional
    @Override
    public NoteResponse createNote(NoteRequest request) {
        Note note = mapper.toEntity(request);
        note.setCreatedAt(LocalDateTime.now());
        Note saved = repository.save(note);
        return mapper.toResponse(saved);
    }

    @Transactional
    @Override
    public NoteResponse updateNote(UUID id, NoteRequest request) {
        Note note = repository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id));
        mapper.updateEntityFromRequest(request, note);
        Note updated = repository.save(note);
        return mapper.toResponse(updated);
    }

    @Override
    public void deleteNote(UUID id) {
        Note note = repository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id));
        repository.delete(note);
    }
}