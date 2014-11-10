/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.csmb.competition.xml.model.Participant;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import junit.framework.Test;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class GridGlobalVision extends Application {

    @Override
    public void start(Stage stage) {
        GridPane gridPane = new GridPane();
        gridPane.getStylesheets().addAll(getClass().getResource("Main.css").toExternalForm());
        drawGridPane(gridPane);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(gridPane);
        Group group = new Group();
        group.getChildren().add(hBox);
        Scene scene = new Scene(group);
        stage.setTitle("Welcome to JavaFX!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }


    public static void main(String[] args) {
        Application.launch(args);
    }

    public void drawGridPane(GridPane gridPane) {

        List<TestStructure> testStructures = new ArrayList<TestStructure>();


        testStructures.add(createStructure("Main Nue"));
        testStructures.add(createStructure("Armes"));

        int startColumn = 0;
        for (TestStructure structure : testStructures) {
            VBox title = new VBox();
            title.setAlignment(Pos.CENTER);
            Label categorieTitle = new Label(structure.getNomCategorie());
            categorieTitle.getStyleClass().add("biglabelGridPane");
            title.getChildren().add(categorieTitle);
            gridPane.add(title, startColumn, 0, 5, 1);

            drawHeader(gridPane, startColumn);
            drawBody(gridPane, startColumn, 2, structure.getTypeCategories());
            startColumn = 5;
        }
    }

    private void drawBody(GridPane gridPane, int colStart, int rowStart, Map<String, Map<String, List<Participant>>> datas) {

        int rowStartForCategorie = rowStart;
        for (String keyCategorie : datas.keySet()) {
            int rowStartForSexe = rowStartForCategorie;
            int rowStartForData = rowStartForCategorie;
            int totalRow = 0; //With total row
            for (String key : datas.get(keyCategorie).keySet()) {
                int totalParticipant = datas.get(keyCategorie).get(key).size(); //with total row

                for (Participant participant : datas.get(keyCategorie).get(key)) {
                    addData(gridPane, colStart + 2, rowStartForData, participant.getNomParticipant().concat(" ").concat(participant.getPrenomParticipant()));
                    addData(gridPane, colStart + 3, rowStartForData, "Club " + rowStartForData);
                    addData(gridPane, colStart + 4, rowStartForData, "1");
                    rowStartForData++;
                }

                addTotalCategorie(gridPane, colStart + 1, rowStartForData, key, totalParticipant);
                rowStartForData++;

                VBox vBox1 = new VBox();
                vBox1.setAlignment(Pos.CENTER);
                Label sexeLabel1 = new Label(key);
                vBox1.getChildren().add(sexeLabel1);
                vBox1.getStyleClass().add("cellSexe");
                gridPane.add(vBox1, colStart + 1, rowStartForSexe, 1, totalParticipant);
                rowStartForSexe = rowStartForData;
                totalRow = rowStartForData;
            }

            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            Label categorieLabel = new Label(keyCategorie);
            vBox.getChildren().add(categorieLabel);
            vBox.getStyleClass().add("cellCategorie");
            gridPane.add(vBox, colStart, rowStartForCategorie, 1, totalRow);
            rowStartForCategorie = totalRow;

        }


    }

    private void addData(GridPane gridPane, int colStart, int rowStart, String data) {
        VBox vBoxnom = new VBox();
        vBoxnom.setAlignment(Pos.CENTER);
        Label nomLabel = new Label(data);
        vBoxnom.getChildren().add(nomLabel);
        vBoxnom.getStyleClass().add("cellData");
        gridPane.add(vBoxnom, colStart, rowStart);
    }

    private void addTotalCategorie(GridPane gridPane, int colStart, int rowStart, String typeCategorie, int total) {
        VBox vBoxTotal = new VBox();
        vBoxTotal.setAlignment(Pos.CENTER);
        Label totalLabel = new Label("Total ".concat(typeCategorie));
        totalLabel.getStyleClass().add("labelTotal");
        vBoxTotal.getChildren().add(totalLabel);
        vBoxTotal.getStyleClass().add("cellTotal");
        gridPane.add(vBoxTotal, colStart, rowStart, 3, 1);

        VBox vBoxTotalValue = new VBox();
        vBoxTotalValue.setAlignment(Pos.CENTER);
        Label totalValue = new Label(String.valueOf(total));
        totalValue.getStyleClass().add("labelTotal");
        vBoxTotalValue.getChildren().add(totalValue);
        vBoxTotalValue.getStyleClass().add("cellTotal");
        gridPane.add(vBoxTotalValue, colStart + 3, rowStart);
    }

    private void drawHeader(GridPane gridPane, int colStart) {
        addHeaderLabel(gridPane, colStart, 1, "cat.");
        addHeaderLabel(gridPane, colStart + 1, 1, "sexe");
        addHeaderLabel(gridPane, colStart + 2, 1, "nom");
        addHeaderLabel(gridPane, colStart + 3, 1, "club");
        addHeaderLabel(gridPane, colStart + 4, 1, "Total");
    }

    private void addHeaderLabel(GridPane gridPane, int colStart, int rowStart, String data) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        Label nomLabel = new Label(data);
        nomLabel.getStyleClass().add("labelHeader");
        vBox.getChildren().add(nomLabel);
        vBox.getStyleClass().add("cellHeader");
        gridPane.add(vBox, colStart, rowStart);
    }

    public class TestStructure {
        private String nomCategorie;
        private Map<String, Map<String, List<Participant>>> typeCategories;

        public TestStructure(String name) {
            this.nomCategorie = name;
            this.typeCategories = new HashMap<String, Map<String, List<Participant>>>();
        }

        public void setNomCategorie(String nomCategorie) {
            this.nomCategorie = nomCategorie;
        }

        public void setTypeCategories(Map<String, Map<String, List<Participant>>> typeCategories) {
            this.typeCategories = typeCategories;
        }

        public String getNomCategorie() {
            return nomCategorie;
        }

        public Map<String, Map<String, List<Participant>>> getTypeCategories() {
            return typeCategories;
        }
    }

    private TestStructure createStructure(String name) {
        TestStructure testStructureMainNue = new TestStructure(name);
        Map<String, List<Participant>> testStructure = new HashMap<String, List<Participant>>();
        List<Participant> participants = new ArrayList<Participant>();
        for (int i = 0; i < 6; i++) {
            Participant participant = new Participant();
            participant.setNomParticipant("Nom " + i);
            participant.setPrenomParticipant("Prénom " + i);
            participants.add(participant);
        }
        testStructure.put("Masculin", participants);

        List<Participant> participants1 = new ArrayList<Participant>();
        for (int i = 0; i < 4; i++) {
            Participant participant = new Participant();
            participant.setNomParticipant("Nom " + i);
            participant.setPrenomParticipant("Prénom " + i);
            participants1.add(participant);
        }
        testStructure.put("Féminin", participants1);
        testStructureMainNue.getTypeCategories().put("Benjamin", testStructure);

        Map<String, List<Participant>> testStructure1 = new HashMap<String, List<Participant>>();
        List<Participant> participants2 = new ArrayList<Participant>();
        for (int i = 0; i < 7; i++) {
            Participant participant = new Participant();
            participant.setNomParticipant("Nom " + i);
            participant.setPrenomParticipant("Prénom " + i);
            participants2.add(participant);
        }
        testStructure1.put("Masculin", participants2);

        List<Participant> participants3 = new ArrayList<Participant>();
        for (int i = 0; i < 3; i++) {
            Participant participant = new Participant();
            participant.setNomParticipant("Nom " + i);
            participant.setPrenomParticipant("Prénom " + i);
            participants3.add(participant);
        }
        testStructure1.put("Féminin", participants3);
        testStructureMainNue.getTypeCategories().put("Minime", testStructure1);

        return testStructureMainNue;
    }

}
