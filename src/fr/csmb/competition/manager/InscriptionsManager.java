package fr.csmb.competition.manager;

import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.xml.model.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
