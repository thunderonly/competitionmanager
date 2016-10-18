/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.Helper;

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
            categorieBean.setSexe(categorie.getTypeCategorie());
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

        ObservableList<EpreuveBean> epreuveBeans = FXCollections.observableArrayList();
        for (Epreuve epreuve : competition.getEpreuve()) {
            EpreuveBean epreuveBean = EpreuveConverter.convertEpreuveToEpreuveBean(epreuve);
            epreuveBeans.add(epreuveBean);
        }
        competitionBean.setEpreuves(epreuveBeans);

        ObservableList<ParticipantBean> participantBeans = FXCollections.observableArrayList();
        for (Participant participant : competition.getParticipant()) {
            ParticipantBean participantBean = ParticipantConverter.convertParticipantToParticipantBean(participant);
            participantBeans.add(participantBean);
        }
        competitionBean.setParticipants(participantBeans);

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
            Categorie categorie = new Categorie(categorieBean.getNom(), categorieBean.getSexe());
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

        for (ParticipantBean participantBean : competitionBean.getParticipants()) {
            Participant participant = ParticipantConverter.convertParticipantBeanToParticipant(participantBean);
            competition.getParticipant().add(participant);
        }

        for (ClubBean clubBean : competitionBean.getClubs()) {
            Club club = ClubConverter.convertClubBeanToClub(clubBean);
            competition.getClubs().add(club);
        }
        return competition;
    }
}
