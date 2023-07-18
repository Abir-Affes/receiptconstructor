package tn.idrm.receiptconstructor.security.jwt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import tech.jhipster.config.JHipsterProperties;
import tn.idrm.receiptconstructor.config.SecurityConfiguration;
import tn.idrm.receiptconstructor.config.SecurityJwtConfiguration;
import tn.idrm.receiptconstructor.config.WebConfigurer;
import tn.idrm.receiptconstructor.management.SecurityMetersService;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        JHipsterProperties.class,
        WebConfigurer.class,
        SecurityConfiguration.class,
        SecurityJwtConfiguration.class,
        SecurityMetersService.class,
        JwtAuthenticationTestUtils.class,
    }
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public @interface AuthenticationIntegrationTest {
}
