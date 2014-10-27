package fr.csmb.competition.controller;

import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.view.ListEleveDialog;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;


/**
 * Created by Administrateur on 13/10/14.
 */
public class ClassementClubController {

    private Stage mainStage;
    private ListEleveDialog listEleveDialog = new ListEleveDialog();

    @FXML
    private TableView<ClubBean> tableClassementClub;
    @FXML
    private TableColumn<ClubBean, String> nomClub;
    @FXML
    private TableColumn<ClubBean, String> identifiantClub;
    @FXML
    private TableColumn<ClubBean, String> totalTechnique;
    @FXML
    private TableColumn<ClubBean, String> totalCombat;
    @FXML
    private TableColumn<ClubBean, String> totalGeneral;
    @FXML
    private TableColumn<ClubBean, String> classementTechnique;
    @FXML
    private TableColumn<ClubBean, String> classementCombat;
    @FXML
    private TableColumn<ClubBean, String> classementGeneral;

    @FXML
    private void initialize() {
        nomClub.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClubBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClubBean, String> clubBeanStringCellDataFeatures) {
                return clubBeanStringCellDataFeatures.getValue().nomProperty();
            }
        });
        identifiantClub.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClubBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClubBean, String> clubBeanStringCellDataFeatures) {
                return clubBeanStringCellDataFeatures.getValue().identifiantProperty();
            }
        });
        totalTechnique.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClubBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClubBean, String> clubBeanStringCellDataFeatures) {
                return clubBeanStringCellDataFeatures.getValue().totalTechniqueProperty().asString();
            }
        });
        totalCombat.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClubBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClubBean, String> clubBeanStringCellDataFeatures) {
                return clubBeanStringCellDataFeatures.getValue().totalCombatProperty().asString();
            }
        });
        totalGeneral.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClubBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClubBean, String> clubBeanStringCellDataFeatures) {
                return clubBeanStringCellDataFeatures.getValue().totalGeneralProperty().asString();
            }
        });
        classementTechnique.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClubBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClubBean, String> clubBeanStringCellDataFeatures) {
                return clubBeanStringCellDataFeatures.getValue().classementTechniqueProperty().asString();
            }
        });
        classementCombat.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClubBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClubBean, String> clubBeanStringCellDataFeatures) {
                return clubBeanStringCellDataFeatures.getValue().classementCombatProperty().asString();
            }
        });
        classementGeneral.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClubBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClubBean, String> clubBeanStringCellDataFeatures) {
                return clubBeanStringCellDataFeatures.getValue().classementGeneralProperty().asString();
            }
        });

        tableClassementClub.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ClubBean>() {
            @Override
            public void changed(ObservableValue<? extends ClubBean> observableValue, ClubBean clubBean, ClubBean clubBean2) {
                listEleveDialog.showClubDetailDialog(mainStage, clubBean2);
            }
        });
    }

    public TableView<ClubBean> getTableClassementClub() {
        return tableClassementClub;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
}
