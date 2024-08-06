package cat.siesta.stickee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "note not found")
public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException() {
    }
}
