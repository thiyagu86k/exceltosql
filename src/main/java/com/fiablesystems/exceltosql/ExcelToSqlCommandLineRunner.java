package com.fiablesystems.exceltosql;

import com.fiablesystems.exceltosql.model.ExcelData;
import com.fiablesystems.exceltosql.services.ExcelReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Set;

@Component
public class ExcelToSqlCommandLineRunner implements CommandLineRunner {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        String file="C:\\thiyagu_data\\coding\\TestXls.xlsx";
        ExcelReader excelReader=new ExcelReader();

       ExcelData excelData= excelReader.loadExcelFile(file);
        excelData.getExcelSheets().forEach(e -> {
           Set<String> keySet=e.getRowColumnData().keySet();
        //    System.out.println(keySet.toString());
            StringBuffer createTableSQL= new StringBuffer("CREATE TABLE ");
            createTableSQL.append(e.getSheetName()).append("( ");
            int len=keySet.size()-1;
            int index=0;
            for(String s: keySet){
                createTableSQL.append(s).append(" STRING");
                if(index!=len){
                    createTableSQL.append(",");
                }
                index++;
            }
            createTableSQL.append(")");
            //System.out.println(createTableSQL.toString());
         //   jdbcTemplate.execute(" CREATE TABLE ")
        });

       // jdbcTemplate.execute(" CREATE TABLE t(a INTEGER, b BIGINT)");
      //  jdbcTemplate.execute("shutdown");
       // System.out.println("Table Created successfully");
    }
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource)
    {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DataSource dataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
        dataSource.setUrl("jdbc:hsqldb:./data/dbData");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

}
