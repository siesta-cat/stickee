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
        var id = idGeneratorService.generate();
        var noteWithId = new Note(id, note.getText());
        return noteRepository.save(noteWithId);
    }

    public Optional<Note> get(String id) {
        return noteRepository.findById(id);
    }
}
