package fr.csmb.competition.view;

import fr.csmb.competition.Helper.ParticipantConverter;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.ParticipantBean;
import fr.csmb.competition.component.grid.fight.GridComponentFight2;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.type.EtatEpreuve;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by Administrateur on 03/11/14.
 */
public class ConfigureFightView {

    private Stage currentStage;
    private List<ParticipantBean> resultatList = null;
    private GridComponentFight2 gridComponentFight;
    public final static DataFormat format = new DataFormat("fr.csmb.competition.xml.model.Participant");

    public void showView(Stage mainStage, final CompetitionBean competitionBean, final EpreuveBean epreuveBean) {
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, 1200, 900);
        currentStage = new Stage();
        currentStage.setTitle("Configuration des combats");
        currentStage.initOwner(mainStage);
        currentStage.initModality(Modality.WINDOW_MODAL);
        currentStage.setScene(scene);
        currentStage.getScene().getStylesheets().add(getClass().getResource("css/fightView.css").toExternalForm());
        currentStage.getScene().getStylesheets().add(getClass().getResource("css/global.css").toExternalForm());

        Button cancelButton = new Button("Annuler");
        cancelButton.getStyleClass().add("buttonCompetition");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                currentStage.close();
            }
        });
        Button validButton = new Button("Valider");
        validButton.getStyleClass().add("buttonCompetition");
        validButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                epreuveBean.setEtat(EtatEpreuve.VALIDE.getValue());
                resultatList = gridComponentFight.getResultatsList();
                for (ParticipantBean participantBean : resultatList) {
                    System.out.println("Place on Grid : " + participantBean.getPlaceOnGrid() + " " + participantBean.toString());
                    ParticipantBean epreuveParticipantBean = competitionBean.getParticipantByNomPrenomEpreuve(
                            participantBean.getNom(), participantBean.getPrenom(), epreuveBean);
                    if (epreuveParticipantBean != null) {
                        epreuveParticipantBean.setPlaceOnGrid(participantBean.getPlaceOnGrid());
                    }
                }
                currentStage.close();
            }
        });
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.getChildren().addAll(validButton, cancelButton);
        borderPane.setBottom(hBox);

        HBox hBox1 = new HBox();
        hBox1.setSpacing(10);
        borderPane.setCenter(hBox1);

        createView(hBox1, competitionBean, epreuveBean);

        currentStage.showAndWait();
    }

    private void createView(HBox hBox, CompetitionBean competitionBean, EpreuveBean epreuveBean) {
        ListView<ParticipantBean> participantBeanListView = new ListView<ParticipantBean>();
        participantBeanListView.getItems().addAll(competitionBean.getParticipantByEpreuve(epreuveBean));
        initializeListener(participantBeanListView);

        ObservableList<ParticipantBean> participantBeans = FXCollections.observableArrayList();
        for (int i = 0; i < participantBeanListView.getItems().size(); i++) {
            participantBeans.add(new ParticipantBean("", ""));
        }

        gridComponentFight = new GridComponentFight2(participantBeans);
        gridComponentFight.drawGrid(true);

        hBox.getChildren().addAll(participantBeanListView, gridComponentFight);
    }

    private void initializeListener(final ListView<ParticipantBean> participantBeanListView) {
        participantBeanListView.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("setOnDragDetected");

                Dragboard dragBoard = participantBeanListView.startDragAndDrop(TransferMode.MOVE);

                ClipboardContent content = new ClipboardContent();
                content.put(format, ParticipantConverter.convertParticipantBeanToParticipant(
                        participantBeanListView.getSelectionModel().getSelectedItem()));
                dragBoard.setContent(content);
            }
        });
    }
}
