package fr.csmb.competition.manager;

import com.sun.javafx.collections.transformation.SortedList;
import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.model.comparator.ComparatorClubTotalCombat;
import fr.csmb.competition.model.comparator.ComparatorClubTotalTechnique;
import fr.csmb.competition.model.comparator.ComparatorParticipantCombat;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.type.TypeEpreuve;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;

/**
 * Created by Administrateur on 22/11/14.
 */
public class ResultatsManager {
    public boolean saveResultatFile(File file, CompetitionBean competition) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFFont fontTitle= workbook.createFont();
        fontTitle.setFontHeightInPoints((short)12);
        fontTitle.setFontName("Arial");
        fontTitle.setColor(IndexedColors.BLACK.getIndex());
        fontTitle.setBold(true);
        fontTitle.setItalic(false);

        XSSFFont fontTitle2= workbook.createFont();
        fontTitle2.setFontHeightInPoints((short)10);
        fontTitle2.setFontName("Arial");
        fontTitle2.setColor(IndexedColors.BLACK.getIndex());
        fontTitle2.setBold(true);
        fontTitle2.setItalic(false);

        XSSFFont fontData= workbook.createFont();
        fontData.setFontHeightInPoints((short)10);
        fontData.setFontName("Arial");
        fontData.setColor(IndexedColors.BLACK.getIndex());
        fontData.setBold(false);
        fontData.setItalic(false);

        XSSFCellStyle styleTitle = workbook.createCellStyle();
        styleTitle.setFont(fontTitle);
        styleTitle.setFillForegroundColor(new XSSFColor(new Color(244, 176, 132)));
        styleTitle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleTitle.setAlignment(CellStyle.ALIGN_CENTER);
        styleTitle.setBorderBottom(CellStyle.BORDER_MEDIUM);
        styleTitle.setBorderLeft(CellStyle.BORDER_MEDIUM);
        styleTitle.setBorderRight(CellStyle.BORDER_MEDIUM);
        styleTitle.setBorderTop(CellStyle.BORDER_MEDIUM);

        XSSFCellStyle styleTitle2 = workbook.createCellStyle();
        styleTitle2.setFont(fontTitle2);
        styleTitle2.setFillForegroundColor(new XSSFColor(new Color(244, 176, 132)));
        styleTitle2.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleTitle2.setAlignment(CellStyle.ALIGN_CENTER);
        styleTitle2.setBorderBottom(CellStyle.BORDER_MEDIUM);
        styleTitle2.setBorderLeft(CellStyle.BORDER_MEDIUM);
        styleTitle2.setBorderRight(CellStyle.BORDER_MEDIUM);
        styleTitle2.setBorderTop(CellStyle.BORDER_MEDIUM);

        XSSFCellStyle styleData = workbook.createCellStyle();
        styleData.setFont(fontData);
        styleData.setAlignment(CellStyle.ALIGN_CENTER);
        styleData.setBorderBottom(CellStyle.BORDER_THIN);
        styleData.setBorderLeft(CellStyle.BORDER_THIN);
        styleData.setBorderRight(CellStyle.BORDER_THIN);
        styleData.setBorderTop(CellStyle.BORDER_THIN);

        createClassementByEpreuve(competition, workbook, styleTitle, styleTitle2, styleData);
        createClassementClubByType(competition, workbook, styleTitle, styleTitle2, styleData);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Create classement table for different Endded epreuve
     * @param competitionBean
     * @param workbook
     * @param styleTitle
     * @param styleTitle2
     * @param styleData
     */
    private void createClassementByEpreuve(CompetitionBean competitionBean, XSSFWorkbook workbook,
                                           XSSFCellStyle styleTitle, XSSFCellStyle styleTitle2, XSSFCellStyle styleData) {
        for (CategorieBean categorieBean : competitionBean.getCategories()) {
            boolean isSheetCreated = false;
            XSSFSheet sheet = null;
            int rowCount = 0;
            int colCount = 0;
            String[] typeEpreuves = new String[]{ TypeEpreuve.TECHNIQUE.getValue(), TypeEpreuve.COMBAT.getValue()};
            for (String typeEpreuve : typeEpreuves) {
                boolean isHeaderCreated = false;
                if (typeEpreuve.equals(TypeEpreuve.TECHNIQUE.getValue())) {
                    colCount = 0;
                } else {
                    colCount = 4;
                }
                rowCount = 0;
                for (EpreuveBean epreuveBean : competitionBean.getEpreuves()) {
                    if (epreuveBean.getDiscipline().getType().equals(typeEpreuve) && EtatEpreuve.TERMINE.getValue().equals(epreuveBean.getEtat())) {
                        if (!isSheetCreated) {
                            sheet = workbook.createSheet(categorieBean.getType().concat(" - ").concat(categorieBean.getNom()));
                            isSheetCreated = true;
                        }
                        //Create Header for Type Epreuve
                        if (!isHeaderCreated) {
                            createHeaderForClassementParticipant(sheet, rowCount, colCount, styleTitle, typeEpreuve);
                            rowCount += 2;
                            isHeaderCreated = true;
                        }

                        //Create cell for Epreuve Name
                        Row rowEpreuve = getRow(sheet, rowCount++);
                        Cell cellEpreuve = rowEpreuve.createCell(colCount + 1);
                        cellEpreuve.setCellStyle(styleTitle2);
                        cellEpreuve.setCellValue(epreuveBean.getDiscipline().getNom());
                        //Add style for all column
                        for (int i = colCount + 2; i <= colCount + 4; i++) {
                            Cell tempCellClassementGene = rowEpreuve.createCell(i);
                            tempCellClassementGene.setCellStyle(styleTitle2);
                        }
                        //Merge 4 column for Epreuve Row
                        sheet.addMergedRegion(new CellRangeAddress(rowEpreuve.getRowNum(), rowEpreuve.getRowNum(), colCount + 1, colCount + 4));
                        createBodyForClassementParticipant(sheet, rowCount, colCount, styleData, competitionBean, epreuveBean);
                        rowCount +=4;
                    }
                }
            }
        }
    }

    /**
     * Create header of participant classement
     * @param sheet
     * @param rowCount
     * @param colCount
     * @param styleTitle
     * @param title
     */
    private void createHeaderForClassementParticipant(XSSFSheet sheet, int rowCount, int colCount, XSSFCellStyle styleTitle, String title) {
        Row rowTypeEpreuve = getRow(sheet, rowCount);
        Cell cellTypeEpreuve = rowTypeEpreuve.createCell(colCount + 1);
        cellTypeEpreuve.setCellStyle(styleTitle);
        cellTypeEpreuve.setCellValue(title);
        Row rowTitle = getRow(sheet, rowCount + 1);
        Cell cell1 = rowTitle.createCell(colCount + 1);
        cell1.setCellValue("Place");
        cell1.setCellStyle(styleTitle);
        Cell cell2 = rowTitle.createCell(colCount + 2);
        cell2.setCellValue("Nom");
        cell2.setCellStyle(styleTitle);
        Cell cell3 = rowTitle.createCell(colCount + 3);
        cell3.setCellValue("Prénom");
        cell3.setCellStyle(styleTitle);
        Cell cell4 = rowTitle.createCell(colCount + 4);
        cell4.setCellValue("Club");
        cell4.setCellStyle(styleTitle);
        sheet.addMergedRegion(new CellRangeAddress(rowTypeEpreuve.getRowNum(), rowTypeEpreuve.getRowNum(), colCount + 1, colCount + 4));
    }

    /**
     * Create body of participant classement
     * @param sheet
     * @param rowCount
     * @param colCount
     * @param styleData
     * @param competitionBean
     * @param epreuveBean
     */
    private void createBodyForClassementParticipant(XSSFSheet sheet, int rowCount, int colCount, XSSFCellStyle styleData, CompetitionBean competitionBean, EpreuveBean epreuveBean) {
        SortedList<ParticipantBean> sortableList = new SortedList<ParticipantBean>(epreuveBean.getParticipants());
        sortableList.setComparator(new ComparatorParticipantCombat());
        sortableList.sort();
        int nbPart = 0;
        //Create Cell for Participant. Just the fourth participant
        for (ParticipantBean participantBean : sortableList) {
            if (participantBean.getClassementFinal() > 0 && participantBean.getClassementFinal() < 5) {
                Row rowParticipant = getRow(sheet, nbPart + rowCount);
                Cell cell1 = rowParticipant.createCell(colCount + 1);
                cell1.setCellStyle(styleData);
                cell1.setCellValue(participantBean.getClassementFinal());
                Cell cell2 = rowParticipant.createCell(colCount + 2);
                cell2.setCellStyle(styleData);
                cell2.setCellValue(participantBean.getNom());
                Cell cell3 = rowParticipant.createCell(colCount + 3);
                cell3.setCellStyle(styleData);
                cell3.setCellValue(participantBean.getPrenom());
                Cell cell4 = rowParticipant.createCell(colCount + 4);
                cell4.setCellStyle(styleData);
                ClubBean clubBean = getClubById(competitionBean, participantBean.getClub());
                if (clubBean != null) {
                    cell4.setCellValue(clubBean.getNom());
                } else {
                    cell4.setCellValue("---");
                }
                nbPart++;
            }
        }
        while (nbPart < 4) {
            Row rowParticipant = getRow(sheet, nbPart + rowCount);
            Cell cell1 = rowParticipant.createCell(colCount + 1);
            cell1.setCellStyle(styleData);
            cell1.setCellValue(nbPart + 1);
            Cell cell2 = rowParticipant.createCell(colCount + 2);
            cell2.setCellStyle(styleData);
            cell2.setCellValue("");
            Cell cell3 = rowParticipant.createCell(colCount + 3);
            cell3.setCellStyle(styleData);
            cell3.setCellValue("");
            Cell cell4 = rowParticipant.createCell(colCount + 4);
            cell4.setCellStyle(styleData);
            cell4.setCellValue("");
            nbPart++;
        }
        for (int columnPosition = 0; columnPosition < 9; columnPosition++) {
            sheet.autoSizeColumn(columnPosition);
        }
    }

    private void createClassementClubByType(CompetitionBean competitionBean, XSSFWorkbook workbook,
                                            XSSFCellStyle styleTitle, XSSFCellStyle styleTitle2, XSSFCellStyle styleData) {

        //Classement des clubs
        XSSFSheet sheet = workbook.createSheet("Classement des clubs");
        int rowCount = 1;
        int colCount = 0;

        createHeaderForClassementClub(sheet, rowCount, colCount, styleTitle);
        rowCount += 1;
        createClassementClub(sheet, rowCount, colCount, styleTitle2, styleData, competitionBean, TypeEpreuve.TECHNIQUE);
        rowCount += competitionBean.getClubs().size() + 1;
        createClassementClub(sheet, rowCount, colCount, styleTitle2, styleData, competitionBean, TypeEpreuve.COMBAT);
        rowCount += competitionBean.getClubs().size() + 1;
        createClassementClub(sheet, rowCount, colCount, styleTitle2, styleData, competitionBean, null);
        rowCount += competitionBean.getClubs().size() + 1;

        for (int columnPosition = 0; columnPosition < 7; columnPosition++) {
            sheet.autoSizeColumn(columnPosition);
        }
    }

    private void createHeaderForClassementClub(XSSFSheet sheet, int rowCount, int colCount, XSSFCellStyle styleTitle) {
        Row rowTitle = sheet.createRow(rowCount);
        Cell cell1 = rowTitle.createCell(colCount + 1);
        cell1.setCellValue("Place ");
        cell1.setCellStyle(styleTitle);
        Cell cell2 = rowTitle.createCell(colCount + 2);
        cell2.setCellValue("Identifiant Club");
        cell2.setCellStyle(styleTitle);
        Cell cell3 = rowTitle.createCell(colCount + 3);
        cell3.setCellValue("Nom Club");
        cell3.setCellStyle(styleTitle);
        Cell cell4 = rowTitle.createCell(colCount + 4);
        cell4.setCellValue("Total Technique");
        cell4.setCellStyle(styleTitle);
        Cell cell5 = rowTitle.createCell(colCount + 5);
        cell5.setCellValue("Total Combat");
        cell5.setCellStyle(styleTitle);
        Cell cell6 = rowTitle.createCell(colCount + 6);
        cell6.setCellValue("Total Général");
        cell6.setCellStyle(styleTitle);
    }

    private void createClassementClub(XSSFSheet sheet, int rowCount, int colCount, XSSFCellStyle styleTitle2,
                                               XSSFCellStyle styleData, CompetitionBean competitionBean, TypeEpreuve typeEpreuve) {
        String title = "Classement Général";
        Comparator comparator = null;
        if (typeEpreuve != null) {
            switch (typeEpreuve) {
                case TECHNIQUE:
                    title = "Classement Technique";
                    comparator = new ComparatorClubTotalTechnique();
                    break;
                case COMBAT:
                    title = "Classement Combat";
                    comparator = new ComparatorClubTotalCombat();
                    break;
            }
        }

        Row rowClassementTech = sheet.createRow(rowCount);
        Cell cellClassementTech = rowClassementTech.createCell(colCount + 1);
        cellClassementTech.setCellStyle(styleTitle2);

        cellClassementTech.setCellValue(title);
        for (int i = colCount + 2; i <= colCount + 6; i++) {
            Cell tempCellClassementGene = rowClassementTech.createCell(i);
            tempCellClassementGene.setCellStyle(styleTitle2);
        }
        sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, colCount + 1, colCount + 6));

        SortedList<ClubBean> sortableList = new SortedList<ClubBean>(competitionBean.getClubs());
        if (comparator != null) {
            sortableList.setComparator(comparator);
        }
        sortableList.sort();
        int i = 1;
        for (ClubBean clubBean : sortableList) {
            Row rowClub = sheet.createRow(rowCount + i);
            createClassementClubTable(clubBean, rowClub, colCount, styleData, typeEpreuve);
            i++;
        }
    }

    private void createClassementClubTable(ClubBean clubBean, Row rowClub, int colCount, XSSFCellStyle styleData, TypeEpreuve typeEpreuve) {
        Cell cell1 = rowClub.createCell(colCount + 1);
        if (typeEpreuve == null) {
            cell1.setCellValue(clubBean.getClassementGeneral());
        } else if (typeEpreuve.getValue().equals(TypeEpreuve.TECHNIQUE.getValue())) {
            cell1.setCellValue(clubBean.getClassementTechnique());
        } else if (typeEpreuve.getValue().equals(TypeEpreuve.COMBAT.getValue())) {
            cell1.setCellValue(clubBean.getClassementCombat());
        }
        cell1.setCellStyle(styleData);
        Cell cell2 = rowClub.createCell(colCount + 2);
        cell2.setCellValue(clubBean.getIdentifiant());
        cell2.setCellStyle(styleData);
        Cell cell3 = rowClub.createCell(colCount + 3);
        cell3.setCellValue(clubBean.getNom());
        cell3.setCellStyle(styleData);
        Cell cell4 = rowClub.createCell(colCount + 4);
        cell4.setCellValue(clubBean.getTotalTechnique());
        cell4.setCellStyle(styleData);
        Cell cell5 = rowClub.createCell(colCount + 5);
        cell5.setCellValue(clubBean.getTotalCombat());
        cell5.setCellStyle(styleData);
        Cell cell6 = rowClub.createCell(colCount + 6);
        cell6.setCellValue(clubBean.getTotalGeneral());
        cell6.setCellStyle(styleData);
    }

    private Row getRow(XSSFSheet sheet, int rowCount) {
        Row row = sheet.getRow(rowCount);
        if (row == null) {
            row = sheet.createRow(rowCount);
        }
        return row;
    }

    private ClubBean getClubById(CompetitionBean competitionBean, String id) {
        for (ClubBean clubBean : competitionBean.getClubs()) {
            if (clubBean.getIdentifiant().equals(id)) {
                return clubBean;
            }
        }
        return null;
    }
}
