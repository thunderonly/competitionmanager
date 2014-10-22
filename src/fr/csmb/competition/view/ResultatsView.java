package fr.csmb.competition.view;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import com.sun.javafx.collections.transformation.SortedList;
import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.controller.ClassementClubController;
import fr.csmb.competition.controller.ResultatsController;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.xml.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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

    public void showView(Stage mainStage, Competition competition) {
        loadCompetitionBean(competition);
        BorderPane stackPane = new BorderPane();
        createTableView(stackPane);
        Stage stage = new Stage();
        stage.setTitle("Résultats compétition : " + competition.getNom());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainStage);
//        stage.setMinWidth(500);
        Scene scene = new Scene(stackPane);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void createTableView(BorderPane stackPane) {

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        for (CategorieBean categorieBean : competitionBean.getCategories()) {
            Tab categorieTab = new Tab(categorieBean.getType().concat(" - ").concat(categorieBean.getNom()));
            GridPane gridPane = new GridPane();
            int row = 0;
            int col = 0;
            String[] typeEpreuves = new String[]{ TypeEpreuve.TECHNIQUE.getValue(), TypeEpreuve.COMBAT.getValue()};
            for (String typeEpreuve : typeEpreuves) {
                for (EpreuveBean epreuveBean : categorieBean.getEpreuves()) {
                    if (EtatEpreuve.TERMINE.getValue().equals(epreuveBean.getEtat())) {
                        try {
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
            categorieTab.setContent(gridPane);
            tabPane.getTabs().add(categorieTab);
        }

        Tab classementClub = new Tab("Classement Club");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("fxml/classementClubView.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();
            ClassementClubController controller = (ClassementClubController) loader.getController();
            computeClassementClub();
            controller.getTableClassementClub().setItems(competitionBean.clubsProperty());
            classementClub.setContent(borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tabPane.getTabs().add(classementClub);
        stackPane.setCenter(tabPane);
    }

    private void loadCompetitionBean(Competition competition) {
        competitionBean = new CompetitionBean(competition.getNom());
        ObservableList<CategorieBean> categorieBeans = FXCollections.observableArrayList();
        for (Categorie categorie : competition.getCategories()) {
            CategorieBean categorieBean = new CategorieBean(categorie.getNomCategorie());
            categorieBean.setType(categorie.getTypeCategorie());
            ObservableList<EpreuveBean> epreuveBeans = FXCollections.observableArrayList();
            for (Epreuve epreuve : categorie.getEpreuves()) {
                EpreuveBean epreuveBean = new EpreuveBean(epreuve.getNomEpreuve());
                epreuveBean.setEtat(epreuve.getEtatEpreuve());
                epreuveBean.setType(epreuve.getTypeEpreuve());
                ObservableList<ParticipantBean> participantBeans = FXCollections.observableArrayList();
                for (Participant participant : epreuve.getParticipants()) {
                    ParticipantBean participantBean = new ParticipantBean(participant.getNomParticipant(), participant.getPrenomParticipant());
                    participantBean.setClassementFinal(Integer.parseInt(participant.getClassementFinal()));
                    participantBeans.add(participantBean);
                }
                epreuveBean.setParticipants(participantBeans);
                epreuveBeans.add(epreuveBean);
            }
            categorieBean.setEpreuves(epreuveBeans);
            categorieBeans.add(categorieBean);
        }
        competitionBean.setCategories(categorieBeans);
        ObservableList<ClubBean> clubBeans = FXCollections.observableArrayList();
        for (Club club : competition.getClubs()) {
            ClubBean clubBean = new ClubBean();
            clubBean.setNom(club.getNomClub());
            clubBean.setIdentifiant(club.getIdentifiant());
            clubBean.setResponsable(club.getResponsable());
            ObservableList<EleveBean> eleveBeans = FXCollections.observableArrayList();
            for (Eleve eleve : club.getEleves()) {
                EleveBean eleveBean = new EleveBean();
                eleveBean.setNom(eleve.getNomEleve());
                eleveBean.setPrenom(eleve.getPrenomEleve());
                eleveBean.setLicence(eleve.getLicenceEleve());
                eleveBeans.add(eleveBean);
            }
            clubBean.setEleves(eleveBeans);
            clubBeans.add(clubBean);
        }
        competitionBean.setClubs(clubBeans);
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
