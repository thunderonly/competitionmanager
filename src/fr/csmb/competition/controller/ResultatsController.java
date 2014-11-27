package fr.csmb.competition.controller;

import fr.csmb.competition.model.ParticipantBean;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Created by Administrateur on 20/10/14.
 */
public class ResultatsController {
    @FXML
    private TableView tableResultats;
    @FXML
    private TableColumn titleCategorie;
    @FXML
    private TableColumn place;
    @FXML
    private TableColumn nomParticipant;
    @FXML
    private TableColumn prenomParticipant;

    @FXML
    private void initialize() {
        place.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Integer>("classementFinal"));
        nomParticipant.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Integer>("nom"));
        nomParticipant.setSortable(false);
        prenomParticipant.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Integer>("prenom"));
        prenomParticipant.setSortable(false);
    }

    public TableView getTableResultats() {
        return tableResultats;
    }

    public TableColumn getTitleCategorie() {
        return titleCategorie;
    }

    public TableColumn getPlace() {
        return place;
    }

    public TableColumn getNomParticipant() {
        return nomParticipant;
    }

    public TableColumn getPrenomParticipant() {
        return prenomParticipant;
    }
}
