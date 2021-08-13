package com.fiablesystems.exceltosql;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
public class ExcelToSqlApplication {


    public static void main(String[] args) {

        new SpringApplicationBuilder(ExcelToSqlApplication.class)
                .headless(false)
                .web(WebApplicationType.NONE)
                .run(args);
        //SpringApplication.run(ExcelToSqlApplication.class, args);
    }


}
