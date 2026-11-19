package com.abc.sid.notifsrvc.common.config;

import com.ctc.wstx.stax.WstxOutputFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonEngineConfig {

    @Bean
    public WstxOutputFactory wstxOutputFactory() {
        return new WstxOutputFactory();
    }
}
