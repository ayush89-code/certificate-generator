
package com.example.certgen;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class ExcelService {

    private final String resourcePath = "data.xlsx";

    public List<Map<String, String>> readAll() {
        List<Map<String, String>> rows = new ArrayList<>();
        try (InputStream is = new ClassPathResource(resourcePath).getInputStream();
             Workbook wb = WorkbookFactory.create(is)) {
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            if (!it.hasNext()) return rows;
            Row header = it.next(); // header possibly Date, Name, Description
            List<String> cols = new ArrayList<>();
            for (Cell c : header) {
                c.setCellType(CellType.STRING);
                cols.add(c.getStringCellValue().trim());
            }
            while (it.hasNext()) {
                Row r = it.next();
                Map<String,String> map = new LinkedHashMap<>();
                for (int i=0;i<cols.size();i++) {
                    Cell cell = r.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellType(CellType.STRING);
                    map.put(cols.get(i), cell.getStringCellValue().trim());
                }
                // Require Name (col index 1) not blank; Description col index 2 may be blank but we'll accept
                String name = cols.size()>1 ? map.get(cols.get(1)) : "";
                if (name!=null && !name.isEmpty()) {
                    rows.add(map);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read Excel: " + e.getMessage(), e);
        }
        return rows;
    }

    public Map<String,String> mapNameToDesc() {
        Map<String,String> out = new LinkedHashMap<>();
        for (Map<String,String> r : readAll()) {
            List<String> keys = new ArrayList<>(r.keySet());
            String name = keys.size()>1 ? r.get(keys.get(1)) : "";
            String desc = keys.size()>2 ? r.get(keys.get(2)) : "";
            if (name!=null && !name.isEmpty()) out.put(name, desc==null? "": desc);
        }
        return out;
    }
}
