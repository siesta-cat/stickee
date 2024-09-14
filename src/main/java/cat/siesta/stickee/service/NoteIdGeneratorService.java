package cat.siesta.stickee.service;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.siesta.stickee.domain.NoteId;
import cat.siesta.stickee.persistence.NoteRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoteIdGeneratorService {

    @Autowired
    private NoteRepository noteRepository;

    public NoteId generate() {
        var generatedId = Stream.generate(() -> NoteId.generate())
                .filter(id -> !noteRepository.existsById(id.getId()))
                .peek(id -> log.debug("Generated id {}", id))
                .findFirst()
                .get();
        return generatedId;
    }
}
