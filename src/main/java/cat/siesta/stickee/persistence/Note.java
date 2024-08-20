package cat.siesta.stickee.persistence;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@EqualsAndHashCode
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    @Id
    private String id;

    @Default
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text = "";

    @Default
    @Column(nullable = false)
    private LocalDateTime creationTimestamp = LocalDateTime.now();

    public Optional<String> getId() {
        return Optional.ofNullable(this.id);
    }
}
