package cat.siesta.stickee.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import cat.siesta.stickee.domain.Note;
import cat.siesta.stickee.domain.NoteId;
import cat.siesta.stickee.domain.NoteTimestamp;

public class NoteTest {

    @Test
    void shouldCreateSameNoteWithGivenId() {
        var noteId = NoteId.generate();
        var note = new Note(Optional.empty(), "text", new NoteTimestamp(Instant.now()),
                new NoteTimestamp(Instant.now().plus(Duration.ofDays(7))));
        var noteWithId = note.withId(noteId);

        assertEquals(note.getText(), note.getText());
        assertEquals(note.getCreationTimestamp(), note.getCreationTimestamp());
        assertEquals(noteWithId.getMaybeId().get(), noteId);
    }
}
