package com.dung.order_service.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper init()
    {
        return new ModelMapper();
    }
}
