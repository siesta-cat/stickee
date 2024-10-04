package cat.siesta.stickee.mapper;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cat.siesta.stickee.domain.Note;
import cat.siesta.stickee.domain.NoteId;
import cat.siesta.stickee.domain.NoteTimestamp;
import cat.siesta.stickee.persistence.NoteEntity;
import cat.siesta.stickee.persistence.TextCipher;

@Component
public class NoteEntityMapper {

    private TextEncryptor encryptor;
    private Duration maxExpirationTime;

    public NoteEntityMapper(TextEncryptor encryptor,
            @Value("${notes.max-expiration-time}") Duration maxExpirationTime) {
        this.encryptor = encryptor;
        this.maxExpirationTime = maxExpirationTime;
    }

    public NoteEntity fromModel(Note note) {
        var encryptedText = encryptor.encrypt(note.getText());
        return new NoteEntity(note.getMaybeId().map(NoteId::getId).orElse(null), encryptedText,
                note.getCreationTimestamp(), note.getExpirationTimestamp(),
                TextCipher.AES256);
    }

    public Note toModel(NoteEntity entity) {
        var decryptedText = switch (entity.getTextCipher()) {
            case PLAIN -> entity.getText();
            case AES256 -> encryptor.decrypt(entity.getText());
        };
        var expirationTimestamp = new NoteTimestamp(
                entity.getExpirationTimestamp() != null ? entity.getExpirationTimestamp()
                        : Instant.now().plus(maxExpirationTime));
        return new Note(Optional.ofNullable(NoteId.createWithoutValidation(entity.getId())), decryptedText,
                new NoteTimestamp(entity.getCreationTimestamp()), expirationTimestamp);
    }
}
