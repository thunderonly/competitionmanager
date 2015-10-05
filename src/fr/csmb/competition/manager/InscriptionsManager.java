package fr.csmb.competition.manager;

import fr.csmb.competition.component.grid.globalvision.GlobalVision;
import fr.csmb.competition.model.*;
import fr.csmb.competition.model.comparator.EpreuveCombatComparator;
import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.xml.model.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.awt.Color;
import java.io.*;
import java.util.*;

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
                    Map<String, List<EleveBean>> teamSongLuyenMainNue = new HashMap<String, List<EleveBean>>();
                    Map<String, List<EleveBean>> teamSongLuyenArmes = new HashMap<String, List<EleveBean>>();
                    Map<String, List<EleveBean>> teamDoiLuyenMainNue = new HashMap<String, List<EleveBean>>();
                    Map<String, List<EleveBean>> teamDoiLuyenArmes = new HashMap<String, List<EleveBean>>();
                    Map<String, List<EleveBean>> teamSynchroMainNue = new HashMap<String, List<EleveBean>>();
                    Map<String, List<EleveBean>> teamSynchroArmesCourtes = new HashMap<String, List<EleveBean>>();
                    Map<String, List<EleveBean>> teamSynchroArmesLongues = new HashMap<String, List<EleveBean>>();
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
                            eleve.getEpreuves().add("Quy Dinh Main Nue");
                        }
                        epreuve = getCellValue(row, 11);
                        if ("Oui".equalsIgnoreCase(epreuve)) {
                            eleve.getEpreuves().add("Quy Dinh Armes Courtes");
                        }
                        epreuve = getCellValue(row, 12);
                        if ("Oui".equalsIgnoreCase(epreuve)) {
                            eleve.getEpreuves().add("Quy Dinh Armes Longues");
                        }
                        epreuve = getCellValue(row, 13);
                        if (epreuve.contains("Équipe")) {
                            if (teamSongLuyenMainNue.get(epreuve) == null) {
                                teamSongLuyenMainNue.put(epreuve, new ArrayList<EleveBean>());
                            }
                            teamSongLuyenMainNue.get(epreuve).add(eleve);
                        }
                        epreuve = getCellValue(row, 14);
                        if (epreuve.contains("Équipe")) {
                            if (teamSongLuyenArmes.get(epreuve) == null) {
                                teamSongLuyenArmes.put(epreuve, new ArrayList<EleveBean>());
                            }
                            teamSongLuyenArmes.get(epreuve).add(eleve);
                        }
                        epreuve = getCellValue(row, 15);
                        if (epreuve.contains("Équipe")) {
                            if (teamDoiLuyenMainNue.get(epreuve) == null) {
                                teamDoiLuyenMainNue.put(epreuve, new ArrayList<EleveBean>());
                            }
                            teamDoiLuyenMainNue.get(epreuve).add(eleve);
                        }
                        epreuve = getCellValue(row, 16);
                        if (epreuve.contains("Équipe")) {
                            if (teamDoiLuyenArmes.get(epreuve) == null) {
                                teamDoiLuyenArmes.put(epreuve, new ArrayList<EleveBean>());
                            }
                            teamDoiLuyenArmes.get(epreuve).add(eleve);
                        }
                        epreuve = getCellValue(row, 17);
                        if (epreuve.contains("Équipe")) {
                            if (teamSynchroMainNue.get(epreuve) == null) {
                                teamSynchroMainNue.put(epreuve, new ArrayList<EleveBean>());
                            }
                            teamSynchroMainNue.get(epreuve).add(eleve);
                        }
                        epreuve = getCellValue(row, 18);
                        if (epreuve.contains("Équipe")) {
                            if (teamSynchroArmesCourtes.get(epreuve) == null) {
                                teamSynchroArmesCourtes.put(epreuve, new ArrayList<EleveBean>());
                            }
                            teamSynchroArmesCourtes.get(epreuve).add(eleve);
                        }
                        epreuve = getCellValue(row, 19);
                        if (epreuve.contains("Équipe")) {
                            if (teamSynchroArmesLongues.get(epreuve) == null) {
                                teamSynchroArmesLongues.put(epreuve, new ArrayList<EleveBean>());
                            }
                            teamSynchroArmesLongues.get(epreuve).add(eleve);
                        }
                        epreuve = getCellValue(row, 20);
                        if ("Oui".equalsIgnoreCase(epreuve)) {

                            eleve.getEpreuves().add(extractCategorieCombat2(eleve, competition));
                        }

                        if (!eleve.getNom().equals("")) {
                            clubBean.getEleves().add(eleve);
                            initializeListener(competition, eleve, clubBean);
                        }
                    }
                    for (String equipe : teamSongLuyenMainNue.keySet()) {
                        EleveBean newEleveBean = extractEleveFromTeam(
                                clubBean.getIdentifiant(), equipe, teamSongLuyenMainNue.get(equipe), "Song Luyen Main Nue");
                        initializeListener(competition, newEleveBean, clubBean);
                        clubBean.getEleves().add(newEleveBean);
                    }
                    for (String equipe : teamSongLuyenArmes.keySet()) {
                        EleveBean newEleveBean = extractEleveFromTeam(
                                clubBean.getIdentifiant(), equipe, teamSongLuyenArmes.get(equipe), "Song Luyen Armes");
                        initializeListener(competition, newEleveBean, clubBean);
                        clubBean.getEleves().add(newEleveBean);
                    }
                    for (String equipe : teamDoiLuyenMainNue.keySet()) {
                        EleveBean newEleveBean = extractEleveFromTeam(
                                clubBean.getIdentifiant(), equipe, teamDoiLuyenMainNue.get(equipe), "Doi Luyen Main Nue");
                        initializeListener(competition, newEleveBean, clubBean);
                        clubBean.getEleves().add(newEleveBean);
                    }
                    for (String equipe : teamDoiLuyenArmes.keySet()) {
                        EleveBean newEleveBean = extractEleveFromTeam(
                                clubBean.getIdentifiant(), equipe, teamDoiLuyenArmes.get(equipe), "Doi Luyen Armes");
                        initializeListener(competition, newEleveBean, clubBean);
                        clubBean.getEleves().add(newEleveBean);
                    }
                    for (String equipe : teamSynchroMainNue.keySet()) {
                        EleveBean newEleveBean = extractEleveFromTeam(
                                clubBean.getIdentifiant(), equipe, teamSynchroMainNue.get(equipe), "Quy Dinh Synchro Main Nue");
                        initializeListener(competition, newEleveBean, clubBean);
                        clubBean.getEleves().add(newEleveBean);
                    }
                    for (String equipe : teamSynchroArmesCourtes.keySet()) {
                        EleveBean newEleveBean = extractEleveFromTeam(
                                clubBean.getIdentifiant(), equipe, teamSynchroArmesCourtes.get(equipe), "Quy Dinh Synchro Armes Courtes");
                        initializeListener(competition, newEleveBean, clubBean);
                        clubBean.getEleves().add(newEleveBean);
                    }
                    for (String equipe : teamSynchroArmesLongues.keySet()) {
                        EleveBean newEleveBean = extractEleveFromTeam(
                                clubBean.getIdentifiant(), equipe, teamSynchroArmesLongues.get(equipe), "Quy Dinh Synchro Armes Longues");
                        initializeListener(competition, newEleveBean, clubBean);
                        clubBean.getEleves().add(newEleveBean);
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

    private void initializeListener(CompetitionBean competition, EleveBean eleveBean, ClubBean clubBean) {
        EleveBeanPresenceChangePropertyListener changeListener = new EleveBeanPresenceChangePropertyListener();
        changeListener.setClubBean(clubBean);
        changeListener.setCompetitionBean(competition);
        changeListener.setEleveBean(eleveBean);
        eleveBean.presenceProperty().addListener(changeListener);
    }

    private EleveBean extractEleveFromTeam(String clubId, String equipe, List<EleveBean> eleves, String epreuve) {
        EleveBean newEleve = new EleveBean();
        for (EleveBean eleve : eleves) {
            if (newEleve.getLicence() == null) {
                newEleve.setLicence(clubId.concat("-").concat(equipe));
            }

            if (newEleve.getEpreuves().size() <= 0) {
                newEleve.getEpreuves().add(epreuve);
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
                currentCategorie = "Sénior";
            }
            if (newEleve.getCategorie() == null) {
               newEleve.setCategorie(currentCategorie);
            } else {
                if (newEleve.getCategorie().equals("Cadet") && currentCategorie.equals("Sénior")) {
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
            return "Sénior";
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
        for (EpreuveBean epreuve : competition.getEpreuves()) {
            if (TypeEpreuve.COMBAT.getValue().equalsIgnoreCase(epreuve.getDiscipline().getType())) {
                String nomEpreuve = epreuve.getDiscipline().getNom();
                String newNomEpreuve = nomEpreuve.trim();
                String minPoids = newNomEpreuve.substring(0, newNomEpreuve.indexOf("-"));
                String maxPoids = newNomEpreuve.substring(newNomEpreuve.indexOf("-") + 1);
                int intMinPoids = Integer.parseInt(minPoids);
                int intMaxPoids = Integer.parseInt(maxPoids);

                if (epreuve.getCategorie().getNom().equals(eleve.getCategorie()) && epreuve.getCategorie().getType().equals(eleve.getSexe())) {
                    if (poidsEleveInt >=intMinPoids && poidsEleveInt < intMaxPoids ) {
                        return nomEpreuve;
                    }
                }
            }
        }
        return "";
    }

    private String extractCategorieCombat2(EleveBean eleve, CompetitionBean competition) {
        String categorieEleve = eleve.getCategorie();
        String sexeEleve = eleve.getSexe();
        String poidsEleveStr = eleve.getPoids();
        Integer poidsEleve = new Integer(0);
        if (poidsEleveStr != null && !poidsEleveStr.isEmpty()) {
            poidsEleve = Integer.parseInt(poidsEleveStr);
        }

        Map<Integer, EpreuveBean> mapEpreuves = new HashMap<Integer, EpreuveBean>();
        EpreuveCombatComparator comparator = new EpreuveCombatComparator(mapEpreuves);
        TreeMap<Integer, EpreuveBean> epreuveBeanTreeMap = new TreeMap<Integer, EpreuveBean>(comparator);

        //recup tous les poids de la categorie de l eleve
        for (EpreuveBean epreuveBean : competition.getEpreuves()) {
            if (TypeEpreuve.COMBAT.getValue().equalsIgnoreCase(epreuveBean.getDiscipline().getType())) {
                if (epreuveBean.getCategorie().getNom().equals(categorieEleve) &&
                        epreuveBean.getCategorie().getType().equals(sexeEleve)) {
                    Integer poidsEpreuve = Integer.parseInt(epreuveBean.getDiscipline().getNom());
                    mapEpreuves.put(poidsEpreuve, epreuveBean);
                }
            }
        }
        //tri par poids
        epreuveBeanTreeMap.putAll(mapEpreuves);

        //comparaison poids eleve avec valeur abs du poids de l epreuve
        Iterator<Integer> iterator = epreuveBeanTreeMap.keySet().iterator();
        while (iterator.hasNext()) {
            Integer poidsEpreuve = iterator.next();
            if (poidsEleve <= Math.abs(poidsEpreuve)) {
                return mapEpreuves.get(poidsEpreuve).getDiscipline().getNom();
            } else {
                if (!iterator.hasNext()) {
                    //c'est le dernier donc plus lourd
                    return mapEpreuves.get(poidsEpreuve).getDiscipline().getNom();
                }
            }
        }

        return "";
    }

    private Row getRow(XSSFSheet sheet, int rowCount) {
        Row row = sheet.getRow(rowCount);
        if (row == null) {
            row = sheet.createRow(rowCount);
        }
        return row;
    }

    public boolean saveGlobalVisionFile(File file, Map<TypeEpreuve, List<GlobalVision>> testStructures) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFFont fontCellGeneral= workbook.createFont();
        fontCellGeneral.setFontHeightInPoints((short) 12);
        fontCellGeneral.setFontName("Arial");
        fontCellGeneral.setColor(IndexedColors.BLACK.getIndex());
        fontCellGeneral.setBold(false);
        fontCellGeneral.setItalic(false);

        XSSFCellStyle styleCellEpreuve = setStyleEpreuveForGlobalVision(workbook);
        XSSFCellStyle styleCellTitle = setStyleTitleForGlobalVision(workbook);
        XSSFCellStyle styleCellCategorie = setStyleCategorieForGlobalVision(workbook, fontCellGeneral);
        XSSFCellStyle styleCellSexe = setStyleSexeForGlobalVision(workbook, fontCellGeneral);
        XSSFCellStyle styleCellTotal = setStyleTotalForGlobalVision(workbook);
        XSSFCellStyle styleCellTotal1 = setStyleTotal1ForGlobalVision(workbook, fontCellGeneral);
        for (TypeEpreuve typeEpreuve :testStructures.keySet()) {

            List<GlobalVision> visions = testStructures.get(typeEpreuve);
            XSSFSheet sheet = workbook.createSheet(typeEpreuve.getValue());
            int colCount = 0;
            for (GlobalVision globalVision : visions) {
                if (haveParticipantForEpreuve(globalVision)) {
                    int rowCount = 0;
                    Row rowEpreuve = getRow(sheet, 0);
                    Cell cellEpreuve = rowEpreuve.createCell(colCount);
                    cellEpreuve.setCellValue(globalVision.getNomCategorie());
                    cellEpreuve.setCellStyle(styleCellEpreuve);
                    for (int i = colCount + 1; i <= colCount + 4; i++) {
                        Cell cellEpreuveEmpty = rowEpreuve.createCell(i);
                        cellEpreuveEmpty.setCellStyle(styleCellEpreuve);
                    }
                    sheet.addMergedRegion(new CellRangeAddress(rowEpreuve.getRowNum(), rowEpreuve.getRowNum(), colCount, colCount + 4));
                    rowCount++;
                    Row rowHeader = getRow(sheet, rowCount);
                    createHeader(rowHeader, colCount, styleCellTitle);
                    rowCount++;
                    createBody(sheet, styleCellCategorie, styleCellSexe, styleCellTotal, styleCellTotal1, colCount, rowCount, globalVision.getTypeCategories());

                    colCount += 5;
                }
            }

            for (int columnPosition = 0; columnPosition < colCount; columnPosition++) {
                sheet.autoSizeColumn(columnPosition);
            }
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

    private void createHeader(Row row, int colCount, XSSFCellStyle styleCellTitle) {
        Cell cellHeaderCat = row.createCell(colCount);
        cellHeaderCat.setCellValue("Cat.");
        cellHeaderCat.setCellStyle(styleCellTitle);
        Cell cellHeaderSexe = row.createCell(colCount+1);
        cellHeaderSexe.setCellValue("Sexe");
        cellHeaderSexe.setCellStyle(styleCellTitle);
        Cell cellHeaderNomPrenom = row.createCell(colCount+2);
        cellHeaderNomPrenom.setCellValue("Nom Prénom");
        cellHeaderNomPrenom.setCellStyle(styleCellTitle);
        Cell cellHeaderClub = row.createCell(colCount + 3);
        cellHeaderClub.setCellValue("Club");
        cellHeaderClub.setCellStyle(styleCellTitle);
        Cell cellHeaderTotal = row.createCell(colCount + 4);
        cellHeaderTotal.setCellValue("Poids");
        cellHeaderTotal.setCellStyle(styleCellTitle);
    }

    private void createBody(XSSFSheet sheet, XSSFCellStyle styleCellCategorie, XSSFCellStyle styleCellSexe, XSSFCellStyle styleCellTotal, XSSFCellStyle styleCellTotal1, int startCol, int rowStart, Map<String, Map<String, List<Participant>>> datas) {
        int rowStartForCategorie = rowStart;
        for (String keyCategorie : datas.keySet()) {
            if (haveParticipantForCategorie(keyCategorie, datas)) {
                Row rowCategorie = getRow(sheet, rowStartForCategorie);
                Cell cellCategorie = rowCategorie.createCell(startCol);
                cellCategorie.setCellValue(keyCategorie);
                cellCategorie.setCellStyle(styleCellCategorie);
                int rowStartForSexe = rowStartForCategorie;
                int rowStartForData = rowStartForCategorie;
                int totalRow = 0; //With total row
                for (String key : datas.get(keyCategorie).keySet()) {
                    int totalParticipant = datas.get(keyCategorie).get(key).size(); //with total row
                    if (totalParticipant > 0) {
                        Row rowSexe = getRow(sheet, rowStartForSexe);
                        Cell cellSexe = rowSexe.createCell(startCol + 1);
                        cellSexe.setCellValue(key);
                        cellSexe.setCellStyle(styleCellSexe);

                        for (Participant participant : datas.get(keyCategorie).get(key)) {
                            Row rowParticipant = getRow(sheet, rowStartForData);
                            Cell cellNomPrenom = rowParticipant.createCell(startCol + 2);
                            cellNomPrenom.setCellValue(participant.getNomParticipant().concat(" ").concat(participant.getPrenomParticipant()));
                            Cell cellClub = rowParticipant.createCell(startCol + 3);
                            cellClub.setCellValue(participant.getClubParticipant());
                            Cell cellPoids = rowParticipant.createCell(startCol + 4);
                            cellPoids.setCellValue(participant.getPoidsParticipant());
                            cellPoids.setCellStyle(styleCellTotal1);
                            rowStartForData++;
                        }

                        Row rowTotal = getRow(sheet, rowStartForData);
                        Cell cellTotal = rowTotal.createCell(startCol + 1);
                        cellTotal.setCellValue("Total ".concat(key));
                        cellTotal.setCellStyle(styleCellTotal);
                        for (int i = startCol + 2; i <= startCol + 3; i++) {
                            Cell cellTotalEmpty = rowTotal.createCell(i);
                            cellTotalEmpty.setCellStyle(styleCellTotal);
                        }
                        sheet.addMergedRegion(new CellRangeAddress(rowTotal.getRowNum(), rowTotal.getRowNum(), startCol + 1, startCol + 3));
                        Cell cellTotalValue = rowTotal.createCell(startCol + 4);
                        cellTotalValue.setCellValue(totalParticipant);
                        cellTotalValue.setCellStyle(styleCellTotal);
                        rowStartForData++;

                        Row rowSexeEmpty = null;
                        for (int i = rowStartForSexe + 1; i < rowStartForSexe + totalParticipant; i++) {
                            rowSexeEmpty = getRow(sheet, i);
                            Cell cellSexeEmpty = rowSexeEmpty.createCell(startCol + 1);
                            cellSexeEmpty.setCellStyle(styleCellSexe);
                        }
                        if (rowSexeEmpty != null) {
                            sheet.addMergedRegion(new CellRangeAddress(rowSexe.getRowNum(), rowSexeEmpty.getRowNum(), startCol + 1, startCol + 1));
                        }

                        rowStartForSexe = rowStartForData;
                        totalRow = rowStartForData;
                    }
                }
                if (totalRow > 0) {
                    Row rowEpreuveEmpty = null;
                    for (int i = rowStartForCategorie + 1; i < totalRow; i++) {
                        rowEpreuveEmpty = getRow(sheet, i);
                        Cell cellEpreuveEmpty = rowEpreuveEmpty.createCell(startCol);
                        cellEpreuveEmpty.setCellStyle(styleCellCategorie);
                    }
                    if (rowEpreuveEmpty != null) {
                        sheet.addMergedRegion(new CellRangeAddress(rowCategorie.getRowNum(), rowEpreuveEmpty.getRowNum(), startCol, startCol));
                    }
                    rowStartForCategorie = totalRow;
                }
            }
        }
    }

    private boolean haveParticipantForEpreuve(GlobalVision structure) {
        boolean result = false;
        for (String keyCategorie : structure.getTypeCategories().keySet()) {
            for (String key : structure.getTypeCategories().get(keyCategorie).keySet()) {
                if (structure.getTypeCategories().get(keyCategorie).get(key).size() > 0) {
                    return true;
                }
            }
        }

        return result;
    }

    private boolean haveParticipantForCategorie(String keyCategorie, Map<String, Map<String, List<Participant>>> datas) {
        boolean result = false;
        for (String key : datas.get(keyCategorie).keySet()) {
            if (datas.get(keyCategorie).get(key).size() > 0) {
                return true;
            }
        }

        return result;
    }

    private XSSFCellStyle setStyleEpreuveForGlobalVision(XSSFWorkbook workbook) {
        XSSFFont fontCellEpreuve= workbook.createFont();
        fontCellEpreuve.setFontHeightInPoints((short) 20);
        fontCellEpreuve.setFontName("Arial");
        fontCellEpreuve.setColor(IndexedColors.BLACK.getIndex());
        fontCellEpreuve.setBold(true);
        fontCellEpreuve.setItalic(false);

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

    private XSSFCellStyle setStyleTitleForGlobalVision(XSSFWorkbook workbook) {
        XSSFFont fontCellTitle= workbook.createFont();
        fontCellTitle.setFontHeightInPoints((short) 12);
        fontCellTitle.setFontName("Arial");
        fontCellTitle.setColor(IndexedColors.WHITE.getIndex());
        fontCellTitle.setBold(true);
        fontCellTitle.setItalic(false);

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

    private XSSFCellStyle setStyleCategorieForGlobalVision(XSSFWorkbook workbook, XSSFFont font) {
        XSSFCellStyle styleCellCategorie = workbook.createCellStyle();
        styleCellCategorie.setFont(font);
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

    private XSSFCellStyle setStyleSexeForGlobalVision(XSSFWorkbook workbook, XSSFFont font) {
        XSSFCellStyle styleCellSexe = workbook.createCellStyle();
        styleCellSexe.setFont(font);
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

    private XSSFCellStyle setStyleTotalForGlobalVision(XSSFWorkbook workbook) {
        XSSFFont fontCellTotal= workbook.createFont();
        fontCellTotal.setFontHeightInPoints((short) 12);
        fontCellTotal.setFontName("Arial");
        fontCellTotal.setColor(IndexedColors.BLACK.getIndex());
        fontCellTotal.setBold(true);
        fontCellTotal.setItalic(false);

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

    private XSSFCellStyle setStyleTotal1ForGlobalVision(XSSFWorkbook workbook, XSSFFont font) {

        XSSFCellStyle styleCellTotal = workbook.createCellStyle();
        styleCellTotal.setFont(font);
        styleCellTotal.setAlignment(CellStyle.ALIGN_CENTER);
        styleCellTotal.setBorderRight(CellStyle.BORDER_MEDIUM);
        return styleCellTotal;
    }
}
