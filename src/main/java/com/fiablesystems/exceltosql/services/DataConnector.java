package com.fiablesystems.exceltosql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataConnector {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void createTable(){
        jdbcTemplate.execute(" CREATE TABLE t(a INTEGER, b BIGINT)");
        System.out.println("Table Created successfully");
    }


}
