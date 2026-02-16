package io.kamailio.admin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kamailio.admin.auth.AuthFilter;
import io.kamailio.admin.auth.AuthResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AuthFilterConfig {

    @Value("${auth.open-api.method:API_KEY}")
    private String openApiAuthMethod;

    @Value("${auth.console.method:SESSION}")
    private String consoleAuthMethod;

    @Bean
    public AuthFilter authFilter(List<AuthResolver> resolvers, ObjectMapper objectMapper) {
        return new AuthFilter(resolvers, objectMapper, openApiAuthMethod, consoleAuthMethod);
    }

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilterRegistration(AuthFilter authFilter) {
        FilterRegistrationBean<AuthFilter> reg = new FilterRegistrationBean<>(authFilter);
        reg.addUrlPatterns("/open/*", "/api/*");
        reg.setOrder(1);
        return reg;
    }
}
