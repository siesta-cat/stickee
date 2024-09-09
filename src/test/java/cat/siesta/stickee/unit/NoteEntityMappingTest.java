package cat.siesta.stickee.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import cat.siesta.stickee.domain.NoteModel;
import cat.siesta.stickee.persistence.NoteEntity;

public class NoteEntityMappingTest {

    @Test
    void entityMapsToModelAndBack() {
        var entities = List.of(
                NoteEntity.builder().id("id").text("text").build(),
                NoteEntity.builder().id(null).text("text").build());

        entities.forEach(entity -> assertEquals(entity, NoteEntity.fromModel(NoteEntity.toModel(entity))));
    }

    @Test
    void modelMapsToEntityAndBack() {
        var models = List.of(
                NoteModel.builder().id(Optional.of("id")).text("text").build(),
                NoteModel.builder().id(Optional.empty()).text("text").build());

        models.forEach(entity -> assertEquals(entity, NoteEntity.toModel(NoteEntity.fromModel(entity))));
    }

}
