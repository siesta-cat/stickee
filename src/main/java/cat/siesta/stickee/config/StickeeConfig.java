package cat.siesta.stickee.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties
@PropertySource("classpath:stickee.yaml")
@Getter
@Setter
public class StickeeConfig {
    private String notesBasePath;
    private Integer noteMaxAge;
}
