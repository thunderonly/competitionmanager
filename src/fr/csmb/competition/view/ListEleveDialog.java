package fr.csmb.competition.view;

import fr.csmb.competition.controller.DetailClubController;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.EleveBean;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Administrateur on 13/10/14.
 */
public class ListEleveDialog {

    public void showClubDetailDialog(Stage mainStage, ClubBean clubBean) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("clubDetailView.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Détail club : " + clubBean.getNom());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage);
            Scene scene = new Scene(borderPane);
            stage.setScene(scene);

            DetailClubController detailClubController = loader.getController();

            detailClubController.getTableEleve().setItems(clubBean.getEleves());
            stage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
