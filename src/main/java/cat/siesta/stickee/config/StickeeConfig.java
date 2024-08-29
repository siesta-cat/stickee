package cat.siesta.stickee.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.unit.DataSize;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "notes")
@PropertySource("classpath:stickee.properties")
@Getter
@Setter
public class StickeeConfig {
    private String basePath;
    private Duration maxAge;
    private Long deletionDelay;
    private DataSize maxSize;
}
