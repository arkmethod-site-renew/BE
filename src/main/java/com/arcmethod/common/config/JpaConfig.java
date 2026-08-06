package com.arcmethod.common.config;

import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "offsetDateTimeProvider")
public class JpaConfig {
    /**
     * 기본 provider는 LocalDateTime을 반환해 OffsetDateTime 필드에 넣지 못한다.
     * BaseTimeEntity가 OffsetDateTime을 쓰므로 provider를 맞춰준다.
     *
     */
    @Bean
    public DateTimeProvider offsetDateTimeProvider(){
        return () -> Optional.of((TemporalAccessor) OffsetDateTime.now());
    }
}
