/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.csmb.competition.Helper.ParticipantConverter;
import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.component.grid.globalvision.GlobalVision;
import fr.csmb.competition.component.grid.globalvision.GlobalVisionGrid;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.xml.model.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class GlobalVisionView {

    private Stage currentStage;
    private CompetitionBean competitionBean;

    public GlobalVisionView() {

    }

    public void showView(Stage mainStage, CompetitionBean competitionBean) {
        this.currentStage = mainStage;
        this.competitionBean = competitionBean;

        GlobalVisionGrid globalVisionGrid = new GlobalVisionGrid();
        List<GlobalVision> visions = new ArrayList<GlobalVision>();
        for (GlobalVision globalVision : computeStructure().values()) {
            visions.add(globalVision);
        }
        GridPane gridPane = globalVisionGrid.drawGlobalVisionGrid(visions);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridPane);
        BorderPane root = (BorderPane) mainStage.getScene().getRoot();
        root.setCenter(scrollPane);
    }

    private Map<String, GlobalVision> computeStructure() {
        Map<String, GlobalVision> map = new HashMap<String, GlobalVision>();
        for (CategorieBean categorieBean : competitionBean.getCategories()) {
            for (EpreuveBean epreuveBean : categorieBean.getEpreuves()) {
                GlobalVision testStructure = null;
                if (map.containsKey(epreuveBean.getNom())) {
                    testStructure = map.get(epreuveBean.getNom());
                } else {
                    testStructure = new GlobalVision(epreuveBean.getNom());
                    map.put(epreuveBean.getNom(), testStructure);
                }

                Map<String, List<Participant>> map1 = null;
                if(testStructure.getTypeCategories().containsKey(categorieBean.getNom())) {
                    map1 = testStructure.getTypeCategories().get(categorieBean.getNom());
                } else {
                    map1 = new HashMap<String, List<Participant>>();
                    testStructure.getTypeCategories().put(categorieBean.getNom(), map1);
                }

                List<Participant> participants = null;
                if (map1.containsKey(categorieBean.getType())) {
                    participants = map1.get(categorieBean.getType());
                } else {
                    participants = new ArrayList<Participant>();
                    map1.put(categorieBean.getType(), participants);
                }

                ObservableList<ParticipantBean> participantBeans = extractParticipants(categorieBean.getType(), categorieBean.getNom(), epreuveBean.getNom());
                for (ParticipantBean participantBean : participantBeans) {
                    Participant participant = ParticipantConverter.convertParticipantBeanToParticipant(participantBean);
                    participants.add(participant);
                }

            }
        }

        return map;
    }

    public ObservableList<ParticipantBean> extractParticipants(String typeCategorie, String categorie, String epreuve) {
        ObservableList<ParticipantBean> participantBeans1 = FXCollections.observableArrayList();
        for (ClubBean clubBean : competitionBean.getClubs()) {
            for (EleveBean eleveBean : clubBean.getEleves()) {
                if (categorie.equals(eleveBean.getCategorie()) && typeCategorie.equals(eleveBean.getSexe())) {
                    if (eleveBean.getEpreuves().contains(epreuve)) {
                        ParticipantBean participantBean = new ParticipantBean(eleveBean.getNom(), eleveBean.getPrenom());
                        participantBean.setClub(clubBean.getNom());
                        participantBeans1.add(participantBean);
                    }
                }
            }
        }

        return participantBeans1;
    }
}
