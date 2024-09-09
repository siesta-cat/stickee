package cat.siesta.stickee.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.siesta.stickee.persistence.NoteEntity;
import cat.siesta.stickee.persistence.NoteRepository;

@Service
public class NoteService {

    @Autowired
    private NoteIdGeneratorService idGeneratorService;

    @Autowired
    private NoteRepository noteRepository;

    public NoteEntity create(NoteEntity note) {
        var id = idGeneratorService.generate();
        var noteWithId = note.withId(id);
        return noteRepository.save(noteWithId);
    }

    public Optional<NoteEntity> get(String id) {
        return noteRepository.findById(id);
    }
}
