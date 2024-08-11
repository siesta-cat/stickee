package cat.siesta.stickee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import cat.siesta.stickee.persistence.Note;
import cat.siesta.stickee.service.NoteService;

@RestController
@RequestMapping("${stickee.notes-base-path}")
public class NoteController {

    @Value("${stickee.notes-base-path}")
    private String notesBasePath;

    @Autowired
    private NoteService noteService;

    @GetMapping("/{resource-locator}")
    public String getNote(@PathVariable("resource-locator") String resourceLocator) {
        var maybeNote = noteService.get(resourceLocator);
        return maybeNote.map(Note::getText).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "note not found"));
    }

    @PostMapping("/create")
    public ResponseEntity<String> postNote(@RequestParam("text") String text) {
        var resourceLocator = noteService.create(new Note(text)).getResourceLocator().orElseThrow().toString();
        return ResponseEntity
            .status(HttpStatus.FOUND)
            .header("Location", notesBasePath + "/" + resourceLocator)
            .build();
    }
}
