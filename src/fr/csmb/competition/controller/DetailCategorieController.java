package fr.csmb.competition.controller;

import fr.csmb.competition.model.ParticipantBean;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

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
}
