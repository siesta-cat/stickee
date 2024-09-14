package cat.siesta.stickee.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import cat.siesta.stickee.service.NoteService;
import cat.siesta.stickee.utils.NoteStub;

@ActiveProfiles("test")
@SpringBootTest
public class NoteServiceTest {

    @Autowired
    private NoteService noteService;

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
}
