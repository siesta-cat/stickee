package cat.siesta.stickee.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * This configuration disables scheduling on the test profile.
 * There are scheduled tasks that can produce unexpected results on tests.
 */
@Configuration
@EnableScheduling
@Profile("!test")
public class SchedulingConfig {

}
