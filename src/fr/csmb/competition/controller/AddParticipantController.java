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
public class AddParticipantController {

    @FXML
    private ComboBox<EleveBean> eleveComboBox;
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
        ObservableList eleves = FXCollections.observableArrayList();

        for (ClubBean club : competitionBean.getClubs()) {
            eleves.addAll(club.getEleves());
        }

        this.competitionBean = competitionBean;
        this.epreuveBean = epreuveBean;
        eleveComboBox.getItems().addAll(eleves);
    }

    @FXML
    private void validate() {
        EleveBean eleveBean = eleveComboBox.getValue();
        ParticipantBean participantBean = new ParticipantBean(eleveBean.getNom(), eleveBean.getPrenom());
        participantBean.setEpreuveBean(epreuveBean);
        if (eleveBean.getPoids() != null && !eleveBean.getPoids().trim().equals("")) {
            participantBean.setPoids(Integer.parseInt(eleveBean.getPoids()));
        }
        participantBean.setClub(competitionBean.getClubByEleve(eleveBean).getIdentifiant());
        competitionBean.getParticipants().add(participantBean);
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
