/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.component.grid.technical;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.collections.transformation.SortedList;

import fr.csmb.competition.component.grid.GridComponent;
import fr.csmb.competition.component.grid.ParticipantClassementFinalListener;
import fr.csmb.competition.model.ParticipantBean;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class GridComponentTechnical extends GridComponent {

    private int minWidthColumnNote = 60;
    private int minWidthColumnJoueur = 80;
    private TableView table = new TableView();
    private ObservableList<ParticipantBean> data = FXCollections.observableArrayList();
    private ParticipantClassementFinalListener participantClassementFinalListener;

    public GridComponentTechnical(List<ParticipantBean> joueurs) {
        resultatsList = new ArrayList<ParticipantBean>();
        for (ParticipantBean joueur : joueurs) {
            data.add(joueur);
        }

    }

    public void drawGrid() {
        Group group = this;
        createTableView();
        group.getChildren().add(table);
    }

    public List<ParticipantBean> getResultatsList() {
        resultatsList.clear();

        SortedList<ParticipantBean> resultats = new SortedList<ParticipantBean>(data);
        resultats.sort();
        for (ParticipantBean participant : resultats) {
            resultatsList.add(participant);
        }
        return resultatsList;
    }

    public void addParticipant(ParticipantBean participantBean) {
        table.getItems().add(participantBean);
    }

    public ParticipantBean getSelectedParticipant() {
        return (ParticipantBean)table.getSelectionModel().getSelectedItem();
    }

    public void delParticipant(ParticipantBean participantBean) {
        table.getItems().remove(participantBean);
    }

    private void createTableView() {
        //Create a customer cell factory so that cells can support editing.
        Callback<TableColumn, TableCell> cellFactoryInteger = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                return new EditingIntegerCell();
            }
        };
        Callback<TableColumn, TableCell> cellFactoryDouble = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                return new EditingDoubleCell();
            }
        };

        TableColumn columnClassement = new TableColumn("Classement");
        TableColumn columnClassementAuto = new TableColumn("Auto");
        columnClassementAuto.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Integer>("classementAuto"));
        columnClassementAuto.setCellFactory(cellFactoryInteger);
        columnClassementAuto.setEditable(false);
        columnClassementAuto.setMinWidth(minWidthColumnNote);
        TableColumn columnClassementManuel = new TableColumn("Manuel");
        columnClassementManuel.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Integer>("classementManuel"));
        columnClassementManuel.setCellFactory(cellFactoryInteger);
        columnClassementManuel.setMinWidth(minWidthColumnNote);
        TableColumn columnClassementFinal = new TableColumn("Final");
        columnClassementFinal.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Integer>("classementFinal"));
        columnClassementFinal.setCellFactory(cellFactoryInteger);
        columnClassementFinal.setEditable(false);
        columnClassementFinal.setMinWidth(minWidthColumnNote);
        columnClassement.getColumns().addAll(columnClassementAuto, columnClassementManuel, columnClassementFinal);

        TableColumn columnInformationsJoueur = new TableColumn("Informations");
        TableColumn columnJoueurNom = new TableColumn("Nom");
        columnJoueurNom.setCellValueFactory(new PropertyValueFactory<ParticipantBean, String>("nom"));
        columnJoueurNom.setMinWidth(minWidthColumnJoueur);
        columnInformationsJoueur.getColumns().add(columnJoueurNom);

        TableColumn columnJoueurPrenom = new TableColumn("Prénom");
        columnJoueurPrenom.setCellValueFactory(new PropertyValueFactory<ParticipantBean, String>("prenom"));
        columnJoueurPrenom.setMinWidth(minWidthColumnJoueur);
        columnInformationsJoueur.getColumns().add(columnJoueurPrenom);

        TableColumn columnNotesJuge = new TableColumn("Notes Juge");
        TableColumn columnNotesJuge1 = new TableColumn("Juge 1");
        columnNotesJuge1.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Double>("note1"));
        columnNotesJuge1.setCellFactory(cellFactoryDouble);
        columnNotesJuge1.setMinWidth(minWidthColumnNote);
        TableColumn columnNotesJuge2 = new TableColumn("Juge 2");
        columnNotesJuge2.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Double>("note2"));
        columnNotesJuge2.setCellFactory(cellFactoryDouble);
        columnNotesJuge2.setMinWidth(minWidthColumnNote);
        TableColumn columnNotesJuge3 = new TableColumn("Juge 3");
        columnNotesJuge3.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Double>("note3"));
        columnNotesJuge3.setCellFactory(cellFactoryDouble);
        columnNotesJuge3.setMinWidth(minWidthColumnNote);
        TableColumn columnNotesJuge4 = new TableColumn("Juge 4");
        columnNotesJuge4.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Double>("note4"));
        columnNotesJuge4.setCellFactory(cellFactoryDouble);
        columnNotesJuge4.setMinWidth(minWidthColumnNote);
        TableColumn columnNotesJuge5 = new TableColumn("Juge 5");
        columnNotesJuge5.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Double>("note5"));
        columnNotesJuge5.setCellFactory(cellFactoryDouble);
        columnNotesJuge5.setMinWidth(minWidthColumnNote);
        TableColumn columnNotesTotal = new TableColumn("Total");
        columnNotesTotal.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Double>("noteTotal"));
        columnNotesTotal.setMinWidth(minWidthColumnNote);
        columnNotesTotal.setCellFactory(cellFactoryDouble);
        columnNotesTotal.setEditable(false);
        columnNotesJuge.getColumns().addAll(columnNotesJuge1, columnNotesJuge2, columnNotesJuge3, columnNotesJuge4,
                columnNotesJuge5, columnNotesTotal);

        columnNotesJuge1.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ParticipantBean, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ParticipantBean, Double> cellEditEvent) {
                ParticipantBean participant = cellEditEvent.getRowValue();
                participant.setNote1(cellEditEvent.getNewValue());
                computeClassementAuto();
            }
        });
        columnNotesJuge2.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ParticipantBean, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ParticipantBean, Double> cellEditEvent) {
                ParticipantBean participant = cellEditEvent.getRowValue();
                participant.setNote2(cellEditEvent.getNewValue());
                computeClassementAuto();
            }
        });
        columnNotesJuge3.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ParticipantBean, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ParticipantBean, Double> cellEditEvent) {
                ParticipantBean participant = cellEditEvent.getRowValue();
                participant.setNote3(cellEditEvent.getNewValue());
                computeClassementAuto();

            }
        });
        columnNotesJuge4.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ParticipantBean, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ParticipantBean, Double> cellEditEvent) {
                ParticipantBean participant = cellEditEvent.getRowValue();
                participant.setNote4(cellEditEvent.getNewValue());
                computeClassementAuto();
            }
        });
        columnNotesJuge5.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ParticipantBean, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ParticipantBean, Double> cellEditEvent) {
                ParticipantBean participant = cellEditEvent.getRowValue();
                participant.setNote5(cellEditEvent.getNewValue());
                computeClassementAuto();
            }
        });
        columnClassementManuel.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ParticipantBean, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ParticipantBean, Integer> cellEditEvent) {
                ParticipantBean participant = cellEditEvent.getRowValue();
                participant.setClassementManuel(cellEditEvent.getNewValue());
                participant.setClassementFinal(cellEditEvent.getNewValue());
                participantClassementFinalListener.fireUpdateClassementFinal(participant);
            }
        });
        columnClassementFinal.cellValueFactoryProperty().addListener(new ChangeListener<ParticipantBean>() {
            @Override
            public void changed(ObservableValue<? extends ParticipantBean> observableValue, ParticipantBean o,
                    ParticipantBean o2) {
                System.out.println("Update data on column final");
            }
        });


        table.setItems(data);
        table.getColumns().addAll(columnClassement, columnInformationsJoueur, columnNotesJuge);
        table.setEditable(true);
    }

    private void computeClassementAuto() {
        SortedList<ParticipantBean> parts = new SortedList<ParticipantBean>(data);
        parts.sort();
        int i = 0;
        ParticipantBean previousParticipant = null;
        for (ParticipantBean participant : parts) {
            if (previousParticipant != null) {
                if (previousParticipant.getNoteTotal() == participant.getNoteTotal()) {
                    data.get(data.indexOf(participant)).setClassementAuto(previousParticipant.getClassementAuto());
                    data.get(data.indexOf(participant)).setClassementFinal(previousParticipant.getClassementFinal());
                    data.get(data.indexOf(participant)).setClassementManuel(0);
                    previousParticipant.setClassementManuel(0);
                    i++;
                } else {
                    i++;
                    data.get(data.indexOf(participant)).setClassementAuto(i);
                    data.get(data.indexOf(participant)).setClassementFinal(i);
                }
            } else {
                i++;
                data.get(data.indexOf(participant)).setClassementAuto(i);
                data.get(data.indexOf(participant)).setClassementFinal(i);
            }
            this.participantClassementFinalListener.fireUpdateClassementFinal(participant);
            previousParticipant = participant;
        }
    }

    public void setParticipantClassementFinalListener(ParticipantClassementFinalListener participantClassementFinalListener) {
        this.participantClassementFinalListener = participantClassementFinalListener;
    }
}
