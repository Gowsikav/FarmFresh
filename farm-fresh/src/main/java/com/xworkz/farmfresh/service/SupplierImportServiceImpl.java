package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.dto.SupplierDTO;
import com.xworkz.farmfresh.repository.SupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class SupplierImportServiceImpl implements SupplierImportService {

    @Autowired
    private Validator validator;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductPriceService productPriceService;

    @Override
    public List<Integer> importSuppliersFromExcel(String filePath) {
        List<Integer> invalidRows = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (!rows.hasNext()) {
                log.warn("Excel file is empty");
                return invalidRows;
            }

            Row headerRow = rows.next();
            Map<String, Integer> columnMap = mapHeaderColumns(headerRow);
            List<String> allowedMilkTypes = productPriceService.productListForBuy()
                    .stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());

            int rowNumber = 1;
            while (rows.hasNext()) {
                Row row = rows.next();
                rowNumber++;

                SupplierDTO dto = extractSupplierFromRow(row, columnMap);
                log.error("dto: {}",dto);
                Set<ConstraintViolation<SupplierDTO>> violations = validator.validate(dto);

                boolean duplicateEmail = supplierRepository.checkEmail(dto.getEmail());
                boolean duplicatePhone = supplierRepository.checkPhoneNumber(dto.getPhoneNumber());
                boolean invalidMilkType = false;

                String excelMilkType = dto.getTypeOfMilk();
                if (excelMilkType != null && !excelMilkType.trim().isEmpty()) {
                    String milkWord = excelMilkType.toLowerCase().trim();
                    boolean matchFound = allowedMilkTypes.stream()
                            .anyMatch(type -> type.contains(milkWord));

                    if (!matchFound) {
                        invalidMilkType = true;
                    }
                }

                if (!violations.isEmpty() || duplicateEmail || duplicatePhone || invalidMilkType) {
                    invalidRows.add(rowNumber);
                    log.warn("Invalid row {} - {}", rowNumber,
                            invalidMilkType ? "Invalid milk type: " + dto.getTypeOfMilk() :
                                    !violations.isEmpty() ? violations.iterator().next().getMessage() :
                                            duplicateEmail ? "Duplicate Email" :
                                                    "Duplicate Phone"
                    );
                } else {
                    // If valid, you’ll call save logic separately in controller
                    log.info("Valid row {} - {}", rowNumber, dto.getEmail());
                }
            }

        } catch (Exception e) {
            log.error("Error processing Excel file: {}", e.getMessage());
        }

        return invalidRows;
    }

    private Map<String, Integer> mapHeaderColumns(Row headerRow) {
        Map<String, Integer> columnMap = new HashMap<>();
        Set<String> validHeaders = new HashSet<>(Arrays.asList(
                "firstname", "lastname", "email", "phonenumber", "address", "typeofmilk", "milk type", "phone"
        ));

        for (Cell cell : headerRow) {
            String col = getCellValue(cell).toLowerCase().trim();
            if (!validHeaders.contains(col)) continue;

            switch (col) {
                case "firstname": columnMap.put("firstName", cell.getColumnIndex()); break;
                case "lastname": columnMap.put("lastName", cell.getColumnIndex()); break;
                case "email": columnMap.put("email", cell.getColumnIndex()); break;
                case "phonenumber":
                case "phone": columnMap.put("phoneNumber", cell.getColumnIndex()); break;
                case "address": columnMap.put("address", cell.getColumnIndex()); break;
                case "typeofmilk":
                case "milk type": columnMap.put("typeOfMilk", cell.getColumnIndex()); break;
            }
        }
        return columnMap;
    }

    private SupplierDTO extractSupplierFromRow(Row row, Map<String, Integer> columnMap) {
        SupplierDTO dto = new SupplierDTO();

        dto.setFirstName(getCellValue(row.getCell(columnMap.getOrDefault("firstName", -1))));
        dto.setLastName(getCellValue(row.getCell(columnMap.getOrDefault("lastName", -1))));
        dto.setEmail(getCellValue(row.getCell(columnMap.getOrDefault("email", -1))));
        dto.setPhoneNumber(getCellValue(row.getCell(columnMap.getOrDefault("phoneNumber", -1))));
        dto.setAddress(getCellValue(row.getCell(columnMap.getOrDefault("address", -1))));
        dto.setTypeOfMilk(getCellValue(row.getCell(columnMap.getOrDefault("typeOfMilk", -1))));

        return dto;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue().trim();
            case NUMERIC: return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }
}

