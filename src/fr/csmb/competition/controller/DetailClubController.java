package fr.csmb.competition.controller;

import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.EleveBean;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * Created by Administrateur on 13/10/14.
 */
public class DetailClubController {

    @FXML
    private TableView<EleveBean> tableEleve;
    @FXML
    private TableColumn<EleveBean, String> licenceEleve;
    @FXML
    private TableColumn<EleveBean, String> nomEleve;
    @FXML
    private TableColumn<EleveBean, String> prenomEleve;
    @FXML
    private TableColumn<EleveBean, String> ageEleve;
    @FXML
    private TableColumn<EleveBean, String> categorieEleve;
    @FXML
    private TableColumn<EleveBean, String> sexeEleve;
    @FXML
    private TableColumn<EleveBean, String> poidsEleve;

    @FXML
    private void initialize() {
        licenceEleve.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EleveBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<EleveBean, String> eleveBeanStringCellDataFeatures) {
                return eleveBeanStringCellDataFeatures.getValue().licenceProperty();
            }
        });
        nomEleve.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EleveBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<EleveBean, String> eleveBeanStringCellDataFeatures) {
                return eleveBeanStringCellDataFeatures.getValue().nomProperty();
            }
        });
        prenomEleve.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EleveBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<EleveBean, String> eleveBeanStringCellDataFeatures) {
                return eleveBeanStringCellDataFeatures.getValue().prenomProperty();
            }
        });
        ageEleve.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EleveBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<EleveBean, String> eleveBeanStringCellDataFeatures) {
                return eleveBeanStringCellDataFeatures.getValue().ageProperty();
            }
        });
        categorieEleve.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EleveBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<EleveBean, String> eleveBeanStringCellDataFeatures) {
                return eleveBeanStringCellDataFeatures.getValue().categorieProperty();
            }
        });
        sexeEleve.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EleveBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<EleveBean, String> eleveBeanStringCellDataFeatures) {
                return eleveBeanStringCellDataFeatures.getValue().sexeProperty();
            }
        });
        poidsEleve.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EleveBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<EleveBean, String> eleveBeanStringCellDataFeatures) {
                return eleveBeanStringCellDataFeatures.getValue().poidsProperty();
            }
        });


    }

    public TableView<EleveBean> getTableEleve() {
        return tableEleve;
    }
}
