/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.Helper;

import java.util.ArrayList;

import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.model.*;
import fr.csmb.competition.xml.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class CompetitionConverter {

    public static CompetitionBean convertCompetitionToCompetitionBean(Competition competition) {
        CompetitionBean competitionBean = new CompetitionBean(competition.getNom());
        ObservableList<CategorieBean> categorieBeans = FXCollections.observableArrayList();
        for (Categorie categorie : competition.getCategories()) {
            CategorieBean categorieBean = new CategorieBean(categorie.getNomCategorie());
            categorieBean.setType(categorie.getTypeCategorie());
            categorieBeans.add(categorieBean);
        }
        competitionBean.setCategories(categorieBeans);

        ObservableList<DisciplineBean> disciplineBeans = FXCollections.observableArrayList();
        for (Discipline discipline : competition.getDiscipline()) {
            DisciplineBean disciplineBean = new DisciplineBean(discipline.getNom(), discipline.getType());
            disciplineBean.setType(discipline.getType());
            disciplineBeans.add(disciplineBean);
        }
        competitionBean.setDisciplines(disciplineBeans);

        ObservableList<ClubBean> clubBeans = FXCollections.observableArrayList();
        for (Club club : competition.getClubs()) {
            ClubBean clubBean = ClubConverter.convertClubToClubBean(club);
            clubBeans.add(clubBean);
        }
        competitionBean.setClubs(clubBeans);

        return competitionBean;
    }

    public static Competition convertCompetitionBeanToCompetition(CompetitionBean competitionBean) {
        Competition competition = new Competition(competitionBean.getNom());
        for (CategorieBean categorieBean : competitionBean.getCategories()) {
            Categorie categorie = new Categorie(categorieBean.getNom(), categorieBean.getType());
            competition.getCategories().add(categorie);
        }

        for (EpreuveBean epreuveBean : competitionBean.getEpreuves()) {
            Epreuve epreuve = EpreuveConverter.convertEpreuveBeanToEpreuve(epreuveBean);
            competition.getEpreuve().add(epreuve);
        }

        for (DisciplineBean disciplineBean : competitionBean.getDisciplines()) {
            Discipline discipline = new Discipline(disciplineBean.getNom(), disciplineBean.getType());
            competition.getDiscipline().add(discipline);
        }

        for (ClubBean clubBean : competitionBean.getClubs()) {
            Club club = ClubConverter.convertClubBeanToClub(clubBean);
            competition.getClubs().add(club);
        }
        return competition;
    }
}
