package cat.siesta.stickee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
