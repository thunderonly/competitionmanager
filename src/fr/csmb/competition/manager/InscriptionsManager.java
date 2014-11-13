package fr.csmb.competition.manager;

import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.model.comparator.ComparatorClubTotalCombat;
import fr.csmb.competition.model.comparator.ComparatorClubTotalTechnique;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.type.TypeCategorie;
import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.view.NotificationView;
import fr.csmb.competition.xml.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.awt.*;
import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sun.javafx.collections.transformation.SortedList;

/**
 * Created by Administrateur on 13/10/14.
 */
public class InscriptionsManager {

    FormulaEvaluator evaluator;

    public String loadInscription(File file, CompetitionBean competition) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            XSSFSheet sheet = workbook.getSheet("Epreuves");

            Iterator<Row> rowIterator = sheet.rowIterator();

            ClubBean clubBean = new ClubBean();
            while(rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell cellB = row.getCell(1);

                if (cellB!= null && cellB.getCellType() == Cell.CELL_TYPE_STRING &&
                        "N° Club".equals(cellB.getStringCellValue())) {
                    clubBean.setIdentifiant(getCellValue(row, 2));
                } else if (cellB!= null && cellB.getCellType() == Cell.CELL_TYPE_STRING &&
                        "Nom du Club".equals(cellB.getStringCellValue())) {
                    clubBean.setNom(getCellValue(row, 2));
                } else if (cellB!= null && cellB.getCellType() == Cell.CELL_TYPE_STRING &&
                        "Responsable".equals(cellB.getStringCellValue())) {
                    clubBean.setResponsable(getCellValue(row, 2));
                } else if (cellB!= null && cellB.getCellType() == Cell.CELL_TYPE_STRING &&
                        "Renseignements".equals(cellB.getStringCellValue())) {
                    rowIterator.next();
                    Map<String, List<EleveBean>> teamDoiLuyen = new HashMap<String, List<EleveBean>>();
                    while(rowIterator.hasNext()) {
                        EleveBean eleve = new EleveBean();
                        row = rowIterator.next();
                        eleve.setLicence(getCellValue(row, 3));
                        eleve.setNom(getCellValue(row, 4));
                        eleve.setPrenom(getCellValue(row, 5));
                        eleve.setAge(getCellValue(row, 6));

                        eleve.setCategorie(convertCategorie(getCellValue(row, 7)));
                        eleve.setSexe(getCellValue(row, 8));
                        eleve.setPoids(getCellValue(row, 9));
                        String epreuve = getCellValue(row, 10);
                        if ("Oui".equalsIgnoreCase(epreuve)) {
                            eleve.getEpreuves().add("Main Nue");
                        }
                        epreuve = getCellValue(row, 11);
                        if ("Oui".equalsIgnoreCase(epreuve)) {
                            eleve.getEpreuves().add("Arme");
                        }
                        epreuve = getCellValue(row, 12);
                        if (epreuve.contains("Équipe")) {
                            if (teamDoiLuyen.get(epreuve) == null) {
                                teamDoiLuyen.put(epreuve, new ArrayList<EleveBean>());
                            }
                            teamDoiLuyen.get(epreuve).add(eleve);
                        }
                        epreuve = getCellValue(row, 13);
                        if ("Oui".equalsIgnoreCase(epreuve)) {

                            eleve.getEpreuves().add(extractCategorieCombat(eleve, competition));
                        }

                        if (!eleve.getNom().equals("")) {
                            clubBean.getEleves().add(eleve);
                        }
                    }
                    for (String equipe : teamDoiLuyen.keySet()) {
                        clubBean.getEleves().add(extractEleveFromTeam(clubBean.getIdentifiant(), equipe, teamDoiLuyen.get(equipe)));
                    }

                    if (competition.getClubs().contains(clubBean)) {
                        competition.getClubs().remove(clubBean);
                    }
                    competition.getClubs().add(clubBean);
                }
            }
            fileInputStream.close();
            return clubBean.getNom();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private EleveBean extractEleveFromTeam(String clubId, String equipe, List<EleveBean> eleves) {
        EleveBean newEleve = new EleveBean();
        for (EleveBean eleve : eleves) {
            if (newEleve.getLicence() == null) {
                newEleve.setLicence(clubId.concat("-").concat(equipe));
            }

            if (newEleve.getEpreuves().size() <= 0) {
                newEleve.getEpreuves().add("Doi Luyen");
            }
            //Nom
            if (newEleve.getNom() == null) {
                newEleve.setNom(eleve.getNom());
            } else {
                newEleve.setNom(newEleve.getNom().concat("/").concat(eleve.getNom()));
            }
            //Prénom
            if (newEleve.getPrenom() == null) {
                newEleve.setPrenom(eleve.getPrenom());
            } else {
                newEleve.setPrenom(newEleve.getPrenom().concat("/").concat(eleve.getPrenom()));
            }
            //Age
            if (newEleve.getAge() == null) {
                newEleve.setAge(eleve.getAge());
            } else {
                if (Integer.parseInt(newEleve.getAge()) < Integer.parseInt(eleve.getAge())) {
                    newEleve.setAge(eleve.getAge());
                }
            }
            //Sexe
            if (newEleve.getSexe() == null) {
                newEleve.setSexe(eleve.getSexe());
            } else {
                if (eleve.getSexe().equals("Masculin")) {
                    newEleve.setSexe(eleve.getSexe());
                } else if (eleve.getSexe().equals("Féminin") && newEleve.getSexe().equals("Féminin")) {
                    newEleve.setSexe(eleve.getSexe());
                }
            }
            //Categorie
            String currentCategorie;
            if (eleve.getCategorie().equals("Benjamin") || eleve.getCategorie().equals("Minime") || eleve.getCategorie().equals("Cadet")) {
                currentCategorie = "Cadet";
            } else {
                currentCategorie = "Senior";
            }
            if (newEleve.getCategorie() == null) {
               newEleve.setCategorie(currentCategorie);
            } else {
                if (newEleve.getCategorie().equals("Cadet") && currentCategorie.equals("Senior")) {
                    newEleve.setCategorie(currentCategorie);
                }
            }
        }
        return newEleve;
    }

    private String convertCategorie(String categorie) {
        if ("P".equalsIgnoreCase(categorie)) {
            return "Pupille";
        } else if ("B".equalsIgnoreCase(categorie)) {
            return "Benjamin";
        } else if ("M".equalsIgnoreCase(categorie)) {
            return "Minime";
        } else if ("C".equalsIgnoreCase(categorie)) {
            return "Cadet";
        } else if ("J".equalsIgnoreCase(categorie)) {
            return "Junior";
        } else if ("S".equalsIgnoreCase(categorie)) {
            return "Senior";
        } else if ("V".equalsIgnoreCase(categorie)) {
            return "Vétéran";
        }
        return "";
    }

    private String getCellValue(Row row, int cellId) {
        Cell cellB = row.getCell(cellId);
        CellValue cellValue = evaluator.evaluate(cellB);
        String value = "";
        if (cellValue != null) {
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    Double aDouble = cellB.getNumericCellValue();
                    value = String.valueOf(aDouble.intValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = cellB.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                case Cell.CELL_TYPE_ERROR:
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    break;
            }
        }

        return value;
    }

    private String extractCategorieCombat(EleveBean eleve, CompetitionBean competition) {
        String poids = eleve.getPoids();
        int poidsEleveInt = Integer.parseInt(poids);
        List<Epreuve> epreuvesCombat = new ArrayList<Epreuve>();
        for (CategorieBean categorie : competition.getCategories()) {
            for (EpreuveBean epreuve : categorie.getEpreuves()) {
                if (TypeEpreuve.COMBAT.getValue().equalsIgnoreCase(epreuve.getType())) {
                    String nomEpreuve = epreuve.getNom();
                    String newNomEpreuve = nomEpreuve.trim();
                    String minPoids = newNomEpreuve.substring(0, newNomEpreuve.indexOf("-"));
                    String maxPoids = newNomEpreuve.substring(newNomEpreuve.indexOf("-") + 1);
                    int intMinPoids = Integer.parseInt(minPoids);
                    int intMaxPoids = Integer.parseInt(maxPoids);

                    if (categorie.getNom().equals(eleve.getCategorie())) {
                        if (poidsEleveInt >=intMinPoids && poidsEleveInt < intMaxPoids ) {
                            return nomEpreuve;
                        }
                    }
                }
            }
        }
        return "";
    }

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

        for (CategorieBean categorieBean : competition.getCategories()) {
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
                for (EpreuveBean epreuveBean : categorieBean.getEpreuves()) {
                    if (epreuveBean.getType().equals(typeEpreuve) && EtatEpreuve.TERMINE.getValue().equals(epreuveBean.getEtat())) {
                        if (!isSheetCreated) {
                            sheet = workbook.createSheet(categorieBean.getType().concat(" - ").concat(categorieBean.getNom()));
                            isSheetCreated = true;
                        }
                        if (!isHeaderCreated) {
                            Row rowTypeEpreuve = getRow(sheet, rowCount++);
                            Cell cellTypeEpreuve = rowTypeEpreuve.createCell(colCount + 1);
                            cellTypeEpreuve.setCellStyle(styleTitle);
                            cellTypeEpreuve.setCellValue(typeEpreuve.toUpperCase());
                            Row rowTitle = getRow(sheet, rowCount++);
                            Cell cell1 = rowTitle.createCell(colCount + 1);
                            cell1.setCellValue("Place");
                            cell1.setCellStyle(styleTitle);
                            Cell cell2 = rowTitle.createCell(colCount + 2);
                            cell2.setCellValue("Nom");
                            cell2.setCellStyle(styleTitle);
                            Cell cell3 = rowTitle.createCell(colCount + 3);
                            cell3.setCellValue("Prénom");
                            cell3.setCellStyle(styleTitle);
                            isHeaderCreated = true;
                            sheet.addMergedRegion(new CellRangeAddress(rowTypeEpreuve.getRowNum(), rowTypeEpreuve.getRowNum(), colCount + 1, colCount + 3));
                        }


                        Row rowEpreuve = getRow(sheet, rowCount++);
                        Cell cellEpreuve = rowEpreuve.createCell(colCount + 1);
                        cellEpreuve.setCellStyle(styleTitle2);
                        cellEpreuve.setCellValue(epreuveBean.getNom());
                        for (int i = colCount + 2; i <= colCount + 3; i++) {
                            Cell tempCellClassementGene = rowEpreuve.createCell(i);
                            tempCellClassementGene.setCellStyle(styleTitle2);
                        }
                        sheet.addMergedRegion(new CellRangeAddress(rowEpreuve.getRowNum(), rowEpreuve.getRowNum(), colCount + 1, colCount + 3));

                        ObservableList<ParticipantBean> newList = FXCollections.observableArrayList();
                        //Get participant for 1, 2, 3, 4 place
                        for (ParticipantBean participantBean : epreuveBean.getParticipants()) {
                            if (participantBean.getClassementFinal() > 0 && participantBean.getClassementFinal() < 5) {
                                newList.add(participantBean);
                            }
                        }
                        for (ParticipantBean participantBean : newList) {
                            Row rowParticipant = getRow(sheet, rowCount++);
                            Cell cell1 = rowParticipant.createCell(colCount + 1);
                            cell1.setCellStyle(styleData);
                            cell1.setCellValue(participantBean.getClassementFinal());
                            Cell cell2 = rowParticipant.createCell(colCount + 2);
                            cell2.setCellStyle(styleData);
                            cell2.setCellValue(participantBean.getNom());
                            Cell cell3 = rowParticipant.createCell(colCount + 3);
                            cell3.setCellStyle(styleData);
                            cell3.setCellValue(participantBean.getPrenom());
                        }
                        for (int columnPosition = 0; columnPosition < 8; columnPosition++) {
                            sheet.autoSizeColumn(columnPosition);
                        }
                    }
                }
            }
        }

        //Classement des clubs
        XSSFSheet sheet = workbook.createSheet("Classement des clubs");
        int rowCount = 1;
        int colCount = 0;

        Row rowTitle = sheet.createRow(rowCount++);
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

        Row rowClassementTech = sheet.createRow(rowCount++);
        Cell cellClassementTech = rowClassementTech.createCell(colCount + 1);
        cellClassementTech.setCellStyle(styleTitle2);
        cellClassementTech.setCellValue("Classement Technique");
        for (int i = colCount + 2; i <= colCount + 6; i++) {
            Cell tempCellClassementGene = rowClassementTech.createCell(i);
            tempCellClassementGene.setCellStyle(styleTitle2);
        }
        sheet.addMergedRegion(new CellRangeAddress(rowCount - 1, rowCount - 1, colCount + 1, colCount + 6));

        SortedList<ClubBean> sortableList = new SortedList<ClubBean>(competition.getClubs());
        sortableList.setComparator(new ComparatorClubTotalTechnique());
        sortableList.sort();
        for (ClubBean clubBean : sortableList) {
            Row rowClub = sheet.createRow(rowCount++);
            createClassementClubTable(clubBean, rowClub, colCount, styleData, TypeEpreuve.TECHNIQUE);
        }

        Row rowClassementComb = sheet.createRow(rowCount++);
        Cell cellClassementComb = rowClassementComb.createCell(colCount + 1);
        cellClassementComb.setCellStyle(styleTitle2);
        cellClassementComb.setCellValue("Classement Combat");
        for (int i = colCount + 2; i <= colCount + 6; i++) {
            Cell tempCellClassementGene = rowClassementComb.createCell(i);
            tempCellClassementGene.setCellStyle(styleTitle2);
        }
        sheet.addMergedRegion(new CellRangeAddress(rowCount - 1, rowCount - 1, colCount + 1, colCount + 6));
        sortableList = new SortedList<ClubBean>(competition.getClubs());
        sortableList.setComparator(new ComparatorClubTotalCombat());
        sortableList.sort();
        for (ClubBean clubBean : sortableList) {
            Row rowClub = sheet.createRow(rowCount++);
            createClassementClubTable(clubBean, rowClub, colCount, styleData, TypeEpreuve.COMBAT);
        }

        Row rowClassementGene = sheet.createRow(rowCount++);
        Cell cellClassementGene = rowClassementGene.createCell(colCount + 1);
        cellClassementGene.setCellStyle(styleTitle2);
        cellClassementGene.setCellValue("Classement General");
        for (int i = colCount + 2; i <= colCount + 6; i++) {
            Cell tempCellClassementGene = rowClassementGene.createCell(i);
            tempCellClassementGene.setCellStyle(styleTitle2);
        }

        sheet.addMergedRegion(new CellRangeAddress(rowCount - 1, rowCount - 1, colCount + 1, colCount + 6));
        sortableList = new SortedList<ClubBean>(competition.getClubs());
        sortableList.sort();
        for (ClubBean clubBean : competition.getClubs()) {
            Row rowClub = sheet.createRow(rowCount++);
            createClassementClubTable(clubBean, rowClub, colCount, styleData, null);
        }

        for (int columnPosition = 0; columnPosition < 7; columnPosition++) {
            sheet.autoSizeColumn(columnPosition);
        }

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

    private Row getRow(XSSFSheet sheet, int rowCount) {
        Row row = sheet.getRow(rowCount);
        if (row == null) {
            row = sheet.createRow(rowCount);
        }
        return row;
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

    public boolean saveGlobalVisionFile(File file, CompetitionBean competition) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Vision Globale");
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

}
