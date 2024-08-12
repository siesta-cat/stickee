package cat.siesta.stickee.persistence;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Entity
@ToString
@EqualsAndHashCode
@Getter
public class Note {

    @Id
    private String resourceLocator;

    @Column(nullable = false)
    @Lob
    private String text;

    @Column(nullable = false)
    private final LocalDateTime creationTimestamp = LocalDateTime.now();

    public Note() {
        this.text = "";
    }

    public Note(String text) {
        this.text = text;
    }

    public Note(String resourceLocator, String text) {
        this.resourceLocator = resourceLocator;
        this.text = text;
    }

    public Optional<String> getResourceLocator() {
        return Optional.ofNullable(this.resourceLocator);
    }
}
