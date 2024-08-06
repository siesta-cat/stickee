package cat.siesta.stickee;

import java.util.Optional;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Note {   

    @Id
    @UuidGenerator(style = Style.RANDOM)
    private UUID resourceLocator;

    @Column(nullable = false)
    private String text;

    public Note() {
        this.text = "";
    }

    public Note(String text) {
        this.text = text;
    }

    public Note(UUID resourceLocator, String text) {
        this.resourceLocator = resourceLocator;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Optional<UUID> getResourceLocator() {
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
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        result = prime * result + ((resourceLocator == null) ? 0 : resourceLocator.hashCode());
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
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        if (resourceLocator == null) {
            if (other.resourceLocator != null)
                return false;
        } else if (!resourceLocator.equals(other.resourceLocator))
            return false;
        return true;
    }

    
}
