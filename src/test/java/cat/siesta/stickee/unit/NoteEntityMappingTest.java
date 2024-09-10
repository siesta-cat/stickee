package cat.siesta.stickee.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import cat.siesta.stickee.domain.Note;
import cat.siesta.stickee.domain.NoteTimestamp;
import cat.siesta.stickee.persistence.NoteEntity;

public class NoteEntityMappingTest {

    @Test
    void modelMapsToEntityAndBack() {
        var models = List.of(
                Note.builder().maybeId(Optional.of("id")).text("text").build(),
                Note.builder().maybeId(Optional.of("id")).text("text")
                        .creationTimestamp(new NoteTimestamp(LocalDateTime.now())).build(),
                Note.builder().maybeId(Optional.empty()).text("text").build());

        models.forEach(entity -> assertEquals(entity, NoteEntity.fromModel(entity).toModel()));
    }

}
