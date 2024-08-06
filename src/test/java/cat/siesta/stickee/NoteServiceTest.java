package cat.siesta.stickee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class NoteServiceTest {

    @Autowired
    private NoteService noteService;

    @MockBean
    private NoteRepository noteRepository;

    @Test
    void shouldSave() {
        var note = NoteStubBuilder.create().build();

        noteService.create(note);

        verify(noteRepository).save(note);
    }

    @Test
    void shouldGetWhenExisting() {
        var resourceLocator = UUID.randomUUID();
        var note = NoteStubBuilder.create()
                .withText("I should be get")
                .withResourceLocator(resourceLocator)
                .build();

        given(noteRepository.findByResourceLocator(resourceLocator)).willReturn(Optional.of(note));
        var maybeNote = noteService.get(resourceLocator);

        assertTrue(maybeNote.isPresent());
        assertEquals(note, maybeNote.get());
    }

    @Test
    void shouldGetEmptyWhenAbsent() {
        var resourceLocator = UUID.randomUUID();

        given(noteRepository.findByResourceLocator(resourceLocator)).willReturn(Optional.empty());
        var maybeNote = noteService.get(resourceLocator);

        assertTrue(maybeNote.isEmpty());
    }
}
