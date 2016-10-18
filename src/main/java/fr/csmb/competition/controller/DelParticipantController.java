package fr.csmb.competition.controller;

import fr.csmb.competition.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Administrateur on 23/03/15.
 */
public class DelParticipantController {

    @FXML
    private ComboBox<ParticipantBean> eleveComboBox;
    @FXML
    private Button validateButton;

    private CompetitionBean competitionBean;
    private EpreuveBean epreuveBean;
    private ParticipantBean participantBean;

    private ActionListener actionListener;

    @FXML
    private void initialize(){

    }

    public void initComponent(CompetitionBean competitionBean, EpreuveBean epreuveBean) {
        ObservableList participants = FXCollections.observableArrayList();

        for (ParticipantBean participant : competitionBean.getParticipantByEpreuve(epreuveBean)) {
            participants.addAll(participant);
        }

        this.competitionBean = competitionBean;
        this.epreuveBean = epreuveBean;
        eleveComboBox.getItems().addAll(participants);
    }

    @FXML
    private void validate() {
        ParticipantBean participantBean = eleveComboBox.getValue();
        participantBean.setEpreuveBean(epreuveBean);
        competitionBean.getParticipants().remove(participantBean);
        this.participantBean = participantBean;

        this.actionListener.actionPerformed(new ActionEvent(this, 0, "validate"));
    }

    @FXML
    private void cancel() {

    }

    public ParticipantBean getParticipantBean() {
        return participantBean;
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }
}
