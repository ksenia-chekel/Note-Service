package ru.chekel.notes.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.chekel.notes.domain.entity.Note;

import java.util.UUID;


@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {
    Page<Note> findByTagsContaining(String tag, Pageable pageable);

    @EntityGraph(attributePaths = {"tags"})
    @Override
    Page<Note> findAll(Pageable pageable);

}



