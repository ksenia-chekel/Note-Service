import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.chekel.notes.domain.entity.Note;
import ru.chekel.notes.domain.repository.NoteRepository;
import ru.chekel.notes.dto.NoteRequest;
import ru.chekel.notes.dto.NoteResponse;
import ru.chekel.notes.exception.NoteNotFoundException;
import ru.chekel.notes.mapper.NoteMapper;
import ru.chekel.notes.service.NoteServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {
    @Mock
    private NoteRepository repository;

    @Mock
    private NoteMapper mapper;

    @InjectMocks
    private NoteServiceImpl noteService;

    private Note note;
    private NoteRequest request;
    private NoteResponse response;
    private UUID noteId;

    @BeforeEach
    void setUp() {
        noteId = UUID.randomUUID();
        request = new NoteRequest("Test Note", "Test Content", Set.of("test", "work"));
        note = new Note("Test Note", "Test Content", Set.of("test", "work"));
        response = new NoteResponse(noteId, "Test Note", "Test Content", Set.of("test", "work"), null);
    }

    @Test
    void createNote_shouldReturnCreatedNote() {
        when(mapper.toEntity(request)).thenReturn(note);
        when(repository.save(any(Note.class))).thenReturn(note);
        when(mapper.toResponse(note)).thenReturn(response);

        NoteResponse result = noteService.createNote(request);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Test Note");
        assertThat(result.content()).isEqualTo("Test Content");
        assertThat(result.tags()).containsExactlyInAnyOrder("test", "work");

        verify(repository, times(1)).save(any(Note.class));
        verify(mapper, times(1)).toEntity(request);
        verify(mapper, times(1)).toResponse(note);
    }

    @Test
    void getNoteById_shouldReturnNote_whenExists() {
        when(repository.findById(noteId)).thenReturn(Optional.of(note));
        when(mapper.toResponse(note)).thenReturn(response);

        NoteResponse result = noteService.getNoteById(noteId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(noteId);
        assertThat(result.title()).isEqualTo("Test Note");

        verify(repository, times(1)).findById(noteId);
        verify(mapper, times(1)).toResponse(note);
    }

    @Test
    void getNoteById_shouldThrowException_whenNotFound() {
        when(repository.findById(noteId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> noteService.getNoteById(noteId))
                .isInstanceOf(NoteNotFoundException.class)
                .hasMessageContaining("Note not found with id: " + noteId);

        verify(repository, times(1)).findById(noteId);
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void getNotes_shouldFilterByTag() {
        String tag = "work";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Note> notePage = new PageImpl<>(List.of(note));

        when(repository.findByTagsContaining(tag, pageable)).thenReturn(notePage);
        when(mapper.toResponse(note)).thenReturn(response);

        Page<NoteResponse> result = noteService.getNotes(tag, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).title()).isEqualTo("Test Note");

        verify(repository, times(1)).findByTagsContaining(tag, pageable);
        verify(mapper, times(1)).toResponse(note);
    }

    @Test
    void deleteNote_shouldDeleteExistingNote() {
        when(repository.findById(noteId)).thenReturn(Optional.of(note));

        noteService.deleteNote(noteId);

        verify(repository, times(1)).findById(noteId);
        verify(repository, times(1)).delete(note);
    }

    @Test
    void deleteNote_shouldThrowException_whenNotFound() {
        when(repository.findById(noteId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> noteService.deleteNote(noteId))
                .isInstanceOf(NoteNotFoundException.class);

        verify(repository, times(1)).findById(noteId);
        verify(repository, never()).delete(any());
    }
}
