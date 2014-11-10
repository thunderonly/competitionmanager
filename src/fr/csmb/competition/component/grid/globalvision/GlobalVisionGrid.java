/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.component.grid.globalvision;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.csmb.competition.xml.model.Participant;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class GlobalVisionGrid {

    public GridPane drawGlobalVisionGrid(List<GlobalVision> testStructures) {
        GridPane gridPane = new GridPane();
        gridPane.getStylesheets().addAll(getClass().getResource("css/globalVisionGrid.css").toExternalForm());
        int startColumn = 0;
        for (GlobalVision structure : testStructures) {
            if (haveParticipant(structure)) {
                VBox title = new VBox();
                title.setAlignment(Pos.CENTER);
                Label categorieTitle = new Label(structure.getNomCategorie());
                categorieTitle.getStyleClass().add("biglabelGridPane");
                title.getChildren().add(categorieTitle);
                gridPane.add(title, startColumn, 0, 5, 1);

                drawHeader(gridPane, startColumn);
                drawBody(gridPane, startColumn, 2, structure.getTypeCategories());
                startColumn += 5;
            }
        }
        return gridPane;
    }

    private boolean haveParticipant(GlobalVision structure) {
        boolean result = false;
        for (String keyCategorie : structure.getTypeCategories().keySet()) {
            for (String key : structure.getTypeCategories().get(keyCategorie).keySet()) {
                if (structure.getTypeCategories().get(keyCategorie).get(key).size() > 0) {
                    return true;
                }
            }
        }

        return result;
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
                    addData(gridPane, colStart + 3, rowStartForData, participant.getClubParticipant());
                    addData(gridPane, colStart + 4, rowStartForData, "1");
                    rowStartForData++;
                }
                if (totalParticipant > 0) {
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
            }
            if (totalRow > 0) {
                VBox vBox = new VBox();
                vBox.setAlignment(Pos.CENTER);
                Label categorieLabel = new Label(keyCategorie);
                vBox.getChildren().add(categorieLabel);
                vBox.getStyleClass().add("cellCategorie");
                gridPane.add(vBox, colStart, rowStartForCategorie, 1, totalRow - rowStartForCategorie);
                rowStartForCategorie = totalRow;
            }

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
}
