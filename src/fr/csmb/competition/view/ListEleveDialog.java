package fr.csmb.competition.view;

import fr.csmb.competition.controller.DetailClubController;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.network.sender.NetworkSender;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Administrateur on 13/10/14.
 */
public class ListEleveDialog {

    private NetworkSender sender = new NetworkSender("", 9878);

    public void showClubDetailDialog(Stage mainStage, final ClubBean clubBean) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("fxml/clubDetailView.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();

            Button validateButton = new Button("Valider");
            validateButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    sender.sendClub(clubBean);
                }
            });

            borderPane.setBottom(validateButton);

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
