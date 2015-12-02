package fr.csmb.competition.controller;

import fr.csmb.competition.Helper.ParticipantConverter;
import fr.csmb.competition.Main;
import fr.csmb.competition.model.ParticipantBean;
import fr.csmb.competition.component.grid.globalvision.GlobalVision;
import fr.csmb.competition.manager.InscriptionsManager;
import fr.csmb.competition.manager.ResultatsManager;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.DisciplineBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.model.comparator.DisciplineCombatComparator;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.view.NotificationView;
import fr.csmb.competition.xml.model.Participant;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.*;

/**
 * Created by Administrateur on 13/10/14.
 */
public class Controller {

    private Main main;
    private static final Logger LOGGER = LogManager.getFormatterLogger(Controller.class);

    @FXML
    private MenuItem createCompetition;
    @FXML
    private MenuItem createCategorie;
    @FXML
    private MenuItem createEpreuve;
    private CompetitionBean competitionBean;

    public void setMain(Main main) {
        this.main = main;
    }

    public void setCompetitionBean(CompetitionBean competitionBean) {
        this.competitionBean = competitionBean;
    }

    @FXML
    private void handleCreateCompetition() {
        main.showCreationCompetitionView();
    }

    @FXML
    private void handleCreateCategorie() {
        main.showCreationCategorieView();
    }

    @FXML
    private void handleCreateEpreuve() {
        main.showCreationEpreuveView();
    }

    @FXML
    private void handleManage() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(main.getMainStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            main.loadCompetitionToXmlFile(file);
        }
    }


    @FXML
    private void handleLoadInscription() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(main.getMainStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xlsx")) {
                file = new File(file.getPath() + ".xlsx");
            }
            main.loadInscriptionFile(file);
        }
    }

    @FXML
    private void handleSave() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(main.getMainStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            main.saveCompetitionToXmlFile(file);
        }
    }

    @FXML
    private void generateResultat() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(main.getMainStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xlsx")) {
                file = new File(file.getPath() + ".xlsx");
            }
            ResultatsManager resultatsManager = new ResultatsManager();
            boolean isSaved = resultatsManager.saveResultatFile(file, competitionBean);
            if (isSaved) {
                NotificationView notificationView = new NotificationView(main.getMainStage());
                notificationView.notify(NotificationView.Level.SUCCESS, "Génération",
                        "Le fichier de résultat : " + file.getName() + " a été correctement généré.");
            } else {
                NotificationView notificationView = new NotificationView(main.getMainStage());
                notificationView.notify(NotificationView.Level.SUCCESS, "Génération",
                        "Erreur lors de la génération du fichier de résultat : " + file.getName() + ".");
            }
        }
    }

    @FXML
    private void generateGlobalVision() {
        LOGGER.info("Try to generation global vision : ");
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(main.getMainStage());
        LOGGER.info("Try to generation global vision : " + file);
        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xlsx")) {
                file = new File(file.getPath() + ".xlsx");
            }

            List<GlobalVision> visionsCombat = new ArrayList<GlobalVision>();
            List<GlobalVision> visionsTechnique = new ArrayList<GlobalVision>();
            for (GlobalVision globalVision : computeStructure().get(TypeEpreuve.COMBAT.getValue()).values()) {
                visionsCombat.add(globalVision);
            }
            //Organize categorie combat by weight
            Collections.sort(visionsCombat, new DisciplineCombatComparator());

            for (GlobalVision globalVision : computeStructure().get(TypeEpreuve.TECHNIQUE.getValue()).values()) {
                visionsTechnique.add(globalVision);
            }
            Map<TypeEpreuve, List<GlobalVision>> visions = new HashMap<TypeEpreuve, List<GlobalVision>>();
            visions.put(TypeEpreuve.COMBAT, visionsCombat);
            visions.put(TypeEpreuve.TECHNIQUE, visionsTechnique);
            InscriptionsManager inscriptionsManager = new InscriptionsManager();
            boolean isSaved = inscriptionsManager.saveGlobalVisionFile(file, visions);
            if (isSaved) {
                NotificationView notificationView = new NotificationView(main.getMainStage());
                notificationView.notify(NotificationView.Level.SUCCESS, "Génération",
                        "Le fichier de résultat : " + file.getName() + " a été correctement généré.");
            } else {
                NotificationView notificationView = new NotificationView(main.getMainStage());
                notificationView.notify(NotificationView.Level.SUCCESS, "Génération",
                        "Erreur lors de la génération du fichier de résultat : " + file.getName() + ".");
            }
            LOGGER.info("generation global vision : " + file);
        }
    }

    @FXML
    private void generateGlobalVisionCurrent() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(main.getMainStage());

        LOGGER.info("Error when generate global vision.");
        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xlsx")) {
                file = new File(file.getPath() + ".xlsx");
            }

            List<GlobalVision> visionsCombat = new ArrayList<GlobalVision>();
            List<GlobalVision> visionsTechnique = new ArrayList<GlobalVision>();
            for (GlobalVision globalVision : computeStructureCurrent().get(TypeEpreuve.COMBAT.getValue()).values()) {
                visionsCombat.add(globalVision);
            }
            //Organize categorie combat by weight
            Collections.sort(visionsCombat, new DisciplineCombatComparator());
            for (GlobalVision globalVision : computeStructureCurrent().get(TypeEpreuve.TECHNIQUE.getValue()).values()) {
                visionsTechnique.add(globalVision);
            }
            Map<TypeEpreuve, List<GlobalVision>> visions = new HashMap<TypeEpreuve, List<GlobalVision>>();
            visions.put(TypeEpreuve.COMBAT, visionsCombat);
            visions.put(TypeEpreuve.TECHNIQUE, visionsTechnique);
            InscriptionsManager inscriptionsManager = new InscriptionsManager();
            boolean isSaved = inscriptionsManager.saveGlobalVisionFile(file, visions);
            if (isSaved) {
                NotificationView notificationView = new NotificationView(main.getMainStage());
                notificationView.notify(NotificationView.Level.SUCCESS, "Génération",
                        "Le fichier de résultat : " + file.getName() + " a été correctement généré.");
            } else {
                NotificationView notificationView = new NotificationView(main.getMainStage());
                notificationView.notify(NotificationView.Level.SUCCESS, "Génération",
                        "Erreur lors de la génération du fichier de résultat : " + file.getName() + ".");
            }
        }
    }

    @FXML
    public void generateFicheCompetitionTechnique() {
        generateFicheCompetition(TypeEpreuve.TECHNIQUE);
    }

    @FXML
    public void generateFicheCompetitionCombat() {
        generateFicheCompetition(TypeEpreuve.COMBAT);
    }

    private void generateFicheCompetition(TypeEpreuve typeEpreuve) {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(main.getMainStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xlsx")) {
                file = new File(file.getPath() + ".xlsx");
            }

            List<GlobalVision> visionsCombat = new ArrayList<GlobalVision>();
            List<GlobalVision> visionsTechnique = new ArrayList<GlobalVision>();
            for (GlobalVision globalVision : computeStructureCurrent().get(TypeEpreuve.COMBAT.getValue()).values()) {
                visionsCombat.add(globalVision);
            }
            //Organize categorie combat by weight
            Collections.sort(visionsCombat, new DisciplineCombatComparator());

            for (GlobalVision globalVision : computeStructureCurrent().get(TypeEpreuve.TECHNIQUE.getValue()).values()) {
                visionsTechnique.add(globalVision);
            }
            Map<TypeEpreuve, List<GlobalVision>> visions = new HashMap<TypeEpreuve, List<GlobalVision>>();
            visions.put(TypeEpreuve.COMBAT, visionsCombat);
            visions.put(TypeEpreuve.TECHNIQUE, visionsTechnique);
            InscriptionsManager inscriptionsManager = new InscriptionsManager();
            boolean isSaved = inscriptionsManager.saveFicheCompetition(file, competitionBean, typeEpreuve);
            if (isSaved) {
                NotificationView notificationView = new NotificationView(main.getMainStage());
                notificationView.notify(NotificationView.Level.SUCCESS, "Génération",
                        "Le fichier de résultat : " + file.getName() + " a été correctement généré.");
            } else {
                NotificationView notificationView = new NotificationView(main.getMainStage());
                notificationView.notify(NotificationView.Level.SUCCESS, "Génération",
                        "Erreur lors de la génération du fichier de résultat : " + file.getName() + ".");
            }
        }
    }

    private Map<String, Map<String, GlobalVision>> computeStructure() {
        Map<String, Map<String, GlobalVision>> mapCbtTech = new HashMap<String, Map<String, GlobalVision>>();
        mapCbtTech.put(TypeEpreuve.COMBAT.getValue(), new HashMap<String, GlobalVision>());
        mapCbtTech.put(TypeEpreuve.TECHNIQUE.getValue(), new HashMap<String, GlobalVision>());
        for (DisciplineBean disciplineBean : competitionBean.getDisciplines()) {
            for (EpreuveBean epreuveBean : competitionBean.getEpreuveByDiscipline(disciplineBean)) {
                GlobalVision testStructure = null;
                if (mapCbtTech.get(epreuveBean.getDiscipline().getType()).containsKey(epreuveBean.getLabel())) {
                    testStructure = mapCbtTech.get(epreuveBean.getDiscipline().getType()).get(epreuveBean.getLabel());
                } else {
                    testStructure = new GlobalVision(epreuveBean.getLabel());
                    mapCbtTech.get(epreuveBean.getDiscipline().getType()).put(epreuveBean.getLabel(), testStructure);
                }

                Map<String, List<Participant>> map1 = null;
                if (testStructure.getTypeCategories().containsKey(epreuveBean.getCategorie().getNom())) {
                    map1 = testStructure.getTypeCategories().get(epreuveBean.getCategorie().getNom());
                } else {
                    map1 = new HashMap<String, List<Participant>>();
                    testStructure.getTypeCategories().put(epreuveBean.getCategorie().getNom(), map1);
                }

                List<Participant> participants = null;
                if (map1.containsKey(epreuveBean.getCategorie().getSexe())) {
                    participants = map1.get(epreuveBean.getCategorie().getSexe());
                } else {
                    participants = new ArrayList<Participant>();
                    map1.put(epreuveBean.getCategorie().getSexe(), participants);
                }

                for (ParticipantBean participantBean : competitionBean.getParticipantByEpreuve(epreuveBean)) {
                    Participant participant = ParticipantConverter.convertParticipantBeanToParticipant(participantBean);
                    participants.add(participant);
                }

            }
        }

        return mapCbtTech;
    }

    private Map<String, Map<String, GlobalVision>> computeStructureCurrent() {
        Map<String, Map<String, GlobalVision>> mapCbtTech = new HashMap<String, Map<String, GlobalVision>>();
        mapCbtTech.put(TypeEpreuve.COMBAT.getValue(), new HashMap<String, GlobalVision>());
        mapCbtTech.put(TypeEpreuve.TECHNIQUE.getValue(), new HashMap<String, GlobalVision>());
        for (DisciplineBean disciplineBean : competitionBean.getDisciplines()) {
            for (EpreuveBean epreuveBean : competitionBean.getEpreuveByDiscipline(disciplineBean)) {
                if (!EtatEpreuve.FUSION.getValue().equals(epreuveBean.getEtat())) {
                    GlobalVision testStructure = null;
                    if (mapCbtTech.get(epreuveBean.getDiscipline().getType()).containsKey(epreuveBean.getLabel())) {
                        testStructure = mapCbtTech.get(epreuveBean.getDiscipline().getType()).get(epreuveBean.getLabel());
                    } else {
                        testStructure = new GlobalVision(epreuveBean.getLabel());
                        mapCbtTech.get(epreuveBean.getDiscipline().getType()).put(epreuveBean.getLabel(), testStructure);
                    }

                    Map<String, List<Participant>> map1 = null;
                    if (testStructure.getTypeCategories().containsKey(epreuveBean.getCategorie().getNom())) {
                        map1 = testStructure.getTypeCategories().get(epreuveBean.getCategorie().getNom());
                    } else {
                        map1 = new HashMap<String, List<Participant>>();
                        testStructure.getTypeCategories().put(epreuveBean.getCategorie().getNom(), map1);
                    }

                    List<Participant> participants = null;
                    if (map1.containsKey(epreuveBean.getCategorie().getSexe())) {
                        participants = map1.get(epreuveBean.getCategorie().getSexe());
                    } else {
                        participants = new ArrayList<Participant>();
                        map1.put(epreuveBean.getCategorie().getSexe(), participants);
                    }

                    for (ParticipantBean participantBean : competitionBean.getParticipantPresentByEpreuve(epreuveBean)) {
                        Participant participant = ParticipantConverter.convertParticipantBeanToParticipant(participantBean);
                        participants.add(participant);
                    }
                }
            }
        }

        return mapCbtTech;
    }

    @FXML
    private void handleManageCategories() {
        main.showCategorienView();
    }

    @FXML
    private void handlePresence() {
        main.showCompetitionView();
    }

    @FXML
    private void handleShowResultats() {
        main.showResultatsView();
    }

    @FXML
    private void handleGlobalVision() {
        main.showGlobalVisionView();
    }

    @FXML
    private void handleTechniqueGrid() {
        main.showTechnicalGrid();
    }

    public MenuItem getCreateCompetition() {
        return createCompetition;
    }

    public MenuItem getCreateCategorie() {
        return createCategorie;
    }

    public MenuItem getCreateEpreuve() {
        return createEpreuve;
    }

}
