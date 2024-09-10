package cat.siesta.stickee.domain;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.With;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@Getter
public class Note {

    @Default
    @With(value = AccessLevel.PRIVATE)
    private Optional<String> maybeId = Optional.empty();

    @NotEmpty(message = "text cannot be empty")
    private String text;

    @Default
    private NoteTimestamp creationTimestamp = new NoteTimestamp(LocalDateTime.now());

    public Note withId(String id) {
        return this.withMaybeId(Optional.of(id));
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp.getTimestamp();
    }

}
