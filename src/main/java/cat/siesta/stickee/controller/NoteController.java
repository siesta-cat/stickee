package cat.siesta.stickee.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import cat.siesta.stickee.config.StickeeConfig;
import cat.siesta.stickee.domain.Note;
import cat.siesta.stickee.domain.NoteTimestamp;
import cat.siesta.stickee.service.NoteService;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("${notes.base-path}")
public class NoteController {

    private StickeeConfig stickeeConfig;
    private NoteService noteService;

    @GetMapping({ "/{id}", "/raw/{id}" })
    public ResponseEntity<String> getNote(@PathVariable("id") String id) {
        log.debug("Received request for note with id: {}", id);

        var note = noteService.get(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "note not found"));

        var noteCreationDate = note.getCreationTimestamp();
        var cacheMaxAge = Math.max(0, stickeeConfig.getMaxExpirationTime().toSeconds()
                - ChronoUnit.SECONDS.between(noteCreationDate, LocalDateTime.now()));
        var cacheControl = CacheControl.maxAge(cacheMaxAge, TimeUnit.SECONDS).cachePublic().immutable();

        return ResponseEntity
                .status(HttpStatus.OK)
                .cacheControl(cacheControl)
                .header("Content-Type", "text/plain")
                .body(note.getText());
    }

    @PostMapping("/create")
    public ResponseEntity<String> postNote(
            @RequestHeader(HttpHeaders.HOST) String host,
            @NotEmpty(message = "text cannot be empty") String text,
            @RequestParam Optional<Duration> expirationTime) {
        if (text.getBytes().length > stickeeConfig.getMaxSize().toBytes()) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    "the max size of a note is " + stickeeConfig.getMaxSize().toString());
        }

        if (expirationTime.map(et -> et.compareTo(stickeeConfig.getMaxExpirationTime()) == 1)
                .orElse(false)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "the max expitarion time of a note is " + DurationFormatUtils
                            .formatDurationWords(
                                    stickeeConfig.getMaxExpirationTime().toMillis(),
                                    true, true));
        }

        var expirationTimestamp = new NoteTimestamp(LocalDateTime.now()
                .plus(expirationTime.orElse(stickeeConfig.getMaxExpirationTime())));

        var id = noteService
                .create(Note.builder().text(text)
                        .expirationTimestamp(expirationTimestamp)
                        .build())
                .getMaybeId().orElseThrow();

        log.info("Note created with id: {}", id);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header("Location", "/" + stickeeConfig.getBasePath() + "/detail/" + id)
                .body(host + "/" + stickeeConfig.getBasePath() + "/raw/" + id);
    }
}
