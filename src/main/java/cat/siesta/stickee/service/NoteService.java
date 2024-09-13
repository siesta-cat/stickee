package cat.siesta.stickee.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.siesta.stickee.domain.Note;
import cat.siesta.stickee.mapper.NoteEntityMapper;
import cat.siesta.stickee.persistence.NoteRepository;

@Service
public class NoteService {

    @Autowired
    private NoteEntityMapper mapper;

    @Autowired
    private NoteIdGeneratorService idGeneratorService;

    @Autowired
    private NoteRepository noteRepository;

    public Note create(Note note) {
        var id = idGeneratorService.generate();
        var noteWithId = note.withId(id);
        var entity = noteRepository.save(mapper.fromModel(noteWithId));
        return mapper.toModel(entity);
    }

    public Optional<Note> get(String id) {
        return noteRepository.findById(id).map(entity -> mapper.toModel(entity));
    }
}
