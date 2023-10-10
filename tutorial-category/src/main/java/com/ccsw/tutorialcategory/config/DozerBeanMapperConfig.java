package com.ccsw.tutorialcategory.config;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ccsw
 * 
 */
@Configuration
public class DozerBeanMapperConfig {

    @Bean
    public DozerBeanMapper getDozerBeanMapper() {

        return new DozerBeanMapper();
    }

}