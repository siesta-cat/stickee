package cat.siesta.stickee.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import cat.siesta.stickee.domain.NoteTimestamp;

public class NoteTimestampTest {

    @Test
    void shouldIgnoreNanosOnEquals() {
        var dt = Instant.now();
        var t1 = new NoteTimestamp(dt);
        var t2 = new NoteTimestamp(dt.truncatedTo(ChronoUnit.MILLIS));

        assertEquals(t1, t2);
    }

    @Test
    void shouldNotIgnoreMillisOnEquals() {
        var dt = Instant.now();
        var t1 = new NoteTimestamp(dt);
        var t2 = new NoteTimestamp(dt.minus(1, ChronoUnit.MILLIS));

        assertNotEquals(t1, t2);
    }
}