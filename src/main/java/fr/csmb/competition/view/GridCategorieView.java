package fr.csmb.competition.view;

import fr.csmb.competition.component.grid.GridComponent;
import fr.csmb.competition.component.grid.technical.GridComponentTechnical;
import fr.csmb.competition.controller.AddParticipantController;
import fr.csmb.competition.controller.DelParticipantController;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.model.ParticipantBean;
import fr.csmb.competition.network.sender.NetworkSender;
import fr.csmb.competition.type.TypeEpreuve;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Administrateur on 10/11/15.
 */
public class GridCategorieView {

    private GridComponent gridComponent;
    private CompetitionBean competitionBean;
    private EpreuveBean epreuveBean;
    private NetworkSender sender;

    public void initView(CompetitionBean competitionBean, EpreuveBean epreuveBean) {
        this.competitionBean = competitionBean;
        this.epreuveBean = epreuveBean;
    }

    public AddParticipantController initAddPartView(Stage newStage) {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("fxml/addPartView.fxml"));
            BorderPane pane = (BorderPane) loader.load();
            final AddParticipantController participantController = loader.getController();
            participantController.initComponent(competitionBean, epreuveBean);
            newStage.setScene(new Scene(pane));
            return participantController;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DelParticipantController initDelPartView(Stage newStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("fxml/delPartView.fxml"));
            BorderPane pane = (BorderPane) loader.load();
            final DelParticipantController participantController = loader.getController();
            participantController.initComponent(competitionBean, epreuveBean);
            newStage.setScene(new Scene(pane));

            return participantController;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delPart() {
        ParticipantBean participantBean = null;
        if (gridComponent instanceof GridComponentTechnical) {
            participantBean = ((GridComponentTechnical) gridComponent).getSelectedParticipant();
            gridComponent.delParticipant(participantBean);
            competitionBean.getParticipants().remove(participantBean);
        } else {
            try {
                final Stage newStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("fxml/delPartView.fxml"));
                BorderPane pane = (BorderPane) loader.load();
                final DelParticipantController participantController = loader.getController();
                participantController.initComponent(competitionBean, epreuveBean);
                newStage.setScene(new Scene(pane));
                newStage.show();

                participantController.setActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gridComponent.delParticipant(participantController.getParticipantBean());
                        if (epreuveBean.getDiscipline().getType().equals(TypeEpreuve.COMBAT.getValue())) {
                            ConfigureFightView configureFightView = new ConfigureFightView();
//                        newStage.close();
                            configureFightView.showView(newStage, competitionBean, epreuveBean);
                            gridComponent.drawGrid();
                        }
                        sender.send(competitionBean, epreuveBean);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
