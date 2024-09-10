package cat.siesta.stickee.persistence;

import java.time.LocalDateTime;
import java.util.Optional;

import cat.siesta.stickee.domain.Note;
import cat.siesta.stickee.domain.NoteTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoteEntity {

    @Id
    private String id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(nullable = false)
    private LocalDateTime creationTimestamp;

    public static NoteEntity fromModel(Note note) {
        return new NoteEntity(note.getMaybeId().orElse(null), note.getText(), note.getCreationTimestamp());
    }

    public Note toModel() {
        return new Note(Optional.ofNullable(this.getId()), this.getText(),
                new NoteTimestamp(this.getCreationTimestamp()));
    }
}
