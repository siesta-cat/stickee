package cat.siesta.stickee.persistence;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import cat.siesta.stickee.domain.NoteModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@EqualsAndHashCode
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteEntity {

    @Id
    private String id;

    @Default
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text = "";

    @Default
    @Column(nullable = false)
    private LocalDateTime creationTimestamp = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);

    public NoteEntity withId(String id) {
        return new NoteEntity(id, this.getText(), this.getCreationTimestamp());
    }

    public Optional<String> getId() {
        return Optional.ofNullable(this.id);
    }

    public static NoteEntity fromModel(NoteModel note) {
        return new NoteEntity(note.getId().orElse(null), note.getText(), note.getCreationTimestamp());
    }

    public static NoteModel toModel(NoteEntity note) {
        return new NoteModel(note.getId(), note.getText(), note.getCreationTimestamp());
    }
}
