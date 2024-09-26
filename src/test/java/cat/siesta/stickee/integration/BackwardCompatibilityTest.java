package cat.siesta.stickee.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

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
        var entity = new NoteEntity(invalidId, "text", LocalDateTime.now(), LocalDateTime.now().plusDays(7),
                TextCipher.PLAIN);

        noteRepository.save(entity);

        assertThrows(IllegalArgumentException.class, () -> new NoteId(invalidId));
        assertEquals(entity.getText(), noteService.get(invalidId).get().getText());
    }

    @Test
    void shouldGetNoteWithNullExpirationTimestamp() {
        var id = NoteId.generate().getId();
        var entity = new NoteEntity(id, "text", LocalDateTime.now(), null,
                TextCipher.PLAIN);

        noteRepository.save(entity);

        var actualNote = noteService.get(id).get();

        assertEquals(entity.getText(), actualNote.getText());
    }
}
