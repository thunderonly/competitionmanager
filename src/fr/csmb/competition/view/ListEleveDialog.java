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
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Administrateur on 13/10/14.
 */
public class ListEleveDialog {

    private NetworkSender sender = new NetworkSender("", 9878);
    private Button editButton;
    private Node oldCenter;
    private Node oldBottom;
    private BorderPane root;

    public void showClubDetailDialog(final Stage mainStage, final CompetitionBean competitionBean, final ClubBean clubBean) {
        try {
            root = (BorderPane) mainStage.getScene().getRoot();
            oldCenter = root.getCenter();
            oldBottom = root.getBottom();

            mainStage.getScene().getStylesheets().add(getClass().getResource("css/fightView.css").toExternalForm());
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("fxml/clubDetailView.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();
            root.setCenter(borderPane.getCenter());
            root.setBottom(borderPane.getBottom());

            DetailClubController detailClubController = loader.getController();
            detailClubController.setClubBean(clubBean);
            detailClubController.setListEleveDialog(this);
            detailClubController.setSender(sender);
            detailClubController.setCompetitionBean(competitionBean);
            detailClubController.setMainStage(mainStage);

            if (clubBean != null) {
                detailClubController.getTableEleve().setItems(clubBean.getEleves());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        root.setBottom(oldBottom);
        root.setCenter(oldCenter);
    }
}
