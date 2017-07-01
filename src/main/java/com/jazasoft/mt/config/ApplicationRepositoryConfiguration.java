package com.jazasoft.mt.config;

import com.jazasoft.mt.audit.AuditingDateTimeProvider;
import com.jazasoft.mt.audit.UsernameAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/** Repository configuration behavior. */
@Configuration
@EnableJpaAuditing(
        dateTimeProviderRef = "dateTimeProvider",
        auditorAwareRef = "auditorAware"
)
public class ApplicationRepositoryConfiguration {

    @Bean(name = "dateTimeProvider")
    public DateTimeProvider dateTimeProvider() {
        return new AuditingDateTimeProvider();
    }

    @Bean(name = "auditorAware")
    public AuditorAware<String> auditorAware() {
        return new UsernameAuditorAware();
    }
}
