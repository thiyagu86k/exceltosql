package com.fiablesystems.exceltosql.model;

import lombok.*;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExcelSheet {
    String sheetName;
    Map<String, List<Object>> rowColumnData;
    long totalNoofRows;
}
