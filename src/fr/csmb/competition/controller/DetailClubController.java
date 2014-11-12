package fr.csmb.competition.controller;

import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.network.sender.NetworkSender;
import fr.csmb.competition.view.EditEleveView;
import fr.csmb.competition.view.ListEleveDialog;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
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
    private TableColumn<EleveBean, Boolean> presenceEleve;
    @FXML
    private Button editButton;

    private NetworkSender sender;
    private ListEleveDialog listEleveDialog;
    private ClubBean clubBean;
    private CompetitionBean competitionBean;
    private Stage mainStage;

    @FXML
    private void initialize() {
        editButton.setDisable(true);

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

        presenceEleve.setCellFactory(new Callback<TableColumn<EleveBean, Boolean>, TableCell<EleveBean, Boolean>>() {
            @Override
            public TableCell<EleveBean, Boolean> call(TableColumn<EleveBean, Boolean> eleveBeanBooleanTableColumn) {
                return new PresenceCheckBoxTableCell<EleveBean, Boolean>();
            }
        });
        presenceEleve.setCellValueFactory(new PropertyValueFactory<EleveBean, Boolean>("presence"));

        tableEleve.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<EleveBean>() {
            @Override
            public void changed(ObservableValue<? extends EleveBean> observableValue, EleveBean eleveBean, EleveBean eleveBean2) {
                editButton.setDisable(false);
            }
        });
    }

    public TableView<EleveBean> getTableEleve() {
        return tableEleve;
    }

    public void setSender(NetworkSender sender) {
        this.sender = sender;
    }

    public void setListEleveDialog(ListEleveDialog listEleveDialog) {
        this.listEleveDialog = listEleveDialog;
    }

    public void setClubBean(ClubBean clubBean) {
        this.clubBean = clubBean;
    }

    public void setCompetitionBean(CompetitionBean competitionBean) {
        this.competitionBean = competitionBean;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    //CheckBoxTableCell for creating a CheckBox in a table cell
    private class PresenceCheckBoxTableCell<S, T> extends TableCell<S, T> {
        private final CheckBox checkBox;
        private ObservableValue<T> ov;

        public PresenceCheckBoxTableCell() {
            this.checkBox = new CheckBox();
            this.checkBox.setAlignment(Pos.CENTER);
            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (getTableRow() != null && getTableRow().getItem() != null) {
                        if (newValue) {
                            ((EleveBean) getTableRow().getItem()).setPresence(true);
                        } else {
                            ((EleveBean) getTableRow().getItem()).setPresence(false);
                        }
                    }
                }
            });

            setAlignment(Pos.CENTER);
            setGraphic(checkBox);
        }

        @Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setGraphic(checkBox);
                if (ov instanceof BooleanProperty) {
                    checkBox.selectedProperty().unbindBidirectional((BooleanProperty) ov);
                }
                ov = getTableColumn().getCellObservableValue(getIndex());
                if (ov instanceof BooleanProperty) {
                    checkBox.selectedProperty().bindBidirectional((BooleanProperty) ov);
                }
            }
        }
    }



    @FXML
    private void validate() {
        sender.sendClub(clubBean);
        listEleveDialog.close();
    }

    @FXML
    private void addEleve() {
        EditEleveView editEleveView = new EditEleveView(mainStage, competitionBean, clubBean);
        editEleveView.showView();
    }

    @FXML
    private void editEleve() {
        final EleveBean eleveBean = tableEleve.getSelectionModel().getSelectedItem();
        EditEleveView editEleveView = new EditEleveView(mainStage, competitionBean, clubBean);
        editEleveView.showView(eleveBean);
        eleveBean.poidsProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                int index = tableEleve.getItems().indexOf(eleveBean);
                tableEleve.getItems().remove(index);
                tableEleve.getItems().add(index, eleveBean);
            }
        });
        eleveBean.ageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                int index = tableEleve.getItems().indexOf(eleveBean);
                tableEleve.getItems().remove(index);
                tableEleve.getItems().add(index, eleveBean);
            }
        });
        eleveBean.sexeProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                int index = tableEleve.getItems().indexOf(eleveBean);
                tableEleve.getItems().remove(index);
                tableEleve.getItems().add(index, eleveBean);
            }
        });
        eleveBean.nomProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                int index = tableEleve.getItems().indexOf(eleveBean);
                tableEleve.getItems().remove(index);
                tableEleve.getItems().add(index, eleveBean);
            }
        });
        eleveBean.prenomProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                int index = tableEleve.getItems().indexOf(eleveBean);
                tableEleve.getItems().remove(index);
                tableEleve.getItems().add(index, eleveBean);
            }
        });
    }
}
