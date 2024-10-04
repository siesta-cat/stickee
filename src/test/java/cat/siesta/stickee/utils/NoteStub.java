package cat.siesta.stickee.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import cat.siesta.stickee.domain.Note;
import cat.siesta.stickee.domain.NoteId;
import cat.siesta.stickee.domain.NoteTimestamp;

public class NoteStub {

    public static Note.NoteBuilder builder() {
        return Note.builder()
                .maybeId(Optional.of(NoteId.generate()))
                .text(RandomStringUtils.insecure().next(20))
                .expirationTimestamp(
                        new NoteTimestamp(
                                Instant.now().plus(Duration.ofHours(RandomUtils.insecure().randomInt(1, 100)))));
    }
}
