package cat.siesta.stickee.domain;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
public class NoteTimestamp {
    private Instant timestamp;

    public NoteTimestamp(@NonNull Instant timestamp) {
        this.timestamp = timestamp.truncatedTo(ChronoUnit.MILLIS);
    }
}
