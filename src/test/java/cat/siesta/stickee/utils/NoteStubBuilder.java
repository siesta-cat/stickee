package cat.siesta.stickee.utils;

import java.util.Optional;

import cat.siesta.stickee.persistence.Note;

public class NoteStubBuilder {
    private String text;
    private Optional<String> id;

    private NoteStubBuilder() {
        text = "This is a stub note.\n Haha.\n ¡Únicod€!";
        id = Optional.empty();
    }

    public static NoteStubBuilder create() {
        return new NoteStubBuilder();
    }

    public NoteStubBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public NoteStubBuilder withId(String id) {
        this.id = Optional.of(id);
        return this;
    }

    public Note build() {
        return this.id
                .map(id -> new Note(id, text))
                .orElse(new Note(text));
    }
}
