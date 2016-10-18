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
import fr.csmb.competition.model.*;
import fr.csmb.competition.component.grid.globalvision.GlobalVision;
import fr.csmb.competition.component.grid.globalvision.GlobalVisionGrid;
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
        for (EpreuveBean epreuveBean : competitionBean.getEpreuves()) {
            GlobalVision testStructure = null;
            if (map.containsKey(epreuveBean.getDiscipline().getNom())) {
                testStructure = map.get(epreuveBean.getDiscipline().getNom());
            } else {
                testStructure = new GlobalVision(epreuveBean.getDiscipline().getNom());
                map.put(epreuveBean.getDiscipline().getNom(), testStructure);
            }

            Map<String, List<Participant>> map1 = null;
            if(testStructure.getTypeCategories().containsKey(epreuveBean.getCategorie().getNom())) {
                map1 = testStructure.getTypeCategories().get(epreuveBean.getCategorie().getNom());
            } else {
                map1 = new HashMap<String, List<Participant>>();
                testStructure.getTypeCategories().put(epreuveBean.getCategorie().getNom(), map1);
            }

            List<Participant> participants = null;
            if (map1.containsKey(epreuveBean.getCategorie().getSexe())) {
                participants = map1.get(epreuveBean.getCategorie().getSexe());
            } else {
                participants = new ArrayList<Participant>();
                map1.put(epreuveBean.getCategorie().getSexe(), participants);
            }

            ObservableList<ParticipantBean> participantBeans = extractParticipants(epreuveBean.getCategorie().getSexe(), epreuveBean.getCategorie().getNom(), epreuveBean.getDiscipline().getNom());
            for (ParticipantBean participantBean : participantBeans) {
                Participant participant = ParticipantConverter.convertParticipantBeanToParticipant(participantBean);
                participants.add(participant);
            }

        }

        return map;
    }

    public EpreuveBean getEpreuveBean(String typeCategorie, String categorie, String epreuve) {
        CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
        DisciplineBean disciplineBean = competitionBean.getDiscipline(epreuve);
        EpreuveBean epreuveBean = null;
        if (categorieBean != null && disciplineBean != null) {
            epreuveBean = competitionBean.getEpreuve(categorieBean, disciplineBean, epreuve);
        }
        return epreuveBean;
    }

    public ObservableList<ParticipantBean> extractParticipants(String typeCategorie, String categorie, String epreuve) {
        ObservableList<ParticipantBean> participantBeans1 = FXCollections.observableArrayList();
        EpreuveBean epreuveBean = getEpreuveBean(typeCategorie, categorie, epreuve);
        if (epreuveBean != null) {
            participantBeans1.addAll(competitionBean.getParticipantByEpreuve(epreuveBean));
        }

        return participantBeans1;
    }
}
