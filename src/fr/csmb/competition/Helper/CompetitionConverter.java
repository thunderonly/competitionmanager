/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.Helper;

import java.util.ArrayList;

import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.xml.model.Categorie;
import fr.csmb.competition.xml.model.Club;
import fr.csmb.competition.xml.model.Competition;
import fr.csmb.competition.xml.model.Eleve;
import fr.csmb.competition.xml.model.Epreuve;
import fr.csmb.competition.xml.model.Participant;
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
            ObservableList<EpreuveBean> epreuveBeans = FXCollections.observableArrayList();
            for (Epreuve epreuve : categorie.getEpreuves()) {
                EpreuveBean epreuveBean = new EpreuveBean(epreuve.getNomEpreuve());
                epreuveBean.setEtat(epreuve.getEtatEpreuve());
                epreuveBean.setType(epreuve.getTypeEpreuve());
                ObservableList<ParticipantBean> participantBeans = FXCollections.observableArrayList();
                for (Participant participant : epreuve.getParticipants()) {
                    ParticipantBean participantBean = ParticipantConverter.convertParticipantToParticipantBean(participant);
                    participantBeans.add(participantBean);
                }
                epreuveBean.setAdministrateur(epreuve.getAdministrateur());
                epreuveBean.setChronometreur(epreuve.getChronometreur());
                epreuveBean.setDuree(epreuve.getDuree());
                epreuveBean.setHeureDebut(epreuve.getHeureDebut());
                epreuveBean.setHeureFin(epreuve.getHeureFin());
                epreuveBean.setJuge1(epreuve.getJuge1());
                epreuveBean.setJuge2(epreuve.getJuge2());
                epreuveBean.setJuge3(epreuve.getJuge3());
                epreuveBean.setJuge4(epreuve.getJuge4());
                epreuveBean.setJuge5(epreuve.getJuge5());
                epreuveBean.setParticipants(participantBeans);
                epreuveBeans.add(epreuveBean);
            }
            categorieBean.setEpreuves(epreuveBeans);
            categorieBeans.add(categorieBean);
        }
        competitionBean.setCategories(categorieBeans);

        ObservableList<ClubBean> clubBeans = FXCollections.observableArrayList();
        for (Club club : competition.getClubs()) {
            ClubBean clubBean = new ClubBean();
            clubBean.setNom(club.getNomClub());
            clubBean.setIdentifiant(club.getIdentifiant());
            clubBean.setResponsable(club.getResponsable());

            ObservableList<EleveBean> eleveBeans = FXCollections.observableArrayList();
            for (Eleve eleve : club.getEleves()) {
                EleveBean eleveBean = new EleveBean();
                eleveBean.setNom(eleve.getNomEleve());
                eleveBean.setPrenom(eleve.getPrenomEleve());
                eleveBean.setLicence(eleve.getLicenceEleve());
                eleveBean.setAge(eleve.getAgeEleve());
                eleveBean.setCategorie(eleve.getCategorieEleve());
                eleveBean.setPoids(eleve.getPoidsEleve());
                eleveBean.setSexe(eleve.getSexeEleve());
                eleveBean.setEpreuves(FXCollections.observableArrayList(eleve.getEpreuvesEleves()));
                eleveBeans.add(eleveBean);
            }
            clubBean.setEleves(eleveBeans);
            clubBeans.add(clubBean);
        }
        competitionBean.setClubs(clubBeans);

        return competitionBean;
    }

    public static Competition convertCompetitionBeanToCompetition(CompetitionBean competitionBean) {
        Competition competition = new Competition(competitionBean.getNom());
        for (CategorieBean categorieBean : competitionBean.getCategories()) {
            Categorie categorie = new Categorie();
            categorie.setNomCategorie(categorieBean.getNom());
            categorie.setTypeCategorie(categorieBean.getType());
            categorie.setEpreuves(new ArrayList<Epreuve>());
            competition.getCategories().add(categorie);
            for (EpreuveBean epreuveBean : categorieBean.getEpreuves()) {
                Epreuve epreuve = new Epreuve();
                epreuve.setNomEpreuve(epreuveBean.getNom());
                epreuve.setTypeEpreuve(epreuveBean.getType());
                epreuve.setEtatEpreuve(epreuveBean.getEtat());
                epreuve.setAdministrateur(epreuveBean.getAdministrateur());
                epreuve.setChronometreur(epreuveBean.getChronometreur());
                epreuve.setJuge1(epreuveBean.getJuge1());
                epreuve.setJuge2(epreuveBean.getJuge2());
                epreuve.setJuge3(epreuveBean.getJuge3());
                epreuve.setJuge4(epreuveBean.getJuge4());
                epreuve.setJuge5(epreuveBean.getJuge5());
                epreuve.setTapis(epreuveBean.getTapis());
                epreuve.setHeureDebut(epreuveBean.getHeureDebut());
                epreuve.setHeureFin(epreuveBean.getHeureFin());
                epreuve.setDuree(epreuveBean.getDuree());
                categorie.getEpreuves().add(epreuve);
                for (ParticipantBean participantBean : epreuveBean.getParticipants()) {
                    Participant participant = ParticipantConverter.convertParticipantBeanToParticipant(participantBean);
                    epreuve.getParticipants().add(participant);
                }
            }
        }

        for (ClubBean clubBean : competitionBean.getClubs()) {
            Club club = new Club();
            club.setIdentifiant(clubBean.getIdentifiant());
            club.setNomClub(clubBean.getNom());
            club.setResponsable(clubBean.getResponsable());
            competition.getClubs().add(club);
            for (EleveBean eleveBean : clubBean.getEleves()) {
                Eleve eleve = new Eleve(eleveBean.getNom(), eleveBean.getPrenom());
                eleve.setCategorieEleve(eleveBean.getCategorie());
                eleve.setSexeEleve(eleveBean.getSexe());
                eleve.setAgeEleve(eleveBean.getAge());
                eleve.setLicenceEleve(eleveBean.getLicence());
                eleve.setPoidsEleve(eleveBean.getPoids());
                for (String epreuve : eleveBean.getEpreuves()) {
                    eleve.getEpreuvesEleves().add(epreuve);
                }
                club.getEleves().add(eleve);
            }
        }
        return competition;
    }
}
