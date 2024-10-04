package cat.siesta.stickee.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import cat.siesta.stickee.domain.NoteId;
import cat.siesta.stickee.persistence.NoteEntity;
import cat.siesta.stickee.persistence.NoteRepository;
import cat.siesta.stickee.persistence.TextCipher;
import cat.siesta.stickee.service.NoteService;

/**
 * This test suite is to avoid breaking compatibility with older versions
 */
@ActiveProfiles("test")
@SpringBootTest
public class BackwardCompatibilityTest {

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @Test
    void shouldGetAlreadyExistingNoteWithInvalidId() {
        var invalidId = "invalidId";
        var entity = new NoteEntity(invalidId, "text", Instant.now(), Instant.now().plus(Duration.ofDays(7)),
                TextCipher.PLAIN);

        noteRepository.save(entity);

        assertThrows(IllegalArgumentException.class, () -> new NoteId(invalidId));
        assertEquals(entity.getText(), noteService.get(invalidId).get().getText());
    }

    @Test
    void shouldGetNoteWithNullExpirationTimestamp() {
        var id = NoteId.generate().getId();
        var entity = new NoteEntity(id, "text", Instant.now(), null,
                TextCipher.PLAIN);

        noteRepository.save(entity);

        var actualNote = noteService.get(id).get();

        assertEquals(entity.getText(), actualNote.getText());
    }
}
