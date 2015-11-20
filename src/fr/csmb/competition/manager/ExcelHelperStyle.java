package fr.csmb.competition.manager;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;

/**
 * Created by Administrateur on 20/11/15.
 */
public class ExcelHelperStyle {
    public static XSSFFont getGeneralFontCell(XSSFWorkbook workbook) {
        XSSFFont fontCellGeneral= workbook.createFont();
        fontCellGeneral.setFontHeightInPoints((short) 12);
        fontCellGeneral.setFontName("Arial");
        fontCellGeneral.setColor(IndexedColors.BLACK.getIndex());
        fontCellGeneral.setBold(false);
        fontCellGeneral.setItalic(false);

        return fontCellGeneral;
    }

    public static XSSFFont getFontBoldBlack12(XSSFWorkbook workbook) {
        XSSFFont fontCellTotal= workbook.createFont();
        fontCellTotal.setFontHeightInPoints((short) 12);
        fontCellTotal.setFontName("Arial");
        fontCellTotal.setColor(IndexedColors.BLACK.getIndex());
        fontCellTotal.setBold(true);
        fontCellTotal.setItalic(false);

        return fontCellTotal;
    }

    public static XSSFFont getFontBoldWhite12(XSSFWorkbook workbook) {
        XSSFFont fontCellTitle= workbook.createFont();
        fontCellTitle.setFontHeightInPoints((short) 12);
        fontCellTitle.setFontName("Arial");
        fontCellTitle.setColor(IndexedColors.WHITE.getIndex());
        fontCellTitle.setBold(true);
        fontCellTitle.setItalic(false);

        return fontCellTitle;
    }

    public static XSSFFont getFontBlack20(XSSFWorkbook workbook) {
        XSSFFont fontCellEpreuve= workbook.createFont();
        fontCellEpreuve.setFontHeightInPoints((short) 20);
        fontCellEpreuve.setFontName("Arial");
        fontCellEpreuve.setColor(IndexedColors.BLACK.getIndex());
        fontCellEpreuve.setBold(true);
        fontCellEpreuve.setItalic(false);

        return fontCellEpreuve;
    }

    public static XSSFCellStyle getStyleCellEpreuve(XSSFWorkbook workbook) {
        XSSFFont fontCellEpreuve= ExcelHelperStyle.getFontBlack20(workbook);

        XSSFCellStyle styleCellEpreuve = workbook.createCellStyle();
        styleCellEpreuve.setFont(fontCellEpreuve);
        styleCellEpreuve.setFillForegroundColor(new XSSFColor(new Color(255, 255, 255)));
        styleCellEpreuve.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleCellEpreuve.setAlignment(CellStyle.ALIGN_CENTER);
        styleCellEpreuve.setBorderBottom(CellStyle.BORDER_MEDIUM);
        styleCellEpreuve.setBorderLeft(CellStyle.BORDER_MEDIUM);
        styleCellEpreuve.setBorderRight(CellStyle.BORDER_MEDIUM);
        styleCellEpreuve.setBorderTop(CellStyle.BORDER_MEDIUM);
        return styleCellEpreuve;
    }

    public static XSSFCellStyle getStyleCellHeaderRow(XSSFWorkbook workbook) {
        XSSFFont fontCellTitle= ExcelHelperStyle.getFontBoldWhite12(workbook);

        XSSFCellStyle styleCellTitle = workbook.createCellStyle();
        styleCellTitle.setFont(fontCellTitle);
        styleCellTitle.setFillForegroundColor(new XSSFColor(new Color(226, 107, 10)));
        styleCellTitle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleCellTitle.setAlignment(CellStyle.ALIGN_CENTER);
        styleCellTitle.setBorderBottom(CellStyle.BORDER_MEDIUM);
        styleCellTitle.setBorderLeft(CellStyle.BORDER_MEDIUM);
        styleCellTitle.setBorderRight(CellStyle.BORDER_MEDIUM);
        styleCellTitle.setBorderTop(CellStyle.BORDER_MEDIUM);
        return styleCellTitle;
    }

    public static XSSFCellStyle getStyleCellPairLine(XSSFWorkbook workbook) {
        XSSFFont fontCell= ExcelHelperStyle.getGeneralFontCell(workbook);

        XSSFCellStyle styleCellTitle = workbook.createCellStyle();
        styleCellTitle.setFont(fontCell);
        styleCellTitle.setFillForegroundColor(new XSSFColor(new Color(255, 191, 143)));
        styleCellTitle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleCellTitle.setAlignment(CellStyle.ALIGN_CENTER);
        styleCellTitle.setBorderBottom(CellStyle.BORDER_MEDIUM);
        styleCellTitle.setBorderLeft(CellStyle.BORDER_MEDIUM);
        styleCellTitle.setBorderRight(CellStyle.BORDER_MEDIUM);
        styleCellTitle.setBorderTop(CellStyle.BORDER_MEDIUM);
        return styleCellTitle;
    }

    public static XSSFCellStyle getStyleCellImpairLine(XSSFWorkbook workbook) {
        XSSFFont fontCell= ExcelHelperStyle.getGeneralFontCell(workbook);

        XSSFCellStyle styleCellTitle = workbook.createCellStyle();
        styleCellTitle.setFont(fontCell);
        styleCellTitle.setFillForegroundColor(new XSSFColor(new Color(253, 233, 217)));
        styleCellTitle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleCellTitle.setAlignment(CellStyle.ALIGN_CENTER);
        styleCellTitle.setBorderBottom(CellStyle.BORDER_MEDIUM);
        styleCellTitle.setBorderLeft(CellStyle.BORDER_MEDIUM);
        styleCellTitle.setBorderRight(CellStyle.BORDER_MEDIUM);
        styleCellTitle.setBorderTop(CellStyle.BORDER_MEDIUM);
        return styleCellTitle;
    }

    public static XSSFCellStyle getStyleCellCategorie(XSSFWorkbook workbook) {
        XSSFFont fontCell= ExcelHelperStyle.getGeneralFontCell(workbook);

        XSSFCellStyle styleCellCategorie = workbook.createCellStyle();
        styleCellCategorie.setFont(fontCell);
        styleCellCategorie.setFillForegroundColor(new XSSFColor(new Color(255, 191, 143)));
        styleCellCategorie.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleCellCategorie.setAlignment(CellStyle.ALIGN_CENTER);
        styleCellCategorie.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        styleCellCategorie.setBorderBottom(CellStyle.BORDER_MEDIUM);
        styleCellCategorie.setBorderLeft(CellStyle.BORDER_MEDIUM);
        styleCellCategorie.setBorderRight(CellStyle.BORDER_MEDIUM);
        styleCellCategorie.setBorderTop(CellStyle.BORDER_MEDIUM);
        return styleCellCategorie;
    }

    public static XSSFCellStyle getStyleCellSexe(XSSFWorkbook workbook) {
        XSSFFont fontCell= ExcelHelperStyle.getGeneralFontCell(workbook);

        XSSFCellStyle styleCellSexe = workbook.createCellStyle();
        styleCellSexe.setFont(fontCell);
        styleCellSexe.setFillForegroundColor(new XSSFColor(new Color(253, 233, 217)));
        styleCellSexe.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleCellSexe.setAlignment(CellStyle.ALIGN_CENTER);
        styleCellSexe.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        styleCellSexe.setBorderBottom(CellStyle.BORDER_MEDIUM);
        styleCellSexe.setBorderLeft(CellStyle.BORDER_MEDIUM);
        styleCellSexe.setBorderRight(CellStyle.BORDER_MEDIUM);
        styleCellSexe.setBorderTop(CellStyle.BORDER_MEDIUM);
        return styleCellSexe;
    }

    public static XSSFCellStyle getStyleCellTotal(XSSFWorkbook workbook) {
        XSSFFont fontCellTotal= ExcelHelperStyle.getFontBoldBlack12(workbook);

        XSSFCellStyle styleCellTotal = workbook.createCellStyle();
        styleCellTotal.setFont(fontCellTotal);
        styleCellTotal.setFillForegroundColor(new XSSFColor(new Color(217, 217, 217)));
        styleCellTotal.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleCellTotal.setAlignment(CellStyle.ALIGN_CENTER);
        styleCellTotal.setBorderBottom(CellStyle.BORDER_MEDIUM);
        styleCellTotal.setBorderLeft(CellStyle.BORDER_MEDIUM);
        styleCellTotal.setBorderRight(CellStyle.BORDER_MEDIUM);
        styleCellTotal.setBorderTop(CellStyle.BORDER_MEDIUM);
        return styleCellTotal;
    }

    public static XSSFCellStyle getStyleCellTotal1(XSSFWorkbook workbook) {
        XSSFFont fontCell= ExcelHelperStyle.getGeneralFontCell(workbook);

        XSSFCellStyle styleCellTotal = workbook.createCellStyle();
        styleCellTotal.setFont(fontCell);
        styleCellTotal.setAlignment(CellStyle.ALIGN_CENTER);
        styleCellTotal.setBorderRight(CellStyle.BORDER_MEDIUM);
        return styleCellTotal;
    }
}
