package cat.siesta.stickee.unit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import cat.siesta.stickee.domain.NoteId;

public class NoteIdTest {

    private Integer limit = 100;
    private List<NoteId> ids = Stream.generate(() -> NoteId.generate()).limit(limit).toList();

    @Test
    void shouldHaveSpecifiedLength() {
        ids.forEach(id -> assertEquals(id.getId().length(), NoteId.ID_LENGTH));
    }

    @Test
    void shouldBeAsciiAndAlphanumeric() {
        ids.forEach(id -> assertTrue(StringUtils.isAsciiPrintable(id.getId())
                && StringUtils.isAlphanumeric(id.getId())));
    }

    @Test
    void shouldContainAtLeastOneNumber() {
        ids.forEach(id -> assertTrue(StringUtils.isNotEmpty(StringUtils.getDigits(id.getId()))));
    }

    @Test
    void shouldExceptOnInvalidId() {
        assertThrows(IllegalArgumentException.class,
                () -> new NoteId(RandomStringUtils.insecure().nextAlphabetic(NoteId.ID_LENGTH)));
        assertThrows(IllegalArgumentException.class, () -> new NoteId(""));
        assertThrows(IllegalArgumentException.class,
                () -> new NoteId(RandomStringUtils.insecure().next(NoteId.ID_LENGTH, "€áñ")));
        assertDoesNotThrow(() -> new NoteId(RandomStringUtils.insecure().nextAlphanumeric(NoteId.ID_LENGTH - 1) + "1"));
    }

    @Test
    void shouldReturnIdOnToString() {
        var id = ids.get(0);
        assertEquals(id.toString(), id.getId());
    }

}
