package cat.siesta.stickee;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Note {

    @Id
    private String resourceLocator;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private LocalDateTime creationTimestamp = LocalDateTime.now();

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

    public String getText() {
        return text;
    }

    public Optional<String> getResourceLocator() {
        return Optional.ofNullable(this.resourceLocator);
    }

    @Override
    public String toString() {
        return "Note [text=" + text + ", resourceLocator=" + resourceLocator + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((resourceLocator == null) ? 0 : resourceLocator.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        result = prime * result + ((creationTimestamp == null) ? 0 : creationTimestamp.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Note other = (Note) obj;
        if (resourceLocator == null) {
            if (other.resourceLocator != null)
                return false;
        } else if (!resourceLocator.equals(other.resourceLocator))
            return false;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
    }

}
