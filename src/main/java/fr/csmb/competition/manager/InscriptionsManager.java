package fr.csmb.competition.manager;

import fr.csmb.competition.Helper.CategorieHelper;
import fr.csmb.competition.Helper.ExcelHelperStyle;
import fr.csmb.competition.component.grid.globalvision.GlobalVision;
import fr.csmb.competition.listener.EleveBeanPresenceChangePropertyListener;
import fr.csmb.competition.model.*;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.xml.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;

import java.io.*;
import java.util.*;

/**
 * Created by Administrateur on 13/10/14.
 */
public class InscriptionsManager {

    FormulaEvaluator evaluator;
    private static final Logger LOGGER = LogManager.getFormatterLogger(InscriptionsManager.class);

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
                if (cellB != null && cellB.getCellType() == Cell.CELL_TYPE_STRING &&
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
                        if (getCellValue(row, 3) == null ||
                                getCellValue(row, 3).equals("")) {
                            continue;
                        }
                        eleve.setLicence(getCellValue(row, 3));
                        eleve.setNom(getCellValue(row, 4));
                        eleve.setPrenom(getCellValue(row, 5));
                        String age = calculateAge(getCellValue(row, 6));
                        eleve.setAge(age);

                        eleve.setCategorie(CategorieHelper.getCategorieFromAge(age));
                        eleve.setSexe(getCellValue(row, 9));
                        eleve.setPoids(getCellValue(row, 10));
                        String epreuve = getCellValue(row, 11);
                        if ("Oui".equalsIgnoreCase(epreuve)) {
                            eleve.getEpreuves().add("Quyen Main Nue");
                        }
                        epreuve = getCellValue(row, 12);
                        if ("Oui".equalsIgnoreCase(epreuve)) {
                            eleve.getEpreuves().add("Quyen Arme");
                        }

//                        epreuve = getCellValue(row, 11);
//                        if (epreuve.contains("Équipe")) {
//                            if (teamSongLuyenMainNue.get(epreuve) == null) {
//                                teamSongLuyenMainNue.put(epreuve, new ArrayList<EleveBean>());
//                            }
//                            teamSongLuyenMainNue.get(epreuve).add(eleve);
//                        }
//                        epreuve = getCellValue(row, 14);
//                        if (epreuve.contains("Équipe")) {
//                            if (teamSongLuyenArmes.get(epreuve) == null) {
//                                teamSongLuyenArmes.put(epreuve, new ArrayList<EleveBean>());
//                            }
//                            teamSongLuyenArmes.get(epreuve).add(eleve);
//                        }
                        epreuve = getCellValue(row, 13);
                        if (epreuve.contains("Equipe")) {
                            if (teamDoiLuyenMainNue.get(epreuve) == null) {
                                teamDoiLuyenMainNue.put(epreuve, new ArrayList<EleveBean>());
                            }
                            teamDoiLuyenMainNue.get(epreuve).add(eleve);
                        }
//                        epreuve = getCellValue(row, 16);
//                        if (epreuve.contains("Équipe")) {
//                            if (teamDoiLuyenArmes.get(epreuve) == null) {
//                                teamDoiLuyenArmes.put(epreuve, new ArrayList<EleveBean>());
//                            }
//                            teamDoiLuyenArmes.get(epreuve).add(eleve);
//                        }
                        epreuve = getCellValue(row, 14);
                        if (epreuve.contains("Equipe")) {
                            if (teamSynchroMainNue.get(epreuve) == null) {
                                teamSynchroMainNue.put(epreuve, new ArrayList<EleveBean>());
                            }
                            teamSynchroMainNue.get(epreuve).add(eleve);
                        }
//                        epreuve = getCellValue(row, 18);
//                        if (epreuve.contains("Équipe")) {
//                            if (teamSynchroArmesCourtes.get(epreuve) == null) {
//                                teamSynchroArmesCourtes.put(epreuve, new ArrayList<EleveBean>());
//                            }
//                            teamSynchroArmesCourtes.get(epreuve).add(eleve);
//                        }
//                        epreuve = getCellValue(row, 19);
//                        if (epreuve.contains("Équipe")) {
//                            if (teamSynchroArmesLongues.get(epreuve) == null) {
//                                teamSynchroArmesLongues.put(epreuve, new ArrayList<EleveBean>());
//                            }
//                            teamSynchroArmesLongues.get(epreuve).add(eleve);
//                        }
                        epreuve = getCellValue(row, 15);
                        if ("Oui".equalsIgnoreCase(epreuve)) {

                            eleve.getEpreuves().add(CategorieHelper.extractCategorieCombat(eleve, competition));
                        }

                        if (!eleve.getNom().equals("")) {
                            clubBean.getEleves().add(eleve);
                            initializeListener(competition, eleve, clubBean);
                            eleve.setPresence(Boolean.TRUE);
                        }
                    }
//                    for (String equipe : teamSongLuyenMainNue.keySet()) {
//                        EleveBean newEleveBean = extractEleveFromTeam(
//                                clubBean.getIdentifiant(), equipe, teamSongLuyenMainNue.get(equipe), "Song Luyen Main Nue");
//                        initializeListener(competition, newEleveBean, clubBean);
//                        clubBean.getEleves().add(newEleveBean);
//                    }
//                    for (String equipe : teamSongLuyenArmes.keySet()) {
//                        EleveBean newEleveBean = extractEleveFromTeam(
//                                clubBean.getIdentifiant(), equipe, teamSongLuyenArmes.get(equipe), "Song Luyen Armes");
//                        initializeListener(competition, newEleveBean, clubBean);
//                        clubBean.getEleves().add(newEleveBean);
//                    }
                    for (String equipe : teamDoiLuyenMainNue.keySet()) {
                        EleveBean newEleveBean = extractEleveFromTeam(
                                clubBean.getIdentifiant(), equipe.concat("DL"), teamDoiLuyenMainNue.get(equipe), "Doi Luyen");
                        initializeListener(competition, newEleveBean, clubBean);
                        clubBean.getEleves().add(newEleveBean);
                        newEleveBean.setPresence(Boolean.TRUE);
                    }
//                    for (String equipe : teamDoiLuyenArmes.keySet()) {
//                        EleveBean newEleveBean = extractEleveFromTeam(
//                                clubBean.getIdentifiant(), equipe, teamDoiLuyenArmes.get(equipe), "Doi Luyen Armes");
//                        initializeListener(competition, newEleveBean, clubBean);
//                        clubBean.getEleves().add(newEleveBean);
//                    }
                    for (String equipe : teamSynchroMainNue.keySet()) {
                        EleveBean newEleveBean = extractEleveFromTeam(
                                clubBean.getIdentifiant(), equipe.concat("Sync"), teamSynchroMainNue.get(equipe), "Synchronisé");
                        initializeListener(competition, newEleveBean, clubBean);
                        clubBean.getEleves().add(newEleveBean);
                        newEleveBean.setPresence(Boolean.TRUE);
                    }
//                    for (String equipe : teamSynchroArmesCourtes.keySet()) {
//                        EleveBean newEleveBean = extractEleveFromTeam(
//                                clubBean.getIdentifiant(), equipe, teamSynchroArmesCourtes.get(equipe), "Quy Dinh Synchro Armes Courtes");
//                        initializeListener(competition, newEleveBean, clubBean);
//                        clubBean.getEleves().add(newEleveBean);
//                    }
//                    for (String equipe : teamSynchroArmesLongues.keySet()) {
//                        EleveBean newEleveBean = extractEleveFromTeam(
//                                clubBean.getIdentifiant(), equipe, teamSynchroArmesLongues.get(equipe), "Quy Dinh Synchro Armes Longues");
//                        initializeListener(competition, newEleveBean, clubBean);
//                        clubBean.getEleves().add(newEleveBean);
//                    }

                    if (competition.getClubs().contains(clubBean)) {
                        competition.getClubs().remove(clubBean);
                    }
                    competition.getClubs().add(clubBean);
                }
            }
            fileInputStream.close();
            LOGGER.debug("Fin de traitement du club %s", clubBean.getNom());
            return clubBean.getNom();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String calculateAge(String dateNaissance) {
        DateTime birthDay = DateTime.parse(dateNaissance, DateTimeFormat.forPattern("dd/MM/yyyy"));
        LocalDate birthDayDate = birthDay.toLocalDate();

        int currentYear = LocalDate.now().year().get();
        int currentMonth = LocalDate.now().monthOfYear().get();
        if (currentMonth < DateTimeConstants.SEPTEMBER) {
            currentYear--;
        }
        //get Begin date of season
        LocalDate seasonBeginDate = new LocalDate(currentYear, DateTimeConstants.DECEMBER, 31);
        Period agePeriod = new Period(birthDayDate, seasonBeginDate, PeriodType.years());

        return String.valueOf(agePeriod.getYears());
    }

    private void initializeListener(CompetitionBean competition, EleveBean eleveBean, ClubBean clubBean) {
        EleveBeanPresenceChangePropertyListener changeListener = new EleveBeanPresenceChangePropertyListener();
        changeListener.setClubBean(clubBean);
        changeListener.setCompetitionBean(competition);
        changeListener.setEleveBean(eleveBean);
        eleveBean.presenceProperty().addListener(changeListener);
    }

    private EleveBean extractEleveFromTeam(String clubId, String equipe, List<EleveBean> eleves, String epreuve) {
        LOGGER.debug("Debut extract Eleve From Team");
        EleveBean newEleve = new EleveBean();
        for (EleveBean eleve : eleves) {
            LOGGER.debug("traitement eleve nom : %s, prénom : %s", eleve.getNom(), eleve.getPrenom());
            if (newEleve.getLicence() == null) {
                LOGGER.debug("clubId : %s, équipe : %s", clubId, equipe);
                newEleve.setLicence(clubId.concat("-").concat(equipe));
            }
            LOGGER.debug("traitement eleve nom : %s, prénom : %s. Licence OK", eleve.getNom(), eleve.getPrenom());
            if (newEleve.getEpreuves().size() <= 0) {
                newEleve.getEpreuves().add(epreuve);
            }
            LOGGER.debug("traitement eleve nom : %s, prénom : %s. Epreuve OK", eleve.getNom(), eleve.getPrenom());
            //Nom
            if (newEleve.getNom() == null) {
                newEleve.setNom(eleve.getNom());
            } else {
                newEleve.setNom(newEleve.getNom().concat("/").concat(eleve.getNom()));
            }
            LOGGER.debug("traitement eleve nom : %s, prénom : %s. Nom OK", eleve.getNom(), eleve.getPrenom());
            //Prénom
            if (newEleve.getPrenom() == null) {
                newEleve.setPrenom(eleve.getPrenom());
            } else {
                newEleve.setPrenom(newEleve.getPrenom().concat("/").concat(eleve.getPrenom()));
            }
            LOGGER.debug("traitement eleve nom : %s, prénom : %s. Prénom OK", eleve.getNom(), eleve.getPrenom());
            //Age
            if (newEleve.getAge() == null) {
                newEleve.setAge(eleve.getAge());
            } else {
                if (Integer.parseInt(newEleve.getAge()) < Integer.parseInt(eleve.getAge())) {
                    newEleve.setAge(eleve.getAge());
                }
            }
            LOGGER.debug("traitement eleve nom : %s, prénom : %s. Age OK", eleve.getNom(), eleve.getPrenom());
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
            LOGGER.debug("traitement eleve nom : %s, prénom : %s. Sexe OK", eleve.getNom(), eleve.getPrenom());
            //Categorie
            LOGGER.debug("Traitement catégories");
            String currentCategorie;
            if (eleve.getCategorie().equals("Benjamins") || eleve.getCategorie().equals("Minimes") || eleve.getCategorie().equals("Cadets")) {
                currentCategorie = "Cadets";
            } else {
                currentCategorie = "Séniors";
            }
            if (newEleve.getCategorie() == null) {
               newEleve.setCategorie(currentCategorie);
            } else {
                if (newEleve.getCategorie().equals("Cadets") && currentCategorie.equals("Séniors")) {
                    newEleve.setCategorie(currentCategorie);
                }
            }
            LOGGER.debug("traitement eleve nom : %s, prénom : %s. Catégorie OK", eleve.getNom(), eleve.getPrenom());
        }
        LOGGER.debug("Fin extract Eleve From Team");
        return newEleve;
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
                    if (HSSFDateUtil.isCellDateFormatted(cellB)) {
                        LocalDate birthDayDate = new LocalDate(cellB.getDateCellValue());
                        value = birthDayDate.toString(DateTimeFormat.forPattern("dd/MM/yyyy"));
                    } else {
                        Double aDouble = cellB.getNumericCellValue();
                        value = String.valueOf(aDouble.intValue());
                    }
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

                if (epreuve.getCategorie().getNom().equals(eleve.getCategorie()) && epreuve.getCategorie().getSexe().equals(eleve.getSexe())) {
                    if (poidsEleveInt >=intMinPoids && poidsEleveInt < intMaxPoids ) {
                        return nomEpreuve;
                    }
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

        LOGGER.info("Error when generate global vision.");
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFCellStyle styleCellEpreuve = setStyleEpreuveForGlobalVision(workbook);
        XSSFCellStyle styleCellTitle = setStyleTitleForGlobalVision(workbook);
        XSSFCellStyle styleCellCategorie = setStyleCategorieForGlobalVision(workbook);
        XSSFCellStyle styleCellSexe = setStyleSexeForGlobalVision(workbook);
        XSSFCellStyle styleCellTotal = setStyleTotalForGlobalVision(workbook);
        XSSFCellStyle styleCellTotal1 = setStyleTotal1ForGlobalVision(workbook);
        for (TypeEpreuve typeEpreuve :testStructures.keySet()) {

            List<GlobalVision> visions = testStructures.get(typeEpreuve);
            XSSFSheet sheet = workbook.createSheet(typeEpreuve.getValue());
            int colCount = 0;
            for (GlobalVision globalVision : visions) {
                globalVision.sortTypeCategories();
                if (haveParticipantForEpreuve(globalVision)) {
                    int rowCount = 0;
                    Row rowEpreuve = getRow(sheet, 0);
                    Cell cellEpreuve = rowEpreuve.createCell(colCount);
                    cellEpreuve.setCellValue(globalVision.getDisciplineName());
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
            LOGGER.error("Error when generate global vision.", e);
            return false;
        }
    }

    public boolean saveFicheCompetition(File file, CompetitionBean competitionBean, TypeEpreuve typeEpreuve) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        for (EpreuveBean epreuveBean : competitionBean.getEpreuves()) {
            if (epreuveBean.getEtat() != null && !epreuveBean.getEtat().equals("") &&
                    !epreuveBean.getEtat().equals(EtatEpreuve.FUSION.getValue()) &&
                    competitionBean.getParticipantByEpreuve(epreuveBean).size() > 0) {
                String categorieSheet = epreuveBean.getCategorie().getNom().substring(0, 1);
                String categorie = epreuveBean.getCategorie().getNom();
                String sexeSheet = epreuveBean.getCategorie().getSexe().substring(0, 1);
                String sexe = epreuveBean.getCategorie().getSexe();
                String discipline = epreuveBean.getDiscipline().getNom();
                String title2 = epreuveBean.getLabel();

                if (epreuveBean.getDiscipline().getType().equals(typeEpreuve.getValue())) {
                    String sheetName = categorieSheet.concat(sexeSheet).concat("-").concat(discipline.trim());
                    String title1 = categorie.concat("-").concat(sexe);

                    fillCompetitionFiche(workbook, sheetName, title1, title2, competitionBean, epreuveBean);
                }
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

    private void fillCompetitionFiche(XSSFWorkbook workbook, String sheetName, String title1, String title2,
                                      CompetitionBean competitionBean, EpreuveBean epreuveBean) {
        XSSFCellStyle styleCellEpreuve = setStyleEpreuveForGlobalVision(workbook);
        XSSFCellStyle styleCellTitle = setStyleTitleForGlobalVision(workbook);
        XSSFCellStyle styleCellPairLine = setStylePairLineForCompetitionFiche(workbook);
        XSSFCellStyle styleCellImpairLine = setStyleImpairLineForCompetitionFiche(workbook);

        XSSFSheet sheet = workbook.createSheet(sheetName);

        int colCount = 0;
        int rowCount = 0;
        Row rowTitle = getRow(sheet, 0);
        Cell cellEpreuve = rowTitle.createCell(colCount);
        cellEpreuve.setCellValue(title1);
        cellEpreuve.setCellStyle(styleCellEpreuve);
        for (int i = colCount + 1; i <= colCount + 2; i++) {
            Cell cellEpreuveEmpty = rowTitle.createCell(i);
            cellEpreuveEmpty.setCellStyle(styleCellEpreuve);
        }
        sheet.addMergedRegion(new CellRangeAddress(rowTitle.getRowNum(), rowTitle.getRowNum(), colCount, colCount + 2));
        rowCount++;

        rowTitle = getRow(sheet, rowCount);
        cellEpreuve = rowTitle.createCell(colCount);
        cellEpreuve.setCellValue(title2);
        cellEpreuve.setCellStyle(styleCellEpreuve);
        for (int i = colCount + 1; i <= colCount + 2; i++) {
            Cell cellEpreuveEmpty = rowTitle.createCell(i);
            cellEpreuveEmpty.setCellStyle(styleCellEpreuve);
        }
        sheet.addMergedRegion(new CellRangeAddress(rowTitle.getRowNum(), rowTitle.getRowNum(), colCount, colCount + 2));
        rowCount++;

        Row rowHeader = getRow(sheet, rowCount);
        Cell cellHeaderNomPrenom = rowHeader.createCell(colCount);
        cellHeaderNomPrenom.setCellValue("Nom Prénom");
        cellHeaderNomPrenom.setCellStyle(styleCellTitle);
        Cell cellHeaderClub = rowHeader.createCell(colCount + 1);
        cellHeaderClub.setCellValue("Club");
        cellHeaderClub.setCellStyle(styleCellTitle);
        Cell cellHeaderTotal = rowHeader.createCell(colCount + 2);
        cellHeaderTotal.setCellValue("Poids");
        cellHeaderTotal.setCellStyle(styleCellTitle);

        rowCount++;
        //Body
        for (ParticipantBean participantBean : competitionBean.getParticipantByEpreuve(epreuveBean)) {
            XSSFCellStyle currentCellStyle = styleCellImpairLine;
            if (rowCount%2 == 0) {
                currentCellStyle = styleCellPairLine;
            }
            Row rowPart = getRow(sheet, rowCount);
            Cell cellPartNomPrenom = rowPart.createCell(colCount);
            cellPartNomPrenom.setCellValue(participantBean.getNom().concat(" ").concat(participantBean.getPrenom()));
            cellPartNomPrenom.setCellStyle(currentCellStyle);
            Cell cellPartClub = rowPart.createCell(colCount + 1);
            String nomClub = competitionBean.getClubByIdentifiant(participantBean.getClub()).getNom();
            cellPartClub.setCellValue(nomClub);
            cellPartClub.setCellStyle(currentCellStyle);
            Cell cellPartPoids = rowPart.createCell(colCount + 2);
            cellPartPoids.setCellValue(participantBean.getPoids());
            cellPartPoids.setCellStyle(currentCellStyle);
            rowCount++;
        }

        sheet.getPrintSetup().setLandscape(true);
        sheet.setColumnWidth(0,10000);
        sheet.setColumnWidth(1,10000);
        sheet.setColumnWidth(2,5000);
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
        Iterator<String> iterator = structure.getTypeCategories().keySet().iterator();
        while (iterator.hasNext()) {
            String keyCategorie = iterator.next();
            for (String key : structure.getTypeCategories().get(keyCategorie).keySet()) {
                if (structure.getTypeCategories().get(keyCategorie).get(key).size() > 0) {
                    return true;
                }
            }
        }
//        for (String keyCategorie : structure.getTypeCategories().keySet()) {
//            for (String key : structure.getTypeCategories().get(keyCategorie).keySet()) {
//                if (structure.getTypeCategories().get(keyCategorie).get(key).size() > 0) {
//                    return true;
//                }
//            }
//        }

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
        XSSFCellStyle styleCellEpreuve = ExcelHelperStyle.getStyleCellEpreuve(workbook);
        return styleCellEpreuve;
    }

    private XSSFCellStyle setStyleTitleForGlobalVision(XSSFWorkbook workbook) {
        XSSFCellStyle styleCellTitle = ExcelHelperStyle.getStyleCellHeaderRow(workbook);
        return styleCellTitle;
    }

    private XSSFCellStyle setStylePairLineForCompetitionFiche(XSSFWorkbook workbook) {
        XSSFCellStyle styleCellTitle = ExcelHelperStyle.getStyleCellPairLine(workbook);
        return styleCellTitle;
    }

    private XSSFCellStyle setStyleImpairLineForCompetitionFiche(XSSFWorkbook workbook) {
        XSSFCellStyle styleCellTitle = ExcelHelperStyle.getStyleCellImpairLine(workbook);
        return styleCellTitle;
    }

    private XSSFCellStyle setStyleCategorieForGlobalVision(XSSFWorkbook workbook) {
        XSSFCellStyle styleCellCategorie = ExcelHelperStyle.getStyleCellCategorie(workbook);
        return styleCellCategorie;
    }

    private XSSFCellStyle setStyleSexeForGlobalVision(XSSFWorkbook workbook) {
        XSSFCellStyle styleCellSexe = ExcelHelperStyle.getStyleCellSexe(workbook);
        return styleCellSexe;
    }

    private XSSFCellStyle setStyleTotalForGlobalVision(XSSFWorkbook workbook) {
        XSSFCellStyle styleCellTotal = ExcelHelperStyle.getStyleCellTotal(workbook);
        return styleCellTotal;
    }

    private XSSFCellStyle setStyleTotal1ForGlobalVision(XSSFWorkbook workbook) {
        XSSFCellStyle styleCellTotal = ExcelHelperStyle.getStyleCellTotal1(workbook);
        return styleCellTotal;
    }
}
