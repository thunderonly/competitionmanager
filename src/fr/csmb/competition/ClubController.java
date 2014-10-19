package fr.csmb.competition;

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
public class ClubController {

    private Stage mainStage;
    private ListEleveDialog listEleveDialog = new ListEleveDialog();

    @FXML
    private TableView<ClubBean> tableClub;
    @FXML
    private TableColumn<ClubBean, String> nomClub;
    @FXML
    private TableColumn<ClubBean, String> identifiantClub;
    @FXML
    private TableColumn<ClubBean, String> responsableClub;

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
        responsableClub.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClubBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClubBean, String> clubBeanStringCellDataFeatures) {
                return clubBeanStringCellDataFeatures.getValue().responsableProperty();
            }
        });

        tableClub.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ClubBean>() {
            @Override
            public void changed(ObservableValue<? extends ClubBean> observableValue, ClubBean clubBean, ClubBean clubBean2) {
                listEleveDialog.showClubDetailDialog(mainStage, clubBean2);
            }
        });
    }

    public TableView<ClubBean> getTableClub() {
        return tableClub;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
}
