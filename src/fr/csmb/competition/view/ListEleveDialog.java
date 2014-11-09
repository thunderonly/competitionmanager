package fr.csmb.competition.view;

import fr.csmb.competition.controller.DetailClubController;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.network.sender.NetworkSender;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
            final BorderPane root = (BorderPane) mainStage.getScene().getRoot();
            final Node oldCenter = root.getCenter();
            final Node oldBottom = root.getBottom();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("fxml/clubDetailView.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();
            root.setCenter(borderPane);
            root.setBottom(null);
            mainStage.getScene().getStylesheets().add(getClass().getResource("css/fightView.css").toExternalForm());

            Button validateButton = new Button("Valider");
            validateButton.getStyleClass().add("buttonCompetition");
            validateButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    sender.sendClub(clubBean);
                    root.setBottom(oldBottom);
                    root.setCenter(oldCenter);
                }
            });

            borderPane.setBottom(validateButton);

            DetailClubController detailClubController = loader.getController();

            if (clubBean != null) {
                detailClubController.getTableEleve().setItems(clubBean.getEleves());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
