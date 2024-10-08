package cat.siesta.stickee.persistence;

import java.time.Instant;

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
import lombok.NonNull;
import lombok.ToString;

@Entity(name = "note")
@ToString
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoteEntity {

    @Id
    @NonNull
    private String id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(nullable = false)
    private Instant creationTimestamp;

    @Column(nullable = true)
    private Instant expirationTimestamp;

    // The "PLAIN" column default is to maintain compatibility with older versions
    // that use the plain encoding. It will be removed on a breaking changes update
    @Column(columnDefinition = "VARCHAR(16)", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'PLAIN'")
    private TextCipher textCipher;

}
