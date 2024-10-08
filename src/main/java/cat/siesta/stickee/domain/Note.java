package cat.siesta.stickee.domain;

import java.time.Instant;
import java.util.Optional;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
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
    private Optional<NoteId> maybeId = Optional.empty();

    @NotEmpty(message = "text cannot be empty")
    @NonNull
    private String text;

    @Default
    private NoteTimestamp creationTimestamp = new NoteTimestamp(Instant.now());

    @NonNull
    private NoteTimestamp expirationTimestamp;

    public Note withId(NoteId id) {
        return this.withMaybeId(Optional.of(id));
    }

    public Instant getCreationTimestamp() {
        return creationTimestamp.getTimestamp();
    }

    public Instant getExpirationTimestamp() {
        return expirationTimestamp.getTimestamp();
    }

}
