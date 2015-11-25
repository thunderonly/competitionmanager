package fr.csmb.competition.controller;

import fr.csmb.competition.component.grid.technical.GridComponentTechnical;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.model.ParticipantBean;
import fr.csmb.competition.network.sender.NetworkSender;
import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.view.ConfigureFightView;
import fr.csmb.competition.view.GridCategorieView;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Administrateur on 13/10/14.
 */
public class DetailCategorieController {

    @FXML
    private TableView<ParticipantBean> tableParticipant;
    @FXML
    private TableColumn<ParticipantBean, String> nom;
    @FXML
    private TableColumn<ParticipantBean, String> prenom;

    private CompetitionBean competitionBean;
    private EpreuveBean epreuveBean;
    private GridCategorieView gridCategorieView;

    @FXML
    private void initialize() {
        nom.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ParticipantBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ParticipantBean, String> eleveBeanStringCellDataFeatures) {
                return eleveBeanStringCellDataFeatures.getValue().nomProperty();
            }
        });
        prenom.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ParticipantBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ParticipantBean, String> eleveBeanStringCellDataFeatures) {
                return eleveBeanStringCellDataFeatures.getValue().prenomProperty();
            }
        });


    }

    public TableView<ParticipantBean> getTableParticipant() {
        return tableParticipant;
    }


    @FXML
    private void addPart() {
        if (gridCategorieView == null) {
            gridCategorieView = new GridCategorieView();
            gridCategorieView.initView(competitionBean, epreuveBean);
        }
        final Stage newStage = new Stage();
        final AddParticipantController participantController = this.gridCategorieView.initAddPartView(newStage);
        participantController.setActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newStage.close();
                competitionBean.getParticipants().add(participantController.getParticipantBean());
                tableParticipant.getItems().add(participantController.getParticipantBean());
                NetworkSender.getINSTANCE().send(competitionBean, epreuveBean);
            }
        });
        newStage.show();
    }

    @FXML
    private void delPart() {
        ParticipantBean participantBean = tableParticipant.getSelectionModel().getSelectedItem();
        tableParticipant.getItems().remove(participantBean);
        competitionBean.getParticipants().remove(participantBean);
        NetworkSender.getINSTANCE().send(competitionBean, epreuveBean);
    }

    public void setCompetitionBean(CompetitionBean competitionBean) {
        this.competitionBean = competitionBean;
    }

    public void setEpreuveBean(EpreuveBean epreuveBean) {
        this.epreuveBean = epreuveBean;
    }
}
