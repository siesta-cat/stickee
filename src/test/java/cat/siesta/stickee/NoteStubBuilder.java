package cat.siesta.stickee;

public class NoteStubBuilder {
    private String text;
    private String resourceLocator;

    private NoteStubBuilder() {
        text = "This is a stub note.\n Haha.\n ¡Únicod€!";
        resourceLocator = "4bf10c2";
    }

    public static NoteStubBuilder create() {
        return new NoteStubBuilder();
    }

    public NoteStubBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public NoteStubBuilder withResourceLocator(String resourceLocator) {
        this.resourceLocator = resourceLocator;
        return this;
    }

    public Note build() {
        return new Note(text, resourceLocator);
    }
}
