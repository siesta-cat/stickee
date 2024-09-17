package cat.siesta.stickee.mapper;

import java.util.Optional;

import org.springframework.stereotype.Component;

import cat.siesta.stickee.domain.Note;
import cat.siesta.stickee.domain.NoteId;
import cat.siesta.stickee.domain.NoteTimestamp;
import cat.siesta.stickee.persistence.NoteEntity;
import cat.siesta.stickee.persistence.TextCipher;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class NoteEntityMapper {

    TextEncryptor encryptor;

    public NoteEntity fromModel(Note note) {
        if (note.getMaybeId().isEmpty()) {
            throw new IllegalArgumentException("note model should have a valid id");
        }
        var encryptedText = encryptor.encrypt(note.getText());
        return new NoteEntity(note.getMaybeId().map(NoteId::getId).orElse(null), encryptedText,
                note.getCreationTimestamp(),
                TextCipher.AES256);
    }

    public Note toModel(NoteEntity entity) {
        var decryptedText = switch (entity.getTextCipher()) {
            case PLAIN -> entity.getText();
            case AES256 -> encryptor.decrypt(entity.getText());
        };

        return new Note(Optional.ofNullable(NoteId.createWithoutValidation(entity.getId())), decryptedText,
                new NoteTimestamp(entity.getCreationTimestamp()));
    }
}
