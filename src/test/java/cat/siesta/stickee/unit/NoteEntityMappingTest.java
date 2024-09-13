package cat.siesta.stickee.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import cat.siesta.stickee.domain.Note;
import cat.siesta.stickee.domain.NoteTimestamp;
import cat.siesta.stickee.mapper.NoteEntityMapper;
import cat.siesta.stickee.mapper.TextEncryptor;
import cat.siesta.stickee.persistence.TextCipher;

@ActiveProfiles("test")
@SpringBootTest
public class NoteEntityMappingTest {

    @Autowired
    private NoteEntityMapper mapper;

    @Autowired
    private TextEncryptor encryptor;

    @Test
    void modelMapsToEntityAndBack() {
        var models = List.of(
                Note.builder().maybeId(Optional.of("id")).text("text").build(),
                Note.builder().maybeId(Optional.of("id")).text("text")
                        .creationTimestamp(new NoteTimestamp(LocalDateTime.now())).build(),
                Note.builder().maybeId(Optional.empty()).text("text").build());

        models.forEach(
                entity -> assertEquals(entity, mapper.toModel(mapper.fromModel(entity))));
    }

    @Test
    void entityCiphersModelText() {
        var text = RandomStringUtils.insecure().next(10);
        var model = Note.builder().maybeId(Optional.of("id")).text(text).build();
        var entity = mapper.fromModel(model);

        assertEquals(encryptor.decrypt(entity.getText()), text);
        assertNotEquals(entity.getText(), text);
        assertEquals(entity.getTextCipher(), TextCipher.AES256);
    }

}
