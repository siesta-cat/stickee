package cat.siesta.stickee.service;

import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.siesta.stickee.persistence.NoteRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoteIdGeneratorService {

    private static final int ID_LENGTH = 5;

    @Autowired
    private NoteRepository noteRepository;

    public String generate() {
        String generatedId = Stream.generate(this::generateSingleId)
                .filter(id -> !noteRepository.existsById(id))
                .findFirst()
                .get();
        return generatedId;
    }

    private String generateSingleId() {
        var id = RandomStringUtils.insecure().nextAlphanumeric(ID_LENGTH);
        log.debug("Generated id {}", id);
        return id;
    }
}
