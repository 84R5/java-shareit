package ru.practicum.shareit.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfiguration {
    @Bean
    public java.time.Clock clock() {
        return java.time.Clock.systemUTC();
    }
}