package cat.siesta.stickee.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.siesta.stickee.persistence.Note;
import cat.siesta.stickee.persistence.NoteRepository;

@Service
public class NoteService {

    @Autowired
    private NoteIdGeneratorService idGeneratorService;

    @Autowired
    private NoteRepository noteRepository;

    public Note create(Note note) {
        var resourceLocator = idGeneratorService.generate();
        var noteWithResourceLocator = new Note(resourceLocator, note.getText());
        return noteRepository.save(noteWithResourceLocator);
    }

    public Optional<Note> get(String resourceLocator) {
        return noteRepository.findByResourceLocator(resourceLocator);
    }
}
