package cat.siesta.stickee.mapper;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.stereotype.Component;

@Component
public class TextEncryptor {

    private AES256TextEncryptor encryptor;
    private String key;

    public TextEncryptor(String dbEncryptionKey) {
        this.key = dbEncryptionKey;
        this.encryptor = new AES256TextEncryptor();
        this.encryptor.setPassword(key);
    }

    public String encrypt(String text) {
        return encryptor.encrypt(text);
    }

    public String decrypt(String text) {
        return encryptor.decrypt(text);
    }
}
