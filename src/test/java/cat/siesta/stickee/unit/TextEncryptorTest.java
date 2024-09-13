package cat.siesta.stickee.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import cat.siesta.stickee.mapper.TextEncryptor;

public class TextEncryptorTest {
    // Jasypt requires that password must be ASCII
    private String key = RandomStringUtils.insecure().nextAscii(10);

    @Test
    void isTransitive() {
        var encryptor = new TextEncryptor(key);
        var text = RandomStringUtils.insecure().next(10);
        assertEquals(text, encryptor.decrypt(encryptor.encrypt(text)));
    }

    @Test
    void isNonDeterministicOnEncryption() {
        var encryptor = new TextEncryptor(key);
        var text = RandomStringUtils.insecure().next(10);

        assertNotEquals(encryptor.encrypt(text), encryptor.encrypt(text));
    }

}
