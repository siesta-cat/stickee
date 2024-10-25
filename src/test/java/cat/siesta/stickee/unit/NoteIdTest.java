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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

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

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("generateRandomInvalidIds")
    void shouldExceptOnInvalidId(String invalidId) {
        assertThrows(IllegalArgumentException.class,
                () -> new NoteId(invalidId));
    }

    @Test
    void shouldNotExceptOnValidId() {
        var alphanumericStringWithAtLeastOneNumber = RandomStringUtils.insecure().nextAlphanumeric(NoteId.ID_LENGTH - 1)
                + "1";
        assertDoesNotThrow(() -> new NoteId(alphanumericStringWithAtLeastOneNumber));
    }

    @Test
    void shouldReturnIdOnToString() {
        var id = ids.get(0);
        assertEquals(id.toString(), id.getId());
    }

    public static Stream<String> generateRandomInvalidIds() {
        var idWithoutAnyNumber = RandomStringUtils.insecure().nextAlphabetic(NoteId.ID_LENGTH);
        var idWithInvalidCharacters = RandomStringUtils.insecure().next(NoteId.ID_LENGTH, "€áñ");

        return Stream.of(idWithoutAnyNumber, idWithInvalidCharacters);
    }
}
