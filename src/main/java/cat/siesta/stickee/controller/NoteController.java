package cat.siesta.stickee.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import cat.siesta.stickee.config.StickeeConfig;
import cat.siesta.stickee.persistence.Note;
import cat.siesta.stickee.service.NoteService;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("${notes-base-path}")
public class NoteController {

    @Autowired
    private StickeeConfig stickeeConfig;

    @Autowired
    private NoteService noteService;

    @GetMapping("/{id}")
    public ResponseEntity<String> getNote(@PathVariable("id") String id) {
        log.debug("Received request for note with id: {}", id);

        var maybeNote = noteService.get(id);
        var noteCreationDate = maybeNote.map(note -> note.getCreationTimestamp()).orElse(LocalDateTime.now());
        var cacheMaxAge = Math.max(0, stickeeConfig.getNotesMaxAge().toSeconds()
                - ChronoUnit.SECONDS.between(noteCreationDate, LocalDateTime.now()));
        var cacheControl = CacheControl.maxAge(cacheMaxAge, TimeUnit.SECONDS).cachePublic().immutable();

        return maybeNote.map(note -> ResponseEntity
                .status(HttpStatus.OK)
                .cacheControl(cacheControl)
                .header("Content-Type", "text/plain")
                .body(note.getText()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "note not found"));
    }

    @PostMapping("/create")
    public ResponseEntity<String> postNote(
            @NotEmpty(message = "text cannot be empty") String text) {
        if (text.getBytes().length > stickeeConfig.getNotesMaxSize().toBytes()) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    "the max size of a note is " + stickeeConfig.getNotesMaxSize().toString());
        }

        var id = noteService.create(Note.builder().text(text).build())
                .getId().orElseThrow().toString();

        log.info("Note created with id: {}", id);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header("Location", "/" + stickeeConfig.getNotesBasePath() + "/" + id)
                .build();
    }
}
