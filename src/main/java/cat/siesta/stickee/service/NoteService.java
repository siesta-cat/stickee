package cat.siesta.stickee.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import cat.siesta.stickee.domain.Note;
import cat.siesta.stickee.mapper.NoteEntityMapper;
import cat.siesta.stickee.persistence.NoteRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NoteService {

    private NoteEntityMapper mapper;
    private NoteIdGeneratorService idGeneratorService;
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
