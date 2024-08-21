package cat.siesta.stickee.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import cat.siesta.stickee.persistence.NoteRepository;
import cat.siesta.stickee.service.NoteIdGeneratorService;
import cat.siesta.stickee.service.NoteService;
import cat.siesta.stickee.utils.NoteStub;

@SpringBootTest
public class NoteServiceTest {

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteIdGeneratorService generatorService;

    @MockBean
    private NoteRepository noteRepository;

    @Test
    void shouldSave() {
        // TODO: Make this test integration
        var note = NoteStub.builder().build();

        given(noteRepository.save(any())).willReturn(note);
        var savedNote = noteService.create(note);

        assertEquals(note, savedNote);
    }

    @Test
    void shouldGetWhenExisting() {
        var id = "1a2";
        var note = NoteStub.builder()
                .text("I should be get")
                .id(id)
                .build();

        given(noteRepository.findById(id)).willReturn(Optional.of(note));
        var maybeNote = noteService.get(id);

        assertTrue(maybeNote.isPresent());
        assertEquals(note, maybeNote.get());
    }

    @Test
    void shouldGetEmptyWhenAbsent() {
        var id = "1v4";

        given(noteRepository.findById(id)).willReturn(Optional.empty());
        var maybeNote = noteService.get(id);

        assertTrue(maybeNote.isEmpty());
    }

    @Test
    void shouldRegenerateNewIdOnCollision() {
        var noteToInsert = NoteStub.builder().build();

        given(noteRepository.existsById(anyString()))
                .willReturn(true).willReturn(false);
        noteService.create(noteToInsert);

        verify(noteRepository, times(2)).existsById(anyString());
        verify(noteRepository).save(any());
    }

    @Test
    void IdsHaveDifferentAlphanumericValues() {
        var numberOfIdsToGenerate = 50;
        given(noteRepository.existsById(anyString())).willReturn(false);

        var distinctGeneratedIds = IntStream.range(0, numberOfIdsToGenerate)
                .mapToObj(i -> generatorService.generate())
                .distinct()
                .collect(Collectors.toList());

        assertTrue(distinctGeneratedIds.stream().allMatch(StringUtils::isAlphanumeric));
        assertEquals(numberOfIdsToGenerate, distinctGeneratedIds.size());
    }
}
