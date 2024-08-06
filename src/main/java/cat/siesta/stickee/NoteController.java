package cat.siesta.stickee;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/{resource-locator}")
    public @ResponseBody String getNote(@PathVariable("resource-locator") String resourceLocator) {
        var resourceLocatorUuid = UUID.fromString(resourceLocator);
        var maybeNote = noteService.get(resourceLocatorUuid);
        return maybeNote.map(Note::getText).orElseThrow(NoteNotFoundException::new);
    }

    @PostMapping("/create")
    public String postNote(@RequestParam("text") String text) {
        var resourceLocator = noteService.create(new Note(text)).getResourceLocator().orElseThrow().toString();
        return "redirect:/" + resourceLocator;
    }
}
