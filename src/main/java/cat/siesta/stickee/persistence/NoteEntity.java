package cat.siesta.stickee.persistence;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "note")
@ToString
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoteEntity {

    @Id
    private String id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(nullable = false)
    private LocalDateTime creationTimestamp;

    // The "PLAIN" column default is to maintain compatibility with older versions
    // that use the plain encoding. It will be removed on a breaking changes update
    @Column(columnDefinition = "VARCHAR(16)", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'PLAIN'")
    private TextCipher textCipher;

}
