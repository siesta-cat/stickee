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

    @Column(nullable = false)
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
}
