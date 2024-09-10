package cat.siesta.stickee.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
public class NoteTimestamp {
    private LocalDateTime timestamp;

    public NoteTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp.truncatedTo(ChronoUnit.MILLIS);
    }
}
