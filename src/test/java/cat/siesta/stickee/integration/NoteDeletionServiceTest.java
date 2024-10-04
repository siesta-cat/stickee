package cat.siesta.stickee.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;
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
        var minimumExpiredDate = Instant.now().minus(stickeeConfig.getMaxExpirationTime());
        var noteEntities = List.of(
                new NoteEntity(NoteId.generate().getId(), "text", minimumExpiredDate.minus(Duration.ofHours(1)),
                        null,
                        TextCipher.PLAIN),
                new NoteEntity(NoteId.generate().getId(), "text", Instant.now(), null,
                        TextCipher.PLAIN),
                new NoteEntity(NoteId.generate().getId(), "text", Instant.now().minus(Duration.ofDays(1)),
                        Instant.now().minus(Duration.ofHours(1)), TextCipher.PLAIN),
                new NoteEntity(NoteId.generate().getId(), "text", Instant.now().minus(Duration.ofDays(1)),
                        Instant.now().plus(Duration.ofDays(1)), TextCipher.PLAIN));

        var insertedNotes = noteEntities.stream()
                .map(note -> noteRepository.save(note))
                .toList();

        var expectedExpiredNotes = insertedNotes.stream()
                .filter(note -> note.getExpirationTimestamp() == null
                        ? note.getCreationTimestamp().isBefore(minimumExpiredDate)
                        : Instant.now().isAfter(note.getExpirationTimestamp()))
                .toList();

        var expectedFreshNotes = insertedNotes.stream()
                .filter(note -> note.getExpirationTimestamp() == null
                        ? !note.getCreationTimestamp().isBefore(minimumExpiredDate)
                        : note.getExpirationTimestamp().isAfter(Instant.now()))
                .toList();

        var deletedEntities = noteDeletionService.deleteExpiredNotes();

        assertTrue(expectedExpiredNotes.stream()
                .allMatch(note -> noteService.get(note.getId()).isEmpty()));
        assertTrue(expectedFreshNotes.stream()
                .allMatch(note -> noteService.get(note.getId()).isPresent()));
        assertEquals(expectedExpiredNotes.size(), deletedEntities);
    }

}
