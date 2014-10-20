package fr.csmb.competition.view;

import com.sun.javafx.collections.transformation.SortedList;
import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.xml.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Administrateur on 20/10/14.
 */
public class ResultatsView {


    private CompetitionBean competitionBean;

    public void showView(Stage mainStage, Competition competition) {
        loadCompetitionBean(competition);
        StackPane stackPane = new StackPane();
        createTableView(stackPane);
        Stage stage = new Stage();
        stage.setTitle("Résultats compétition : " + competition.getNom());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainStage);
        Scene scene = new Scene(stackPane);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void createTableView(StackPane stackPane) {
        for (CategorieBean categorieBean : competitionBean.getCategories()) {
            for (EpreuveBean epreuveBean : categorieBean.getEpreuves()) {
                if ("Termine".equals(epreuveBean.getEtat())) {
                    TableView tableView = new TableView();

                    SortedList<ParticipantBean> sortedList = new SortedList<ParticipantBean>(epreuveBean.getParticipants());
                    sortedList.sort();

                    TableColumn columnTitle = new TableColumn(categorieBean.getNom() + " - " + epreuveBean.getNom());
                    tableView.getColumns().add(columnTitle);
                    TableColumn columnClassement = new TableColumn("Place");
                    columnClassement.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Integer>("classementFinal"));
                    columnTitle.getColumns().add(columnClassement);
                    TableColumn columnNom = new TableColumn("Nom");
                    columnNom.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Integer>("nom"));
                    columnTitle.getColumns().add(columnNom);
                    TableColumn columnPrenom = new TableColumn("Prénom");
                    columnPrenom.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Integer>("prenom"));
                    columnTitle.getColumns().add(columnPrenom);
                    tableView.setItems(sortedList);
                    stackPane.getChildren().add(tableView);
                }

            }
        }
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
