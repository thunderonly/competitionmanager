package fr.csmb.competition.controller;

import com.sun.javafx.collections.transformation.SortedList;
import fr.csmb.competition.model.ParticipantBean;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.model.comparator.ComparatorClubTotalCombat;
import fr.csmb.competition.model.comparator.ComparatorClubTotalTechnique;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.type.TypeEpreuve;
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
    private CompetitionBean competitionBean;

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

    private int pointForFirstPlace = 4;
    private int pointForSecondPlace = 3;
    private int pointForThirdPlace = 2;
    private int pointForFourthPlace = 1;

    @FXML
    private void initialize() {
        nomClub.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClubBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClubBean, String> clubBeanStringCellDataFeatures) {
                return clubBeanStringCellDataFeatures.getValue().nomProperty();
            }
        });
        nomClub.setSortable(false);
        identifiantClub.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClubBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClubBean, String> clubBeanStringCellDataFeatures) {
                return clubBeanStringCellDataFeatures.getValue().identifiantProperty();
            }
        });
        identifiantClub.setSortable(false);
        totalTechnique.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClubBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClubBean, String> clubBeanStringCellDataFeatures) {
                return clubBeanStringCellDataFeatures.getValue().totalTechniqueProperty().asString();
            }
        });
        totalTechnique.setSortable(false);
        totalCombat.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClubBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClubBean, String> clubBeanStringCellDataFeatures) {
                return clubBeanStringCellDataFeatures.getValue().totalCombatProperty().asString();
            }
        });
        totalCombat.setSortable(false);
        totalGeneral.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClubBean, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClubBean, String> clubBeanStringCellDataFeatures) {
                return clubBeanStringCellDataFeatures.getValue().totalGeneralProperty().asString();
            }
        });
        totalGeneral.setSortable(false);
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
                listEleveDialog.showClubDetailDialog(mainStage, competitionBean, clubBean2);
            }
        });
    }

    public TableView<ClubBean> getTableClassementClub() {
        return tableClassementClub;
    }

    public TableColumn<ClubBean, String> getClassementTechnique() {
        return classementTechnique;
    }

    public TableColumn<ClubBean, String> getClassementCombat() {
        return classementCombat;
    }

    public TableColumn<ClubBean, String> getClassementGeneral() {
        return classementGeneral;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setCompetitionBean(CompetitionBean competitionBean) {
        this.competitionBean = competitionBean;
    }

    public void computeClassementClub() {
        for (EpreuveBean epreuveBean : competitionBean.getEpreuves()) {
            if (EtatEpreuve.TERMINE.getValue().equals(epreuveBean.getEtat())) {
                for (ParticipantBean participantBean : competitionBean.getParticipantByEpreuve(epreuveBean)) {
                    ClubBean clubBean = getClubById(competitionBean, participantBean.getClub());
                    Integer points = pointForParticipant(participantBean);
                    if (epreuveBean.getDiscipline().getType().equals(TypeEpreuve.COMBAT.getValue())) {
                        clubBean.setTotalCombat(clubBean.getTotalCombat() + points);
                    } else {
                        clubBean.setTotalTechnique(clubBean.getTotalTechnique() + points);
                    }
                }
            }
        }

        for (ClubBean clubBean : competitionBean.getClubs()) {
            clubBean.setTotalGeneral(clubBean.getTotalTechnique() + clubBean.getTotalCombat());
        }
        computeClassementTechnique();
        computeClassementCombat();
        computeClassementGeneral();
    }
    private void computeClassementGeneral() {
        SortedList<ClubBean> sortableList = new SortedList<ClubBean>(competitionBean.getClubs());
        sortableList.sort();
        int i = 0;
        ClubBean previousClub = null;
        for (ClubBean club : sortableList) {
            if (previousClub != null) {
                if (previousClub.getTotalGeneral() == club.getTotalGeneral()) {
                    club.setClassementGeneral(previousClub.getClassementGeneral());
                    i++;
                } else {
                    i++;
                    club.setClassementGeneral(i);
                }
            } else {
                i++;
                club.setClassementGeneral(i);
            }
            previousClub = club;
        }
    }

    private void computeClassementTechnique() {
        SortedList<ClubBean> sortableList = new SortedList<ClubBean>(competitionBean.getClubs());
        sortableList.setComparator(new ComparatorClubTotalTechnique());
        sortableList.sort();
        int i = 0;
        ClubBean previousClub = null;
        for (ClubBean club : sortableList) {
            if (previousClub != null) {
                if (previousClub.getTotalTechnique() == club.getTotalTechnique()) {
                    club.setClassementTechnique(previousClub.getClassementTechnique());
                    i++;
                } else {
                    i++;
                    club.setClassementTechnique(i);
                }
            } else {
                i++;
                club.setClassementTechnique(i);
            }
            previousClub = club;
        }
    }

    private void computeClassementCombat() {
        SortedList<ClubBean> sortableList = new SortedList<ClubBean>(competitionBean.getClubs());
        sortableList.setComparator(new ComparatorClubTotalCombat());
        sortableList.sort();
        int i = 0;
        ClubBean previousClub = null;
        for (ClubBean club : sortableList) {
            if (previousClub != null) {
                if (previousClub.getTotalCombat() == club.getTotalCombat()) {
                    club.setClassementCombat(previousClub.getClassementCombat());
                    i++;
                } else {
                    i++;
                    club.setClassementCombat(i);
                }
            } else {
                i++;
                club.setClassementCombat(i);
            }
            previousClub = club;
        }
    }

    private Integer pointForParticipant(ParticipantBean participantBean) {
        Integer points = 0;
        switch (participantBean.getClassementFinal().intValue()) {
            case 1:
                points+=pointForFirstPlace;
                break;
            case 2:
                points+=pointForSecondPlace;
                break;
            case 3:
                points+=pointForThirdPlace;
                break;
            case 4:
                points+=pointForFourthPlace;
                break;
            default:
                break;
        }
        return points;
    }

    private ClubBean getClubById(CompetitionBean competitionBean, String id) {
        for (ClubBean clubBean : competitionBean.getClubs()) {
            if (clubBean.getIdentifiant().equals(id)) {
                return clubBean;
            }
        }
        return null;
    }
}
