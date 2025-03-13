package com.hunnit_beasts.payment.etc.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("release")
@Log4j2
public class DataSourceConfig {

    // 일단 데모로 사용함 나중에 실제 DB와 연결 시 변경예정
    @Bean
    public DataSource dataSource() {
        log.info("--- Use MySQL in 'release environment'. ---");
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://localhost:3306/parkingPayment?serverTimezone=Asia/Seoul")
                .username("root")
                .password("0000")
                .build();
    }
}
