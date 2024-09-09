package cat.siesta.stickee.utils;

import org.apache.commons.lang3.RandomStringUtils;

import cat.siesta.stickee.persistence.NoteEntity;

public class NoteStub {

    public static NoteEntity.NoteEntityBuilder builder() {
        return NoteEntity.builder()
                .text(RandomStringUtils.insecure().next(20));
    }
}
