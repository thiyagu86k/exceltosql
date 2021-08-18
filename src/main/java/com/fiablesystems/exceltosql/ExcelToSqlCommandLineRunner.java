package com.fiablesystems.exceltosql;

import com.fiablesystems.exceltosql.model.ExcelData;
import com.fiablesystems.exceltosql.services.ExcelReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
public class ExcelToSqlCommandLineRunner implements CommandLineRunner {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        File file = ResourceUtils.getFile("classpath:TestXls.xlsx");
        System.out.println(file.getAbsolutePath());
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
                createTableSQL.append(s).append(" LONGVARCHAR");
                if(index!=len){
                    createTableSQL.append(",");
                }
                index++;
            }
            createTableSQL.append(")");
            jdbcTemplate.execute(createTableSQL.toString());
            jdbcTemplate.execute("commit");
            String[] strArray=keySet.toArray(new String[keySet.size()]);
            List<Integer> eachColumnRowSize=new ArrayList<>();
            for(String s:strArray){
                eachColumnRowSize.add(e.getRowColumnData().get(s).size());
            }
            int highestLength= Collections.max(eachColumnRowSize);

            int lenKeySet= keySet.size()-1;

            List<String> stringList=new ArrayList<>();
            StringBuffer stringBuffer=new StringBuffer();
            StringBuffer valueBuffer=new StringBuffer();
            int indx=0;
            for(int i=0;i<highestLength;i++) {
                int mapKey=0;

                for (String s : strArray) {
                    if (mapKey == 0) {
                        stringBuffer.delete(0, stringBuffer.length());
                        valueBuffer.delete(0, valueBuffer.length());
                        stringBuffer.append("insert into ");
                        stringBuffer.append(e.getSheetName());
                        stringBuffer.append(" (");
                        valueBuffer.append("values (");
                    }
                    stringBuffer.append(s+",");
                    valueBuffer.append("\'"+e.getRowColumnData().get(s).get(indx)+"\',");
                    if (mapKey == lenKeySet) {
                        valueBuffer.deleteCharAt(valueBuffer.lastIndexOf(","));
                        stringBuffer.deleteCharAt(stringBuffer.lastIndexOf(","));
                        String insertStmt = valueBuffer.insert(0, stringBuffer.append(") ").toString()).append(")").toString();
                        System.out.println(insertStmt);
                        stringList.add(insertStmt);

                        mapKey = 0;
                    }

                    mapKey++;
                }
                indx++;
            }
            for(String str:stringList){
                jdbcTemplate.execute(str);
            }
            jdbcTemplate.execute("shutdown");

            //System.out.println(createTableSQL.toString());
         //   jdbcTemplate.execute(" CREATE TABLE ")
        });

       // ;
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
