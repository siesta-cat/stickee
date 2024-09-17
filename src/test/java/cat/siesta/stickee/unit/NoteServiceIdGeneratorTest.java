package cat.siesta.stickee.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cat.siesta.stickee.persistence.NoteRepository;
import cat.siesta.stickee.service.NoteIdGeneratorService;

@ExtendWith(MockitoExtension.class)
public class NoteServiceIdGeneratorTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteIdGeneratorService generatorService;

    @Test
    void shouldRegenerateNewIdOnCollision() {
        given(noteRepository.existsById(anyString()))
                .willReturn(true).willReturn(false);

        generatorService.generate();

        verify(noteRepository, times(2)).existsById(anyString());
    }

    @Test
    void shouldGenerateDistinctIds() {
        var numberOfIdsToGenerate = 50;
        given(noteRepository.existsById(anyString())).willReturn(false);

        var distinctGeneratedIds = IntStream.range(0, numberOfIdsToGenerate)
                .mapToObj(i -> generatorService.generate())
                .distinct()
                .collect(Collectors.toList());

        assertEquals(numberOfIdsToGenerate, distinctGeneratedIds.size());
    }
}
