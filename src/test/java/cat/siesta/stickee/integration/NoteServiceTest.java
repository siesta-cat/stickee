package cat.siesta.stickee.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import cat.siesta.stickee.domain.NoteId;
import cat.siesta.stickee.persistence.NoteEntity;
import cat.siesta.stickee.persistence.NoteRepository;
import cat.siesta.stickee.persistence.TextCipher;
import cat.siesta.stickee.service.NoteService;
import cat.siesta.stickee.utils.NoteStub;

@ActiveProfiles("test")
@SpringBootTest
public class NoteServiceTest {

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @Test
    void shouldCreateAndGet() {
        var note = noteService.create(NoteStub.builder().build());

        var result = noteService.get(note.getMaybeId().get().getId());

        assertTrue(result.isPresent());
        assertEquals(note, result.get());
    }

    @Test
    void shouldGetEmptyWhenAbsent() {
        var maybeNote = noteService.get("123");

        assertTrue(maybeNote.isEmpty());
    }

    // This is to avoid breaking compatibility with older versions if the
    // specification of the note id changes
    @Test
    void shouldGetAlreadyExistingNoteWithInvalidId() {
        var invalidId = "invalidId";
        var entity = new NoteEntity(invalidId, "text", LocalDateTime.now(), TextCipher.PLAIN);

        noteRepository.save(entity);

        assertThrows(IllegalArgumentException.class, () -> new NoteId(invalidId));
        assertNotEquals(Optional.empty(), noteService.get(invalidId));
    }
}
