package cat.siesta.stickee.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cat.siesta.stickee.config.StickeeConfig;
import cat.siesta.stickee.service.NoteDeletionService;
import cat.siesta.stickee.service.NoteService;
import cat.siesta.stickee.utils.NoteStub;

@SpringBootTest
public class NoteDeletionServiceTest {

    @Autowired
    private NoteDeletionService noteDeletionService;

    @Autowired
    private StickeeConfig stickeeConfig;

    @Autowired
    private NoteService noteService;

    @Test
    void shouldDeleteExpiredNotes() {
        var expiredDate = LocalDateTime.now().minus(stickeeConfig.getMaxAge());
        var notes = List.of(
                NoteStub.builder().build(),
                NoteStub.builder().creationTimestamp(expiredDate.minusHours(1)).build(),
                NoteStub.builder().creationTimestamp(expiredDate.minusDays(3)).build());

        var insertedNotes = notes.stream()
                .map(note -> noteService.create(note))
                .collect(Collectors.toList());

        var expectedExpiredNotes = insertedNotes.stream()
                .filter(note -> note.getCreationTimestamp().isBefore(expiredDate));

        var expectedFreshNotes = insertedNotes.stream()
                .filter(note -> !note.getCreationTimestamp().isBefore(expiredDate));

        var deletedEntities = noteDeletionService.deleteExpiredNotes();

        assertEquals(2, deletedEntities);
        assertTrue(expectedExpiredNotes.allMatch(note -> noteService.get(note.getId().get()).isEmpty()));
        assertTrue(expectedFreshNotes.allMatch(note -> noteService.get(note.getId().get()).isPresent()));
    }

}
