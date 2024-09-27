package cat.siesta.stickee.config;

import java.time.Duration;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.unit.DataSize;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "notes")
@PropertySource("classpath:stickee.properties")
@Data
public class StickeeConfig {
    private String basePath;
    private Duration maxExpirationTime;
    private Long deletionDelay;
    private DataSize maxSize;
    private String dbEncryptionKey;
    private List<Duration> expirationTimes;
}
