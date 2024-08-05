package cat.siesta.stickee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Note {    
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false, unique = true)
    private String resourceLocator;
    
    public Note(String text, String resourceLocator) {
        this.text = text;
        this.resourceLocator = resourceLocator;
    }

    public String getText() {
        return text;
    }

    public String getResourceLocator() {
        return resourceLocator;
    }

    @Override
    public String toString() {
        return "Note [id=" + id + ", text=" + text + ", resourceLocator=" + resourceLocator + "]";
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
