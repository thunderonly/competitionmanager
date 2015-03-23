package fr.csmb.competition.controller;

import fr.csmb.competition.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

/**
 * Created by Administrateur on 23/03/15.
 */
public class AddParticipantController {

    @FXML
    private ComboBox eleveComboBox;
    @FXML
    private Button validateButton;

    private CompetitionBean competitionBean;
    private EpreuveBean epreuveBean;

    @FXML
    private void initialize(){

    }

    public void initComponent(CompetitionBean competitionBean, EpreuveBean epreuveBean) {
        ObservableList eleves = FXCollections.observableArrayList();

        for (ClubBean clubBean : competitionBean.getClubs()) {
            eleves.addAll(clubBean.getEleves());
        }

        eleveComboBox.getItems().addAll(eleves);
    }

    @FXML
    private void validate() {
        ObservableList<ParticipantBean> participantBeans = competitionBean.getParticipantByEpreuve(epreuveBean);
//        EleveBean eleveBean = eleveComboBox.getValue();
    }

    @FXML
    private void cancel() {

    }
}
