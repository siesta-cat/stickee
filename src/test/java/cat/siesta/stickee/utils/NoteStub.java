package cat.siesta.stickee.utils;

import org.apache.commons.lang3.RandomStringUtils;

import cat.siesta.stickee.persistence.Note;

public class NoteStub {

    public static Note.NoteBuilder builder() {
        return Note.builder()
                .text(RandomStringUtils.insecure().next(20));
    }
}
