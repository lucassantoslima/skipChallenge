package com.skipthedishes.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.skipthedishes")
@PropertySource("classpath:persistence-generic-entity.properties")
@EnableTransactionManagement
public class H2JpaConfig { 

}
