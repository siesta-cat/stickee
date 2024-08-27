package cat.siesta.stickee.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cat.siesta.stickee.service.NoteService;
import cat.siesta.stickee.utils.NoteStub;

@SpringBootTest
public class NoteServiceTest {

    @Autowired
    private NoteService noteService;

    @Test
    void shouldCreateAndGet() {
        var note = noteService.create(NoteStub.builder().build());

        var result = noteService.get(note.getId().get());

        assertTrue(result.isPresent());
        assertEquals(note, result.get());
    }

    @Test
    void shouldGetEmptyWhenAbsent() {
        var maybeNote = noteService.get("123");

        assertTrue(maybeNote.isEmpty());
    }
}
