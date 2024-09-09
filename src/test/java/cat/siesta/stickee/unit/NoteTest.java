package cat.siesta.stickee.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import cat.siesta.stickee.persistence.NoteEntity;

public class NoteTest {

    @Test
    void shouldCreateSameNoteWithGivenId() {
        var note = new NoteEntity(null, "text", LocalDateTime.now());
        var noteWithId = note.withId("123");

        assertEquals(note.getText(), note.getText());
        assertEquals(note.getCreationTimestamp(), note.getCreationTimestamp());
        assertEquals(noteWithId.getId().get(), "123");
    }
}
