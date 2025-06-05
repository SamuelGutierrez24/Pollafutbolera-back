package co.edu.icesi.pollafutbolera.utils;

import co.edu.icesi.pollafutbolera.dto.PreloadUserDTO;
import co.edu.icesi.pollafutbolera.exception.MissingHeaderException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Component
public class FileUtils {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<PreloadUserDTO> processCSV(MultipartFile file) throws IOException {
        List<PreloadUserDTO> dtoList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVReader csvReader = new CSVReaderBuilder(br)
                     .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                     .build()) {

            String[] headerRow = csvReader.readNext();
            List<String> headers = new ArrayList<>(Arrays.asList(headerRow));
            headers.replaceAll(s -> s.trim().toLowerCase());

            if (!headers.contains("cedula") || !headers.contains("nombre") || !headers.contains("apellido") || !headers.contains("mail")) {
                throw new MissingHeaderException("Encabezados faltantes");
            }

            String[] row;
            while ((row = csvReader.readNext()) != null) {
                Map<String, String> extraInfoMap = new HashMap<>();
                for (int i = 0; i < headers.size(); i++) {
                    String key = headers.get(i);
                    if (!List.of("cedula", "empresa", "nombre", "apellido", "telefono", "mail").contains(key)) {
                        String value = row[i];
                        if (value != null && !value.trim().isEmpty()) {
                            extraInfoMap.put(key, value);
                        }
                    }
                }

                PreloadUserDTO dto = PreloadUserDTO.builder()
                        .cedula(getColumnValue(headers, row, "cedula"))
                        .companyName(getColumnValue(headers, row, "empresa"))
                        .name(getColumnValue(headers, row, "nombre"))
                        .lastName(getColumnValue(headers, row, "apellido"))
                        .phoneNumber(getColumnValue(headers, row, "telefono"))
                        .mail(getColumnValue(headers, row, "mail"))
                        .extraInfo(objectMapper.writeValueAsString(extraInfoMap))
                        .build();

                dtoList.add(dto);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return dtoList;
    }


    private String getColumnValue(List<String> headers, String[] row, String columnName) {
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).equalsIgnoreCase(columnName)) {
                return row[i];
            }
        }
        return null;
    }

    public List<PreloadUserDTO> processExcel(MultipartFile file) {
        List<PreloadUserDTO> dtoList = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue().trim().toLowerCase());
            }

            if (!headers.contains("cedula") || !headers.contains("nombre") || !headers.contains("apellido") || !headers.contains("mail")) {
                throw new MissingHeaderException("Invalid headers");
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                Map<String, String> extraInfoMap = new HashMap<>();
                for (int i = 0; i < headers.size(); i++) {
                    String key = headers.get(i);
                    if (!List.of("cedula", "empresa", "nombre", "apellido", "telefono", "mail").contains(key)) {
                        String value = getCellValue(row, i);
                        if (value != null && !value.trim().isEmpty()) {
                            extraInfoMap.put(key, value);
                        }
                    }
                }

                PreloadUserDTO dto = PreloadUserDTO.builder()
                        .cedula(getCellValue(row, headers.indexOf("cedula")))
                        .companyName(getCellValue(row, headers.indexOf("empresa")))
                        .name(getCellValue(row, headers.indexOf("nombre")))
                        .lastName(getCellValue(row, headers.indexOf("apellido")))
                        .phoneNumber(getCellValue(row, headers.indexOf("telefono")))
                        .mail(getCellValue(row, headers.indexOf("mail")))
                        .extraInfo(objectMapper.writeValueAsString(extraInfoMap))
                        .build();

                dtoList.add(dto);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return dtoList;
    }

    private String getCellValue(Row row, int cellIndex) {
        if (cellIndex < 0) return null;
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }


}

