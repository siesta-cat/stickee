package cat.siesta.stickee.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import cat.siesta.stickee.domain.Note;
import cat.siesta.stickee.domain.NoteId;
import cat.siesta.stickee.domain.NoteTimestamp;
import cat.siesta.stickee.mapper.NoteEntityMapper;
import cat.siesta.stickee.mapper.TextEncryptor;
import cat.siesta.stickee.persistence.TextCipher;

public class NoteEntityMappingTest {

    private String key = RandomStringUtils.insecure().nextAscii(10);
    private TextEncryptor encryptor = new TextEncryptor(key);
    private NoteEntityMapper mapper = new NoteEntityMapper(encryptor);

    @Test
    void modelMapsToEntityAndBack() {
        var models = List.of(
                Note.builder().maybeId(Optional.of(NoteId.generate())).text("text").build(),
                Note.builder().maybeId(Optional.of(NoteId.generate())).text("text")
                        .creationTimestamp(new NoteTimestamp(LocalDateTime.now())).build());

        models.forEach(
                entity -> assertEquals(entity, mapper.toModel(mapper.fromModel(entity))));
    }

    @Test
    void shouldThrowOnEmptyId() {
        var model = Note.builder().maybeId(Optional.empty()).text("text").build();

        assertThrows(IllegalArgumentException.class, () -> mapper.fromModel(model));
    }

    @Test
    void entityCiphersModelText() {
        var text = RandomStringUtils.insecure().next(10);
        var model = Note.builder().maybeId(Optional.of(NoteId.generate())).text(text).build();
        var entity = mapper.fromModel(model);

        assertEquals(encryptor.decrypt(entity.getText()), text);
        assertNotEquals(entity.getText(), text);
        assertEquals(entity.getTextCipher(), TextCipher.AES256);
    }

}
