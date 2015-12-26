package org.fenixedu.ulisboa.specifications.service.evaluation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fenixedu.ulisboa.specifications.domain.exceptions.ULisboaSpecificationsDomainException;
import org.fenixedu.ulisboa.specifications.dto.evaluation.markSheet.CompetenceCourseMarkSheetBean;
import org.fenixedu.ulisboa.specifications.dto.evaluation.markSheet.MarkBean;
import org.fenixedu.ulisboa.specifications.util.ULisboaSpecificationsUtil;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

//TODO: improve errors
public class MarkSheetImportExportService {

    private static final int EXPECTED_COLUMNS = 3;

    static public final String XLSX_EXTENSION = ".xlsx";

    static public final String XLSX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    static public byte[] exportToXLSX(final CompetenceCourseMarkSheetBean bean) {

        final Workbook workbook = new XSSFWorkbook();
        final Sheet sheet = workbook.createSheet(ULisboaSpecificationsUtil.bundle("label.CompetenceCourseMarkSheet"));

        writeHeader(sheet);
        writeRows(bean, sheet);

        return writeFile(workbook);
    }

    private static void writeHeader(final Sheet sheet) {
        final Row row = sheet.createRow(0);
        final String[] headers = {

                ULisboaSpecificationsUtil.bundle("label.MarkBean.studentNumber"),

                ULisboaSpecificationsUtil.bundle("label.MarkBean.studentName"),

                ULisboaSpecificationsUtil.bundle("label.MarkBean.gradeValue")

        };

        final Workbook workbook = sheet.getWorkbook();
        final CellStyle style = workbook.createCellStyle();
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillBackgroundColor(IndexedColors.DARK_BLUE.getIndex());
        final Font font = workbook.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        for (int i = 0; i < headers.length; i++) {
            final Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }

    }

    private static void writeRows(CompetenceCourseMarkSheetBean bean, final Sheet sheet) {
        int rowIndex = 1;
        for (final MarkBean markBean : bean.getEvaluations()) {
            final String[] values =
                    { markBean.getStudentNumber().toString(), markBean.getStudentName(), markBean.getGradeValue() };
            final Row row = sheet.createRow(rowIndex++);
            for (int i = 0; i < values.length; i++) {
                row.createCell(i).setCellValue(values[i]);
            }
        }
    }

    private static byte[] writeFile(final Workbook workbook) {
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return baos.toByteArray();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(workbook);
        }
    }

    static public void importFromXLSX(final String filename, byte[] content, CompetenceCourseMarkSheetBean bean) {

        if (!filename.endsWith(XLSX_EXTENSION)) {
            throw new ULisboaSpecificationsDomainException("error.MarkSheetImportExportService.invalid.file.extension");
        }

        XSSFWorkbook workbook = null;
        try {

            workbook = new XSSFWorkbook(new ByteArrayInputStream(content));
            final XSSFSheet sheet = workbook.getSheetAt(0);

            final Row headerRow = sheet.getRow(0);
            if (headerRow.getPhysicalNumberOfCells() != EXPECTED_COLUMNS) {
                throw new ULisboaSpecificationsDomainException("error.MarkSheetImportExportService.unexpected.number.of.columns");
            }

            final ImmutableMap<String, MarkBean> indexedMarkBeans =
                    Maps.uniqueIndex(bean.getEvaluations(), e -> buildMarkIndexKey(e.getStudentNumber(), e.getStudentName()));

            final Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {

                final Row row = rowIterator.next();

                if (row.equals(headerRow)) {
                    continue;
                }

                final Integer studentNumber = Double.valueOf(getCellValueAsString(row.getCell(0))).intValue();
                final String studentName = getCellValueAsString(row.getCell(1));
                final String gradeValue = getCellValueAsString(row.getCell(2));

                final String key = buildMarkIndexKey(studentNumber, studentName);
                if (!indexedMarkBeans.containsKey(key)) {
                    throw new ULisboaSpecificationsDomainException("error.MarkSheetImportExportService.student.not.found",
                            studentNumber.toString(), studentName);
                }

                indexedMarkBeans.get(key).setGradeValue(gradeValue);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(workbook);
        }

    }

    private static String getCellValueAsString(final Cell cell) {

        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {

        case Cell.CELL_TYPE_BLANK:
            return null;
        case Cell.CELL_TYPE_STRING:
            return cell.getStringCellValue();
        case Cell.CELL_TYPE_NUMERIC:
            return String.valueOf(cell.getNumericCellValue());
        default:
            throw new ULisboaSpecificationsDomainException("error.MarkSheetImportExportService.invalid.cell.type",
                    String.valueOf(cell.getRowIndex()));
        }

    }

    private static String buildMarkIndexKey(final Integer number, final String name) {
        return number + "-" + name;
    }

}
