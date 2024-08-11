package cat.siesta.stickee.utils;

import java.util.Optional;

import cat.siesta.stickee.persistence.Note;

public class NoteStubBuilder {
    private String text;
    private Optional<String> resourceLocator;

    private NoteStubBuilder() {
        text = "This is a stub note.\n Haha.\n ¡Únicod€!";
        resourceLocator = Optional.empty();
    }

    public static NoteStubBuilder create() {
        return new NoteStubBuilder();
    }

    public NoteStubBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public NoteStubBuilder withResourceLocator(String resourceLocator) {
        this.resourceLocator = Optional.of(resourceLocator);
        return this;
    }

    public Note build() {
        return this.resourceLocator
                .map(resourceLocator -> new Note(resourceLocator, text))
                .orElse(new Note(text));
    }
}
