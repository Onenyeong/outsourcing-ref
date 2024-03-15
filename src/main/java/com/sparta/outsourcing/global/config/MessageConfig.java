package com.sparta.outsourcing.global.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageConfig {
  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasenames("custommessage/message");
    messageSource.setFallbackToSystemLocale(false);
    messageSource.setDefaultEncoding("UTF-8");

    return messageSource;
  }

}
