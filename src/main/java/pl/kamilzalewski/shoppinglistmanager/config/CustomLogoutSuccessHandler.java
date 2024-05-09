package pl.kamilzalewski.shoppinglistmanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

@Configuration
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    public CustomLogoutSuccessHandler() {
        super();
        setDefaultTargetUrl("/login");
    }
}
