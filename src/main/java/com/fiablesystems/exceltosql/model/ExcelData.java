package com.fiablesystems.exceltosql.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExcelData {
    String filePath;
    List<ExcelSheet> excelSheets;
}
