/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.component.grid.technical;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.sun.javafx.collections.transformation.SortedList;

import fr.csmb.competition.component.grid.GridComponent;
import fr.csmb.competition.component.grid.bean.ParticipantBean;
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

    public GridComponentTechnical(List<ParticipantBean> joueurs) {
        resultatsList = new ArrayList<ParticipantBean>();
        Group group = this;
        group.getChildren().add(table);
        for (ParticipantBean joueur : joueurs) {
            data.add(joueur);
        }
        //Create a customer cell factory so that cells can support editing.
        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                return new EditingCell();
            }
        };

        TableColumn columnClassement = new TableColumn("Classement");
        TableColumn columnClassementAuto = new TableColumn("Auto");
        columnClassementAuto.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Integer>("classementAuto"));
        columnClassementAuto.setCellFactory(cellFactory);
        columnClassementAuto.setEditable(false);
        columnClassementAuto.setMinWidth(minWidthColumnNote);
        TableColumn columnClassementManuel = new TableColumn("Manuel");
        columnClassementManuel.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Integer>("classementManuel"));
        columnClassementManuel.setCellFactory(cellFactory);
        columnClassementManuel.setMinWidth(minWidthColumnNote);
        TableColumn columnClassementFinal = new TableColumn("Final");
        columnClassementFinal.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Integer>("classementFinal"));
        columnClassementFinal.setCellFactory(cellFactory);
        columnClassementFinal.setEditable(false);
        columnClassementFinal.setMinWidth(minWidthColumnNote);
        columnClassement.getColumns().addAll(columnClassementAuto, columnClassementManuel, columnClassementFinal);

        TableColumn columnInformationsJoueur = new TableColumn("Informations");
        TableColumn columnJoueurNom = new TableColumn("Nom");
        columnJoueurNom.setCellValueFactory(new PropertyValueFactory<ParticipantBean, String>("nom"));
        columnJoueurNom.setMinWidth(minWidthColumnJoueur);
        columnInformationsJoueur.getColumns().add(columnJoueurNom);

        TableColumn columnNotesJuge = new TableColumn("Notes Juge");
        TableColumn columnNotesJuge1 = new TableColumn("Juge 1");
        columnNotesJuge1.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Integer>("note1"));
        columnNotesJuge1.setCellFactory(cellFactory);
        columnNotesJuge1.setMinWidth(minWidthColumnNote);
        TableColumn columnNotesJuge2 = new TableColumn("Juge 2");
        columnNotesJuge2.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Integer>("note2"));
        columnNotesJuge2.setCellFactory(cellFactory);
        columnNotesJuge2.setMinWidth(minWidthColumnNote);
        TableColumn columnNotesJuge3 = new TableColumn("Juge 3");
        columnNotesJuge3.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Integer>("note3"));
        columnNotesJuge3.setCellFactory(cellFactory);
        columnNotesJuge3.setMinWidth(minWidthColumnNote);
        TableColumn columnNotesJuge4 = new TableColumn("Juge 4");
        columnNotesJuge4.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Integer>("note4"));
        columnNotesJuge4.setCellFactory(cellFactory);
        columnNotesJuge4.setMinWidth(minWidthColumnNote);
        TableColumn columnNotesJuge5 = new TableColumn("Juge 5");
        columnNotesJuge5.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Integer>("note5"));
        columnNotesJuge5.setCellFactory(cellFactory);
        columnNotesJuge5.setMinWidth(minWidthColumnNote);
        TableColumn columnNotesTotal = new TableColumn("Total");
        columnNotesTotal.setCellValueFactory(new PropertyValueFactory<ParticipantBean, Integer>("noteTotal"));
        columnNotesTotal.setMinWidth(minWidthColumnNote);
        columnNotesTotal.setCellFactory(cellFactory);
        columnNotesTotal.setEditable(false);
        columnNotesJuge.getColumns().addAll(columnNotesJuge1, columnNotesJuge2, columnNotesJuge3, columnNotesJuge4,
                columnNotesJuge5, columnNotesTotal);

        columnNotesJuge1.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ParticipantBean, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ParticipantBean, Integer> cellEditEvent) {
                ParticipantBean participant = cellEditEvent.getRowValue();
                participant.setNote1(cellEditEvent.getNewValue());
                computeClassementAuto();
            }
        });
        columnNotesJuge2.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ParticipantBean, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ParticipantBean, Integer> cellEditEvent) {
                ParticipantBean participant = cellEditEvent.getRowValue();
                participant.setNote2(cellEditEvent.getNewValue());
                computeClassementAuto();
            }
        });
        columnNotesJuge3.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ParticipantBean, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ParticipantBean, Integer> cellEditEvent) {
                ParticipantBean participant = cellEditEvent.getRowValue();
                participant.setNote3(cellEditEvent.getNewValue());
                computeClassementAuto();

            }
        });
        columnNotesJuge4.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ParticipantBean, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ParticipantBean, Integer> cellEditEvent) {
                ParticipantBean participant = cellEditEvent.getRowValue();
                participant.setNote4(cellEditEvent.getNewValue());
                computeClassementAuto();
            }
        });
        columnNotesJuge5.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ParticipantBean, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ParticipantBean, Integer> cellEditEvent) {
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
            }
        });


        table.setItems(data);
        table.getColumns().addAll(columnClassement, columnInformationsJoueur, columnNotesJuge);
        table.setEditable(true);
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

    private class ResultatComparator implements Comparator<ParticipantBean> {

        @Override
        public int compare(ParticipantBean p1, ParticipantBean p2) {
            if (p1.getClassementFinal() > p2.getClassementFinal()) {
                return 1;
            } else if (p1.getClassementFinal() < p2.getClassementFinal()) {
                return -1;
            }
            return 0;
        }
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
            previousParticipant = participant;
        }
    }
}