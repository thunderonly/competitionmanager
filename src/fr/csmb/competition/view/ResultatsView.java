package fr.csmb.competition.view;

import com.sun.javafx.collections.transformation.SortedList;
import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.controller.ClassementClubController;
import fr.csmb.competition.controller.ResultatsController;
import fr.csmb.competition.manager.ResultatsManager;
import fr.csmb.competition.model.*;
import fr.csmb.competition.model.comparator.ComparatorClubTotalCombat;
import fr.csmb.competition.model.comparator.ComparatorClubTotalTechnique;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.type.TypeEpreuve;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

/**
 * Created by Administrateur on 20/10/14.
 */
public class ResultatsView {

    private CompetitionBean competitionBean;
    private Stage mainStage;

    public void showView(Stage mainStage, CompetitionBean competition) {
        competitionBean = competition;
        BorderPane root = (BorderPane) mainStage.getScene().getRoot();
        createTableView(root);
        this.mainStage = mainStage;
        this.mainStage.getScene().getStylesheets().add(getClass().getResource("css/global.css").toExternalForm());
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
                row = 0;
                for (EpreuveBean epreuveBean : competitionBean.getEpreuveByCategorie(categorieBean)) {
                    if (epreuveBean.getDiscipline().getType().equals(typeEpreuve) && EtatEpreuve.TERMINE.getValue().equals(epreuveBean.getEtat())) {
                        try {
                            isTabCreated = true;

                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("fxml/resultatsView.fxml"));
                            BorderPane borderPane = (BorderPane) loader.load();
                            ResultatsController controller = (ResultatsController) loader.getController();
                            controller.getTitleCategorie().setText(categorieBean.getNom().concat(" - ").concat(epreuveBean.getDiscipline().getNom()));

                            ObservableList<ParticipantBean> newList = FXCollections.observableArrayList();
                            //Get participant for 1, 2, 3, 4 place
                            for (ParticipantBean participantBean : epreuveBean.getParticipants()) {
                                if (participantBean.getClassementFinal() > 0 && participantBean.getClassementFinal() < 5) {
                                    newList.add(participantBean);
                                }
                            }

                            controller.getTableResultats().setItems(newList);
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
            controller.setCompetitionBean(competitionBean);
            controller.computeClassementClub();
            SortedList<ClubBean> clubBeans = new SortedList<ClubBean>(competitionBean.getClubs());
            controller.getTableClassementClub().setItems(clubBeans);
            controller.getTableClassementClub().getSortOrder().setAll(Collections.singletonList(controller
                    .getClassementGeneral()));
            classementClub.setContent(borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tabPane.getTabs().add(classementClub);
        stackPane.setCenter(tabPane);

        Button generateResultat = new Button("Générer");
        generateResultat.getStyleClass().add("buttonCompetition");
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
                    ResultatsManager resultatsManager = new ResultatsManager();
                    boolean isSaved = resultatsManager.saveResultatFile(file, competitionBean);
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





}
