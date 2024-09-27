package cat.siesta.stickee.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import cat.siesta.stickee.mapper.NoteEntityMapper;
import cat.siesta.stickee.mapper.TextEncryptor;
import cat.siesta.stickee.persistence.TextCipher;
import cat.siesta.stickee.utils.NoteStub;

public class NoteEntityMappingTest {

    private Duration expirationTime = Duration.ofDays(7);
    private String key = RandomStringUtils.insecure().nextAscii(10);
    private TextEncryptor encryptor = new TextEncryptor(key);
    private NoteEntityMapper mapper = new NoteEntityMapper(encryptor, expirationTime);

    @Test
    void modelMapsToEntityAndBack() {
        var models = Stream.generate(() -> NoteStub.builder().build())
                .limit(5);

        models.forEach(
                entity -> assertEquals(entity, mapper.toModel(mapper.fromModel(entity))));
    }

    @Test
    void shouldThrowOnEmptyId() {
        var model = NoteStub.builder().maybeId(Optional.empty()).build();

        assertThrows(NullPointerException.class, () -> mapper.fromModel(model));
    }

    @Test
    void entityCiphersModelText() {
        var text = RandomStringUtils.insecure().next(10);
        var model = NoteStub.builder().text(text).build();
        var entity = mapper.fromModel(model);

        assertEquals(encryptor.decrypt(entity.getText()), text);
        assertNotEquals(entity.getText(), text);
        assertEquals(entity.getTextCipher(), TextCipher.AES256);
    }

}
