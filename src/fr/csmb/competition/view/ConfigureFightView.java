package fr.csmb.competition.view;

import fr.csmb.competition.Helper.ParticipantConverter;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.ParticipantBean;
import fr.csmb.competition.component.grid.fight.GridComponentFight2;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.xml.model.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.BlendMode;
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
    private ListView<ParticipantBean> participantBeanListView = new ListView<ParticipantBean>();

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
                if (epreuveBean.getEtat() == null) {
                    epreuveBean.setEtat(EtatEpreuve.VALIDE.getValue());
                }
                resultatList = gridComponentFight.getResultatsList();
                for (ParticipantBean participantBean : resultatList) {
                    ParticipantBean epreuveParticipantBean = competitionBean.getParticipantByNomPrenomEpreuve(
                            participantBean.getNom(), participantBean.getPrenom(), epreuveBean);
                    if (epreuveParticipantBean != null) {
                        epreuveParticipantBean.setPlaceOnGrid(participantBean.getPlaceOnGrid());
                    }
                }

                for (ParticipantBean participantBean : participantBeanListView.getItems()) {
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
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(hBox1);
//        borderPane.setCenter(scrollPane);

        SplitPane splitPane = new SplitPane();
        borderPane.setCenter(splitPane);
        splitPane.setDividerPosition(0, 0.2);

        createView(splitPane, competitionBean, epreuveBean);

        currentStage.showAndWait();
    }

    private void createView(SplitPane hBox, CompetitionBean competitionBean, EpreuveBean epreuveBean) {

        participantBeanListView.getItems().addAll(competitionBean.getParticipantByEpreuve(epreuveBean));
        initializeListener(participantBeanListView);

        ObservableList<ParticipantBean> participantBeans = FXCollections.observableArrayList();

        for (ParticipantBean participantBean : participantBeanListView.getItems()) {
            if (participantBean.getPlaceOnGrid() != 0) {
                participantBeans.add(participantBean);
            } else {
                participantBeans.add(new ParticipantBean("", ""));
            }
        }

        //remove participant from listView
        for (ParticipantBean participantBean : participantBeans) {
            if (participantBeanListView.getItems().contains(participantBean)) {
                participantBeanListView.getItems().remove(participantBean);
            }
        }

        gridComponentFight = new GridComponentFight2(participantBeans);
        gridComponentFight.drawGrid(true);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridComponentFight);

        hBox.getItems().add(participantBeanListView);
        hBox.getItems().add(scrollPane);
    }

    private void initializeListener(final ListView<ParticipantBean> participantBeanListView) {
        participantBeanListView.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Dragboard dragBoard = participantBeanListView.startDragAndDrop(TransferMode.MOVE);

                ClipboardContent content = new ClipboardContent();
                ParticipantBean participantBean = participantBeanListView.getSelectionModel().getSelectedItem();
                content.put(format, ParticipantConverter.convertParticipantBeanToParticipant(
                        participantBean));
                dragBoard.setContent(content);
                participantBeanListView.getItems().remove(participantBean);
                participantBeanListView.getSelectionModel().clearSelection();
            }
        });


        participantBeanListView.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {

//                DataFormat format = DataFormat.lookupMimeType("fr.csmb.competition.xml.model.Participant");
                Participant participant1 = (Participant) dragEvent.getDragboard().getContent(format);
                ParticipantBean player = ParticipantConverter.convertParticipantToParticipantBean(participant1);
                player.setPlaceOnGrid(0);
                participantBeanListView.getItems().add(player);
                dragEvent.setDropCompleted(true);
            }
        });
        participantBeanListView.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                participantBeanListView.setBlendMode(BlendMode.DARKEN);
            }
        });
        participantBeanListView.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                participantBeanListView.setBlendMode(null);
            }
        });
        participantBeanListView.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {

                dragEvent.acceptTransferModes(TransferMode.MOVE);
            }
        });
    }
}
