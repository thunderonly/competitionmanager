package fr.csmb.competition.view;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import com.sun.javafx.collections.SortableList;
import com.sun.javafx.collections.transformation.SortedList;

import fr.csmb.competition.Helper.CompetitionConverter;
import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.controller.ClassementClubController;
import fr.csmb.competition.controller.ResultatsController;
import fr.csmb.competition.manager.InscriptionsManager;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.model.comparator.ComparatorClubTotalCombat;
import fr.csmb.competition.model.comparator.ComparatorClubTotalTechnique;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.xml.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Administrateur on 20/10/14.
 */
public class ResultatsView {

    private int pointForFirstPlace = 4;
    private int pointForSecondPlace = 3;
    private int pointForThirdPlace = 2;
    private int pointForFourthPlace = 1;

    private CompetitionBean competitionBean;
    private Stage mainStage;

    public void showView(Stage mainStage, CompetitionBean competition) {
        competitionBean = competition;
        BorderPane root = (BorderPane) mainStage.getScene().getRoot();
        BorderPane stackPane = new BorderPane();
        createTableView(root);
        root.setBottom(null);
//        stage.setTitle("Résultats compétition : " + competition.getNom());
        this.mainStage = mainStage;
    }

    private void createTableView(BorderPane stackPane) {

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        for (CategorieBean categorieBean : competitionBean.getCategories()) {
            boolean isTabCreated = false;
            Tab categorieTab = new Tab(categorieBean.getType().concat(" - ").concat(categorieBean.getNom()));
            GridPane gridPane = new GridPane();
            int row = 0;
            int col = 0;
            String[] typeEpreuves = new String[]{ TypeEpreuve.TECHNIQUE.getValue(), TypeEpreuve.COMBAT.getValue()};
            for (String typeEpreuve : typeEpreuves) {
                for (EpreuveBean epreuveBean : categorieBean.getEpreuves()) {
                    if (epreuveBean.getType().equals(typeEpreuve) && EtatEpreuve.TERMINE.getValue().equals(epreuveBean.getEtat())) {
                        try {
                            isTabCreated = true;

                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("fxml/resultatsView.fxml"));
                            BorderPane borderPane = (BorderPane) loader.load();
                            ResultatsController controller = (ResultatsController) loader.getController();
                            controller.getTitleCategorie().setText(categorieBean.getNom().concat(" - ").concat(epreuveBean.getNom()));
                            controller.getTableResultats().setItems(epreuveBean.getParticipants());
                            controller.getPlace().setSortable(false);
                            controller.getTableResultats().getSortOrder().setAll(Collections.singletonList(controller
                                    .getPlace()));
                            gridPane.add(borderPane, col, row);
                            row++;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                col++;
            }
            if (isTabCreated) {
                categorieTab.setContent(gridPane);
                tabPane.getTabs().add(categorieTab);
            }
        }

        Tab classementClub = new Tab("Classement Club");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("fxml/classementClubView.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();
            ClassementClubController controller = (ClassementClubController) loader.getController();
            computeClassementClub();
            SortedList<ClubBean> clubBeans = new SortedList<ClubBean>(competitionBean.getClubs());
            controller.getTableClassementClub().setItems(clubBeans);
            controller.getTableClassementClub().getSortOrder().setAll(Collections.singletonList(controller
                    .getClassementGeneral()));
            controller.getClassementGeneral().setSortable(false);
            classementClub.setContent(borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tabPane.getTabs().add(classementClub);
        stackPane.setCenter(tabPane);

        Button generateResultat = new Button("Générer");
        generateResultat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();

                // Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                        "XML files (*.xlsx)", "*.xlsx");
                fileChooser.getExtensionFilters().add(extFilter);

                // Show save file dialog
                File file = fileChooser.showSaveDialog(mainStage);

                if (file != null) {
                    // Make sure it has the correct extension
                    if (!file.getPath().endsWith(".xlsx")) {
                        file = new File(file.getPath() + ".xlsx");
                    }
                    InscriptionsManager inscriptionsManager = new InscriptionsManager();
                    boolean isSaved = inscriptionsManager.saveResultatFile(file, competitionBean);
                    if (isSaved) {
                        NotificationView notificationView = new NotificationView(mainStage);
                        notificationView.notify(NotificationView.Level.SUCCESS, "Génération",
                                "Le fichier de résultat : " + file.getName() + " a été correctement généré.");
                    } else {
                        NotificationView notificationView = new NotificationView(mainStage);
                        notificationView.notify(NotificationView.Level.SUCCESS, "Génération",
                                "Erreur lors de la génération du fichier de résultat : " + file.getName() + ".");
                    }
                }
            }
        });
        stackPane.setBottom(generateResultat);
    }

    private void computeClassementClub() {
        for (ClubBean clubBean : competitionBean.getClubs()) {
            int pointTechnique = 0;
            int pointCombat = 0;
            for (EleveBean eleveBean : clubBean.getEleves()) {
                pointCombat+=getPointsForEleve(eleveBean, TypeEpreuve.COMBAT.getValue());
                pointTechnique+=getPointsForEleve(eleveBean, TypeEpreuve.TECHNIQUE.getValue());
            }
            clubBean.setTotalCombat(pointCombat);
            clubBean.setTotalTechnique(pointTechnique);
            clubBean.setTotalGeneral(pointCombat + pointTechnique);
        }
        computeClassementTechnique();
        computeClassementCombat();
        computeClassementGeneral();
    }

    private void computeClassementGeneral() {
        SortedList<ClubBean> sortableList = new SortedList<ClubBean>(competitionBean.getClubs());
        sortableList.sort();
        int i = 0;
        ClubBean previousClub = null;
        for (ClubBean club : sortableList) {
            if (previousClub != null) {
                if (previousClub.getTotalGeneral() == club.getTotalGeneral()) {
                    club.setClassementGeneral(previousClub.getClassementGeneral());
                    i++;
                } else {
                    i++;
                    club.setClassementGeneral(i);
                }
            } else {
                i++;
                club.setClassementGeneral(i);
            }
            previousClub = club;
        }
    }

    private void computeClassementTechnique() {
        SortedList<ClubBean> sortableList = new SortedList<ClubBean>(competitionBean.getClubs());
        sortableList.setComparator(new ComparatorClubTotalTechnique());
        sortableList.sort();
        int i = 0;
        ClubBean previousClub = null;
        for (ClubBean club : sortableList) {
            if (previousClub != null) {
                if (previousClub.getTotalTechnique() == club.getTotalTechnique()) {
                    club.setClassementTechnique(previousClub.getClassementTechnique());
                    i++;
                } else {
                    i++;
                    club.setClassementTechnique(i);
                }
            } else {
                i++;
                club.setClassementTechnique(i);
            }
            previousClub = club;
        }
    }

    private void computeClassementCombat() {
        SortedList<ClubBean> sortableList = new SortedList<ClubBean>(competitionBean.getClubs());
        sortableList.setComparator(new ComparatorClubTotalCombat());
        sortableList.sort();
        int i = 0;
        ClubBean previousClub = null;
        for (ClubBean club : sortableList) {
            if (previousClub != null) {
                if (previousClub.getTotalCombat() == club.getTotalCombat()) {
                    club.setClassementCombat(previousClub.getClassementCombat());
                    i++;
                } else {
                    i++;
                    club.setClassementCombat(i);
                }
            } else {
                i++;
                club.setClassementCombat(i);
            }
            previousClub = club;
        }
    }

    private int getPointsForEleve(EleveBean eleveBean, String epreuveType) {
        Integer points = 0;

        for (CategorieBean categorieBean : competitionBean.getCategories()) {
            for (EpreuveBean epreuveBean : categorieBean.getEpreuves()) {
                if (epreuveBean.getType().equalsIgnoreCase(epreuveType)) {
                    for (ParticipantBean participantBean : epreuveBean.getParticipants()) {
                        if (eleveBean.getPrenom().equalsIgnoreCase(participantBean.getPrenom()) && eleveBean.getNom().equalsIgnoreCase(participantBean.getNom())) {
                            switch (participantBean.getClassementFinal()) {
                                case 1:
                                    points+=pointForFirstPlace;
                                    break;
                                case 2:
                                    points+=pointForSecondPlace;
                                    break;
                                case 3:
                                    points+=pointForThirdPlace;
                                    break;
                                case 4:
                                    points+=pointForFourthPlace;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        }
        return points;
    }

}
