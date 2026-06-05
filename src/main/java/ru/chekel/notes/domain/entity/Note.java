package ru.chekel.notes.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    @Id
    @GeneratedValue(generator = "UUID")
    @Setter(AccessLevel.PROTECTED)
    @Column(updatable = false, nullable = false)
    private UUID id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    @ElementCollection
    @BatchSize(size = 20)
    @CollectionTable(name = "note_tags", joinColumns = @JoinColumn(name = "note_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Note(String title, String content, Set<String> tags) {
        this.title = title;
        this.content = content;
        this.tags = tags != null ? new HashSet<>(tags) : new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note note)) return false;
        return Objects.equals(id, note.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
