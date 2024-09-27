package cat.siesta.stickee.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import cat.siesta.stickee.config.StickeeConfig;
import cat.siesta.stickee.domain.NoteId;
import cat.siesta.stickee.persistence.NoteEntity;
import cat.siesta.stickee.persistence.NoteRepository;
import cat.siesta.stickee.persistence.TextCipher;
import cat.siesta.stickee.service.NoteDeletionService;
import cat.siesta.stickee.service.NoteService;

@ActiveProfiles("test")
@SpringBootTest
public class NoteDeletionServiceTest {

    @Autowired
    private NoteDeletionService noteDeletionService;

    @Autowired
    private StickeeConfig stickeeConfig;

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @Test
    void shouldDeleteExpiredNotes() {
        var minimumExpiredDate = LocalDateTime.now().minus(stickeeConfig.getMaxExpirationTime());
        var noteEntities = List.of(
                new NoteEntity(NoteId.generate().getId(), "text", minimumExpiredDate.minusHours(1), null,
                        TextCipher.PLAIN),
                new NoteEntity(NoteId.generate().getId(), "text", LocalDateTime.now(), null, TextCipher.PLAIN),
                new NoteEntity(NoteId.generate().getId(), "text", LocalDateTime.now().minusDays(1),
                        LocalDateTime.now().minusHours(1), TextCipher.PLAIN),
                new NoteEntity(NoteId.generate().getId(), "text", LocalDateTime.now().minusDays(1),
                        LocalDateTime.now().plusHours(1), TextCipher.PLAIN));

        var insertedNotes = noteEntities.stream()
                .map(note -> noteRepository.save(note))
                .toList();

        var expectedExpiredNotes = insertedNotes.stream()
                .filter(note -> note.getExpirationTimestamp() == null
                        ? note.getCreationTimestamp().isBefore(minimumExpiredDate)
                        : LocalDateTime.now().isAfter(note.getExpirationTimestamp()))
                .toList();

        var expectedFreshNotes = insertedNotes.stream()
                .filter(note -> note.getExpirationTimestamp() == null
                        ? !note.getCreationTimestamp().isBefore(minimumExpiredDate)
                        : note.getExpirationTimestamp().isAfter(LocalDateTime.now()))
                .toList();

        var deletedEntities = noteDeletionService.deleteExpiredNotes();

        assertEquals(expectedExpiredNotes.size(), deletedEntities);
        assertTrue(expectedExpiredNotes.stream()
                .allMatch(note -> noteService.get(note.getId()).isEmpty()));
        assertTrue(expectedFreshNotes.stream()
                .allMatch(note -> noteService.get(note.getId()).isPresent()));
    }

}
