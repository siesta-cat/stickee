package cat.siesta.stickee.domain;

import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class NoteId {

    private String id;

    public static final int ID_LENGTH = 5;

    public NoteId(String id) {
        if (!isValid(id)) {
            throw new IllegalArgumentException("id doesn't follow the specification");
        }
        this.id = id;
    }

    /**
     * Generates a valid note id following the specifications
     */
    public static NoteId generate() {
        var id = Stream.generate(() -> RandomStringUtils.insecure().nextAlphanumeric(ID_LENGTH))
                .filter(x -> isValid(x))
                .findFirst().get();
        return new NoteId(id);
    }

    private static boolean isValid(String id) {
        return StringUtils.isAlphanumeric(id) && StringUtils.isAsciiPrintable(id)
                && !StringUtils.isEmpty(StringUtils.getDigits(id)) && id.length() == ID_LENGTH;
    }

    @Override
    public String toString() {
        return id;
    }
}
