package cat.siesta.stickee.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.siesta.stickee.domain.Note;
import cat.siesta.stickee.persistence.NoteEntity;
import cat.siesta.stickee.persistence.NoteRepository;

@Service
public class NoteService {

    @Autowired
    private NoteIdGeneratorService idGeneratorService;

    @Autowired
    private NoteRepository noteRepository;

    public Note create(Note note) {
        var id = idGeneratorService.generate();
        var noteWithId = note.withId(id);
        var entity = noteRepository.save(NoteEntity.fromModel(noteWithId));
        return entity.toModel();
    }

    public Optional<Note> get(String id) {
        return noteRepository.findById(id).map(NoteEntity::toModel);
    }
}
