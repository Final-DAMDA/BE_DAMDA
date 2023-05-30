package com.damda.back.config;


import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;


@EnableJpaAuditing
@Configuration
public class CommonConfig {
        @Bean
        public JPAQueryFactory queryFactory(EntityManager em){
            return new JPAQueryFactory(em);
        }
}
