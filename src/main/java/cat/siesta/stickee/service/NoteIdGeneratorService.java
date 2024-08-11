package cat.siesta.stickee.service;

import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.siesta.stickee.persistence.NoteRepository;

@Service
public class NoteIdGeneratorService {

    private static final int ID_LENGTH = 5;

    @Autowired
    private NoteRepository noteRepository;

    public String generate() {
        String generatedId = Stream.generate(() -> RandomStringUtils.randomAlphanumeric(ID_LENGTH))
            .filter(id -> !noteRepository.existsByResourceLocator(id))
            .findFirst()
            .get();
        return generatedId;
    }
}
