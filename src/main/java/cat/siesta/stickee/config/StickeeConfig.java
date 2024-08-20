package cat.siesta.stickee.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.unit.DataSize;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties
@PropertySource("classpath:stickee.yaml")
@Getter
@Setter
public class StickeeConfig {
    private String notesBasePath;
    private Duration noteMaxAge;
    private DataSize noteMaxSize;
}