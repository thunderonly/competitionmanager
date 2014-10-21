package fr.csmb.competition.view;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import com.sun.javafx.collections.transformation.SortedList;
import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.controller.ResultatsController;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EpreuveBean;
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
            Tab categorieTab = new Tab(categorieBean.getNom());
            GridPane gridPane = new GridPane();
            int i = 0;
            for (EpreuveBean epreuveBean : categorieBean.getEpreuves()) {
                if ("Termine".equals(epreuveBean.getEtat())) {

                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("fxml/resultatsView.fxml"));
                        BorderPane borderPane = (BorderPane) loader.load();
                        ResultatsController controller = (ResultatsController) loader.getController();
                        controller.getTitleCategorie().setText(categorieBean.getNom() + " - " + epreuveBean.getNom());
                        controller.getTableResultats().setItems(epreuveBean.getParticipants());
                        controller.getPlace().setSortable(false);
                        controller.getTableResultats().getSortOrder().setAll(Collections.singletonList(controller.getPlace()));
                        gridPane.add(borderPane, 0, i);
                        i++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            categorieTab.setContent(gridPane);
            tabPane.getTabs().add(categorieTab);
        }
        stackPane.setCenter(tabPane);
    }

    private void loadCompetitionBean(Competition competition) {
        competitionBean = new CompetitionBean(competition.getNom());
        ObservableList<CategorieBean> categorieBeans = FXCollections.observableArrayList();
        for (Categorie categorie : competition.getCategories()) {
            CategorieBean categorieBean = new CategorieBean(categorie.getNomCategorie());
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
    }

}
