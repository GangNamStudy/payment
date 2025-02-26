package com.hunnit_beasts.payment.etc.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    // 일단 데모로 사용함 나중에 실제 DB와 연결 시 변경예정
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://localhost:3306/parkingPayment?serverTimezone=Asia/Seoul")
                .username("root")
                .password("0000")
                .build();
    }
}