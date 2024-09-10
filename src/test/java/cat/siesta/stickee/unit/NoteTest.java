package cat.siesta.stickee.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import cat.siesta.stickee.domain.Note;
import cat.siesta.stickee.domain.NoteTimestamp;

public class NoteTest {

    @Test
    void shouldCreateSameNoteWithGivenId() {
        var note = new Note(Optional.empty(), "text", new NoteTimestamp(LocalDateTime.now()));
        var noteWithId = note.withId("123");

        assertEquals(note.getText(), note.getText());
        assertEquals(note.getCreationTimestamp(), note.getCreationTimestamp());
        assertEquals(noteWithId.getMaybeId().get(), "123");
    }
}
