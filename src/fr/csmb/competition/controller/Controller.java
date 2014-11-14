package fr.csmb.competition.controller;

import fr.csmb.competition.Helper.ParticipantConverter;
import fr.csmb.competition.Main;
import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.component.grid.globalvision.GlobalVision;
import fr.csmb.competition.manager.InscriptionsManager;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.view.CategoriesView;
import fr.csmb.competition.view.NotificationView;
import fr.csmb.competition.xml.model.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrateur on 13/10/14.
 */
public class Controller {

    private Main main;

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
            InscriptionsManager inscriptionsManager = new InscriptionsManager();
            boolean isSaved = inscriptionsManager.saveResultatFile(file, competitionBean);
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

            List<GlobalVision> visions = new ArrayList<GlobalVision>();
            for (GlobalVision globalVision : computeStructure().values()) {
                visions.add(globalVision);
            }
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

    private Map<String, GlobalVision> computeStructure() {
        Map<String, GlobalVision> map = new HashMap<String, GlobalVision>();
        for (CategorieBean categorieBean : competitionBean.getCategories()) {
            for (EpreuveBean epreuveBean : categorieBean.getEpreuves()) {
                GlobalVision testStructure = null;
                if (map.containsKey(epreuveBean.getNom())) {
                    testStructure = map.get(epreuveBean.getNom());
                } else {
                    testStructure = new GlobalVision(epreuveBean.getNom());
                    map.put(epreuveBean.getNom(), testStructure);
                }

                Map<String, List<Participant>> map1 = null;
                if(testStructure.getTypeCategories().containsKey(categorieBean.getNom())) {
                    map1 = testStructure.getTypeCategories().get(categorieBean.getNom());
                } else {
                    map1 = new HashMap<String, List<Participant>>();
                    testStructure.getTypeCategories().put(categorieBean.getNom(), map1);
                }

                List<Participant> participants = null;
                if (map1.containsKey(categorieBean.getType())) {
                    participants = map1.get(categorieBean.getType());
                } else {
                    participants = new ArrayList<Participant>();
                    map1.put(categorieBean.getType(), participants);
                }

                ObservableList<ParticipantBean> participantBeans = extractParticipants(categorieBean.getType(), categorieBean.getNom(), epreuveBean.getNom());
                for (ParticipantBean participantBean : participantBeans) {
                    Participant participant = ParticipantConverter.convertParticipantBeanToParticipant(participantBean);
                    participants.add(participant);
                }

            }
        }

        return map;
    }

    public ObservableList<ParticipantBean> extractParticipants(String typeCategorie, String categorie, String epreuve) {
        ObservableList<ParticipantBean> participantBeans1 = FXCollections.observableArrayList();
        for (ClubBean clubBean : competitionBean.getClubs()) {
            for (EleveBean eleveBean : clubBean.getEleves()) {
                if (categorie.equals(eleveBean.getCategorie()) && typeCategorie.equals(eleveBean.getSexe())) {
                    if (eleveBean.getEpreuves().contains(epreuve)) {
                        ParticipantBean participantBean = new ParticipantBean(eleveBean.getNom(), eleveBean.getPrenom());
                        participantBean.setClub(clubBean.getNom());
                        participantBeans1.add(participantBean);
                    }
                }
            }
        }

        return participantBeans1;
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
