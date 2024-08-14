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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import cat.siesta.stickee.config.StickeeConfiguration;
import cat.siesta.stickee.persistence.Note;
import cat.siesta.stickee.service.NoteService;

@RestController
@RequestMapping("${notes-base-path}")
public class NoteController {

    @Autowired
    private StickeeConfiguration stickeeConfiguration;

    @Autowired
    private NoteService noteService;

    @GetMapping("/{id}")
    public ResponseEntity<String> getNote(@PathVariable("id") String id) {
        var maybeNote = noteService.get(id);
        var noteCreationDate = maybeNote.map(note -> note.getCreationTimestamp()).orElse(LocalDateTime.now());
        var cacheMaxAge = stickeeConfiguration.getNoteMaxAge()
                - ChronoUnit.SECONDS.between(noteCreationDate, LocalDateTime.now());
        var cacheControl = CacheControl.maxAge(cacheMaxAge, TimeUnit.SECONDS).cachePublic().immutable();

        return maybeNote.map(note -> ResponseEntity
                .status(HttpStatus.OK)
                .cacheControl(cacheControl)
                .header("Content-Type", "text/plain")
                .body(note.getText()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "note not found"));
    }

    @PostMapping("/create")
    public ResponseEntity<String> postNote(@RequestParam("text") String text) {
        var id = noteService.create(new Note(text)).getId().orElseThrow().toString();
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header("Location", "/" + stickeeConfiguration.getNotesBasePath() + "/" + id)
                .build();
    }
}
