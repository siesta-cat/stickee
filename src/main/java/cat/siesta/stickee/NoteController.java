package cat.siesta.stickee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/{resource-locator}")
    public String getNote(@PathVariable("resource-locator") String resourceLocator) {
        var maybeNote = noteService.get(resourceLocator);
        return maybeNote.map(Note::getText).orElseThrow(NoteNotFoundException::new);
    }

    @PostMapping("/create")
    public ResponseEntity<String> postNote(@RequestParam("text") String text) {
        var resourceLocator = noteService.create(new Note(text)).getResourceLocator().orElseThrow().toString();
        return ResponseEntity
            .status(HttpStatus.FOUND)
            .header("Location", "/" + resourceLocator)
            .build();
    }
}
