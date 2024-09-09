package cat.siesta.stickee;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
public class NoteModel {

    private Optional<String> id;

    private String text;

    @Default
    private LocalDateTime creationTimestamp = LocalDateTime.now();

}
