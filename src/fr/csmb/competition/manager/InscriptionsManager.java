package fr.csmb.competition.manager;

import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.model.comparator.ComparatorClubTotalCombat;
import fr.csmb.competition.model.comparator.ComparatorClubTotalTechnique;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.xml.model.*;
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

    public void loadInscription(File file, Competition competition) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            XSSFSheet sheet = workbook.getSheet("Epreuves");

            Iterator<Row> rowIterator = sheet.rowIterator();

            Club club = new Club();
            while(rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell cellB = row.getCell(1);

                if (cellB!= null && cellB.getCellType() == Cell.CELL_TYPE_STRING &&
                        "N° Club".equals(cellB.getStringCellValue())) {
                    String identifiant = "";
                    Cell cellC = row.getCell(2);
                    if (cellC != null) {
                        switch (cellC.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                System.out.println(cellC.getStringCellValue());
                                identifiant = cellC.getStringCellValue();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                Double aDouble = cellC.getNumericCellValue();
                                identifiant = String.valueOf(aDouble.intValue());
                                System.out.println(identifiant);
                                break;
                        }
                    }
                    club.setIdentifiant(identifiant);
                } else if (cellB!= null && cellB.getCellType() == Cell.CELL_TYPE_STRING &&
                        "Nom du Club".equals(cellB.getStringCellValue())) {
                    String nom = "";
                    Cell cellC = row.getCell(2);
                    if (cellC != null) {
                        switch (cellC.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                System.out.println(cellC.getStringCellValue());
                                nom = cellC.getStringCellValue();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                Double aDouble = cellC.getNumericCellValue();
                                nom = String.valueOf(aDouble.intValue());
                                System.out.println(nom);
                                break;
                        }
                    }
                    club.setNomClub(nom);
                } else if (cellB!= null && cellB.getCellType() == Cell.CELL_TYPE_STRING &&
                        "Responsable".equals(cellB.getStringCellValue())) {
                    String responsable = "";
                    Cell cellC = row.getCell(2);
                    if (cellC != null) {
                        switch (cellC.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                System.out.println(cellC.getStringCellValue());
                                responsable = cellC.getStringCellValue();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                Double aDouble = cellC.getNumericCellValue();
                                responsable = String.valueOf(aDouble.intValue());
                                System.out.println(responsable);
                                break;
                        }
                    }
                    club.setResponsable(responsable);
                } else if (cellB!= null && cellB.getCellType() == Cell.CELL_TYPE_STRING &&
                        "Renseignements".equals(cellB.getStringCellValue())) {
                    rowIterator.next();
                    List<Eleve> eleves = new ArrayList<Eleve>();
                    Map<String, List<Eleve>> teamDoiLuyen = new HashMap<String, List<Eleve>>();
                    while(rowIterator.hasNext()) {
                        Eleve eleve = new Eleve();
                        row = rowIterator.next();
                        eleve.setLicenceEleve(getCellValue(row, 3));
                        eleve.setNomEleve(getCellValue(row, 4));
                        eleve.setPrenomEleve(getCellValue(row, 5));
                        eleve.setAgeEleve(getCellValue(row, 6));

                        eleve.setCategorieEleve(convertCategorie(getCellValue(row, 7)));
                        eleve.setSexeEleve(getCellValue(row, 8));
                        eleve.setPoidsEleve(getCellValue(row, 9));
                        List<String> epreuves = new ArrayList<String>();
                        String epreuve = getCellValue(row, 10);
                        if ("Oui".equalsIgnoreCase(epreuve)) {
                            epreuves.add("Main Nue");
                        }
                        epreuve = getCellValue(row, 11);
                        if ("Oui".equalsIgnoreCase(epreuve)) {
                            epreuves.add("Arme");
                        }
                        epreuve = getCellValue(row, 12);
                        if (epreuve.contains("Équipe")) {
                            if (teamDoiLuyen.get(epreuve) == null) {
                                teamDoiLuyen.put(epreuve, new ArrayList<Eleve>());
                            }
                            teamDoiLuyen.get(epreuve).add(eleve);
                        }
                        epreuve = getCellValue(row, 13);
                        if ("Oui".equalsIgnoreCase(epreuve)) {

                            epreuves.add(extractCategorieCombat(eleve, competition));
                        }
                        eleve.setEpreuvesEleves(epreuves);

                        if (!eleve.getNomEleve().equals("")) {
                            eleves.add(eleve);
                        }
                    }
                    club.setEleves(eleves);
                    for (List<Eleve> eleveList : teamDoiLuyen.values()) {
                        club.getEleves().add(extractEleveFromTeam(eleveList));
                    }

                    if (competition.getClubs().contains(club)) {
                        competition.getClubs().remove(club);
                    }
                    competition.getClubs().add(club);
                }
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Eleve extractEleveFromTeam(List<Eleve> eleves) {
        Eleve newEleve = new Eleve();
        for (Eleve eleve : eleves) {
            newEleve.setEpreuvesEleves(new ArrayList<String>());
            newEleve.getEpreuvesEleves().add("Doi Luyen");
            //Nom
            if (newEleve.getNomEleve() == null) {
                newEleve.setNomEleve(eleve.getNomEleve());
            } else {
                newEleve.setNomEleve(newEleve.getNomEleve().concat("/").concat(eleve.getNomEleve()));
            }
            //Prénom
            if (newEleve.getPrenomEleve() == null) {
                newEleve.setPrenomEleve(eleve.getPrenomEleve());
            } else {
                newEleve.setPrenomEleve(newEleve.getPrenomEleve().concat("/").concat(eleve.getPrenomEleve()));
            }
            //Age
            if (newEleve.getAgeEleve() == null) {
                newEleve.setAgeEleve(eleve.getAgeEleve());
            } else {
                if (Integer.parseInt(newEleve.getAgeEleve()) < Integer.parseInt(eleve.getAgeEleve())) {
                    newEleve.setAgeEleve(eleve.getAgeEleve());
                }
            }
            //Sexe
            if (newEleve.getSexeEleve() == null) {
                newEleve.setSexeEleve(eleve.getSexeEleve());
            } else {
                if (eleve.getSexeEleve().equals("Masculin")) {
                    newEleve.setSexeEleve(eleve.getSexeEleve());
                } else if (eleve.getSexeEleve().equals("Féminin") && newEleve.getSexeEleve().equals("Féminin")) {
                    newEleve.setSexeEleve(eleve.getSexeEleve());
                }
            }
            //Categorie
            String currentCategorie;
            if (eleve.getCategorieEleve().equals("Benjamin") || eleve.getCategorieEleve().equals("Minime") || eleve.getCategorieEleve().equals("Cadet")) {
                currentCategorie = "Cadet";
            } else {
                currentCategorie = "Senior";
            }
            if (newEleve.getCategorieEleve() == null) {
               newEleve.setCategorieEleve(currentCategorie);
            } else {
                if (newEleve.getCategorieEleve().equals("Cadet") && currentCategorie.equals("Senior")) {
                    newEleve.setCategorieEleve(currentCategorie);
                }
            }
        }
        return newEleve;
    }

    private String convertCategorie(String categorie) {
        if ("B".equalsIgnoreCase(categorie)) {
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

    private String extractCategorieCombat(Eleve eleve, Competition competition) {
        String poids = eleve.getPoidsEleve();
        int poidsEleveInt = Integer.parseInt(poids);
        List<Epreuve> epreuvesCombat = new ArrayList<Epreuve>();
        for (Categorie categorie : competition.getCategories()) {
            for (Epreuve epreuve : categorie.getEpreuves()) {
                if (TypeEpreuve.COMBAT.getValue().equalsIgnoreCase(epreuve.getTypeEpreuve())) {
                    String nomEpreuve = epreuve.getNomEpreuve();
                    String newNomEpreuve = nomEpreuve.trim();
                    String minPoids = newNomEpreuve.substring(0, newNomEpreuve.indexOf("-"));
                    String maxPoids = newNomEpreuve.substring(newNomEpreuve.indexOf("-") + 1);
                    int intMinPoids = Integer.parseInt(minPoids);
                    int intMaxPoids = Integer.parseInt(maxPoids);

                    if (categorie.getNomCategorie().equals(eleve.getCategorieEleve())) {
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
            int rowCount = 0;
            int colCount = 0;
            String[] typeEpreuves = new String[]{ TypeEpreuve.TECHNIQUE.getValue(), TypeEpreuve.COMBAT.getValue()};
            for (String typeEpreuve : typeEpreuves) {
                if (typeEpreuve.equals(TypeEpreuve.TECHNIQUE.getValue())) {
                    colCount = 0;
                } else {
                    colCount = 4;
                }
                for (EpreuveBean epreuveBean : categorieBean.getEpreuves()) {
                    if (epreuveBean.getType().equals(typeEpreuve) && EtatEpreuve.TERMINE.getValue().equals(epreuveBean.getEtat())) {
                        XSSFSheet sheet = null;
                        if (!isSheetCreated) {
                            sheet = workbook.createSheet(categorieBean.getType().concat(" - ").concat(categorieBean.getNom()));
                            Row rowTitle = sheet.createRow(rowCount++);
                            Cell cell1 = rowTitle.createCell(colCount + 1);
                            cell1.setCellValue("Place");
                            cell1.setCellStyle(styleTitle);
                            Cell cell2 = rowTitle.createCell(colCount + 2);
                            cell2.setCellValue("Nom");
                            cell2.setCellStyle(styleTitle);
                            Cell cell3 = rowTitle.createCell(colCount + 3);
                            cell3.setCellValue("Prénom");
                            cell3.setCellStyle(styleTitle);
                            isSheetCreated = true;
                        }

                        Row rowEpreuve = sheet.createRow(rowCount++);
                        Cell cellEpreuve = rowEpreuve.createCell(colCount + 1);
                        cellEpreuve.setCellStyle(styleTitle2);
                        cellEpreuve.setCellValue(epreuveBean.getNom());
                        for (int i = colCount + 2; i <= colCount + 3; i++) {
                            Cell tempCellClassementGene = rowEpreuve.createCell(i);
                            tempCellClassementGene.setCellStyle(styleTitle2);
                        }
                        sheet.addMergedRegion(new CellRangeAddress(1, 1, colCount + 1, colCount + 3));
                        for (ParticipantBean participantBean : epreuveBean.getParticipants()) {
                            Row rowParticipant = sheet.createRow(rowCount++);
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
                        for (int columnPosition = 0; columnPosition < 4; columnPosition++) {
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
            createClassementClubTable(clubBean, rowClub, colCount, styleData);
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
            createClassementClubTable(clubBean, rowClub, colCount, styleData);
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
            createClassementClubTable(clubBean, rowClub, colCount, styleData);
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

    private void createClassementClubTable(ClubBean clubBean, Row rowClub, int colCount, XSSFCellStyle styleData) {
        Cell cell1 = rowClub.createCell(colCount + 1);
        cell1.setCellValue(clubBean.getClassementTechnique());
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

    public void saveInscription(File file, Competition competition) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("export");

        int rowCount = 0;
        Row row1 = sheet.createRow(rowCount++);
        Cell cell1 = row1.createCell(1);
        cell1.setCellValue("Renseignements");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 4));
        XSSFCellStyle style1 = workbook.createCellStyle();
        style1.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        style1.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style1.setAlignment(CellStyle.ALIGN_CENTER);
        style1.setBorderBottom(CellStyle.BORDER_MEDIUM);
        style1.setBorderLeft(CellStyle.BORDER_MEDIUM);
        style1.setBorderRight(CellStyle.BORDER_MEDIUM);
        style1.setBorderTop(CellStyle.BORDER_MEDIUM);
        cell1.setCellStyle(style1);

        Row row2 = sheet.createRow(rowCount++);
        Cell cell21 = row2.createCell(1);
        cell21.setCellValue("N° Club");
        cell21.setCellStyle(style1);
        sheet.autoSizeColumn(1);

        Cell cell22 = row2.createCell(2);
        cell22.setCellValue("Nom du Club");
        cell22.setCellStyle(style1);
        sheet.autoSizeColumn(2);

        Cell cell23 = row2.createCell(3);
        cell23.setCellValue("Nom Eleve");
        cell23.setCellStyle(style1);
        sheet.autoSizeColumn(3);

        Cell cell24 = row2.createCell(4);
        cell24.setCellValue("Prénom Eleve");
        cell24.setCellStyle(style1);
        sheet.autoSizeColumn(4);

        for (Club club : competition.getClubs()) {
            for(Eleve eleve : club.getEleves()) {
                Row rowClub = sheet.createRow(rowCount++);
                Cell cellNumClub = rowClub.createCell(1);
                Cell cellNomClub = rowClub.createCell(2);
                Cell cellNomEleve = rowClub.createCell(3);
                Cell cellPrenomEleve = rowClub.createCell(4);

                cellNomClub.setCellValue(club.getNomClub());
                cellNumClub.setCellValue(club.getIdentifiant());
                cellNomEleve.setCellValue(eleve.getNomEleve());
                cellPrenomEleve.setCellValue(eleve.getPrenomEleve());
                sheet.autoSizeColumn(1);
                sheet.autoSizeColumn(2);
                sheet.autoSizeColumn(3);
                sheet.autoSizeColumn(4);
            }
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
