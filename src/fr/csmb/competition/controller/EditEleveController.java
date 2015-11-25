package fr.csmb.competition.controller;

import fr.csmb.competition.Helper.CategorieHelper;
import fr.csmb.competition.Helper.ParticipantHelper;
import fr.csmb.competition.listener.EleveBeanPresenceChangePropertyListener;
import fr.csmb.competition.model.*;
import fr.csmb.competition.model.comparator.EpreuveCombatComparator;
import fr.csmb.competition.network.sender.NetworkSender;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.type.TypeCategorie;
import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.view.ConfigureFightView;
import fr.csmb.competition.xml.model.Epreuve;
import javafx.stage.Stage;

import java.util.*;

/**
 * Created by Administrateur on 11/11/14.
 */
public class EditEleveController {

    private CompetitionBean competitionBean;
    private ClubBean clubBean;

    public void addEleve(String licence, String nom, String prenom, String age, String poids, TypeCategorie sexe, List<String> epreuves) {
        EleveBean eleveBean = new EleveBean();
        setEleve(eleveBean, licence, nom, prenom, age, poids, sexe, epreuves);
        clubBean.getEleves().add(eleveBean);

        EleveBeanPresenceChangePropertyListener propertyListener = new EleveBeanPresenceChangePropertyListener();
        propertyListener.setCompetitionBean(competitionBean);
        propertyListener.setClubBean(clubBean);
        propertyListener.setEleveBean(eleveBean);
        eleveBean.presenceProperty().addListener(propertyListener);
        eleveBean.setPresence(true);

        NetworkSender.getINSTANCE().sendClub(clubBean);
    }

    public void updateEleve(EleveBean eleveBean, String licence, String nom, String prenom, String age, String poids, TypeCategorie sexe, List<String> epreuves) {
        setEleve(eleveBean, licence, nom, prenom, age, poids, sexe, epreuves);
        //Call PresenceListener
        NetworkSender.getINSTANCE().sendClub(clubBean);
    }

    private void setEleve(EleveBean eleveBean, String licence, String nom, String prenom, String age, String poids, TypeCategorie sexe, List<String> epreuves) {
        eleveBean.setLicence(licence);
        eleveBean.setNom(nom);
        eleveBean.setPrenom(prenom);
        eleveBean.setAge(age);
        eleveBean.setPoids(poids);
        eleveBean.setSexe(sexe.getValue());
        eleveBean.setCategorie(CategorieHelper.getCategorieFromAge(age));
        //Find epreuve to be remove
        CategorieBean categorieBean = competitionBean.getCategorie(eleveBean.getSexe(), eleveBean.getCategorie());
        if (categorieBean != null) {
            List<String> epreuvesToBeRemove = new ArrayList<String>();
            for (String epreuve : eleveBean.getEpreuves()) {
                if (!epreuves.contains(epreuve)) {
                    epreuvesToBeRemove.add(epreuve);
                }
            }

            List<String> epreuvesToBeAdd = new ArrayList<String>();
            for (String epreuve : epreuves) {
                String newEpreuve = epreuve;
                if (epreuve.equals(TypeEpreuve.COMBAT.getValue())) {
                    newEpreuve = CategorieHelper.extractCategorieCombat(eleveBean, competitionBean);
                }

                if (!eleveBean.getEpreuves().contains(newEpreuve)) {
                    epreuvesToBeAdd.add(newEpreuve);
                }
            }

            removeEpreuves(epreuvesToBeRemove, categorieBean, eleveBean);
            addEpreuves(epreuvesToBeAdd, categorieBean, eleveBean);


        }
    }

    public void setCompetitionBean(CompetitionBean competitionBean) {
        this.competitionBean = competitionBean;
    }

    public void setClubBean(ClubBean clubBean) {
        this.clubBean = clubBean;
    }

    private void addEpreuves(List<String> epreuvesToBeAdd, CategorieBean categorieBean, EleveBean eleveBean) {
        for (String epreuve : epreuvesToBeAdd) {
            String newEpreuve = epreuve;
            if (epreuve.equals(TypeEpreuve.COMBAT.getValue())) {
                newEpreuve = CategorieHelper.extractCategorieCombat(eleveBean, competitionBean);
            }

            if (!eleveBean.getEpreuves().contains(newEpreuve)) {
                eleveBean.getEpreuves().add(newEpreuve);
                DisciplineBean disciplineBean = competitionBean.getDiscipline(newEpreuve);
                if (categorieBean != null) {
                    ParticipantHelper.createParticipantFromEleveBeanCategorie(
                            competitionBean, eleveBean, clubBean, categorieBean, newEpreuve);

                    EpreuveBean epreuveBean = competitionBean.getEpreuve(categorieBean, disciplineBean);
                    if (disciplineBean.getType().equals(TypeEpreuve.COMBAT.getValue())) {
                        if (epreuveBean.getEtat() != null &&
                                (epreuveBean.getEtat().equals(EtatEpreuve.VALIDE.getValue()) ||
                                        epreuveBean.getEtat().equals(EtatEpreuve.DEMARRE.getValue()))) {
                            final Stage newStage = new Stage();
                            ConfigureFightView configureFightView = new ConfigureFightView();
                            configureFightView.showView(newStage, competitionBean, epreuveBean);
                        }
                    }
                }
            }
        }
    }

    private void removeEpreuves(List<String> epreuvesToBeRemove, CategorieBean categorieBean, EleveBean eleveBean) {
        for (String epreuve : epreuvesToBeRemove) {
            DisciplineBean disciplineBean = competitionBean.getDiscipline(epreuve);
            EpreuveBean epreuveBean = competitionBean.getEpreuve(categorieBean, disciplineBean);
            ParticipantBean participantBeanToBeRemove = null;
            eleveBean.getEpreuves().remove(epreuve);
            for (ParticipantBean participantBean : competitionBean.getParticipantByEpreuve(
                    epreuveBean)) {
                if (participantBean.getNom().equals(eleveBean.getNom()) &&
                        participantBean.getPrenom().equals(eleveBean.getPrenom())) {
                    participantBeanToBeRemove = participantBean;
                }
            }
            if (participantBeanToBeRemove != null) {
                competitionBean.getParticipants().remove(participantBeanToBeRemove);
            }
            if (disciplineBean.getType().equals(TypeEpreuve.COMBAT.getValue())) {
                if (epreuveBean.getEtat() != null &&
                        (epreuveBean.getEtat().equals(EtatEpreuve.VALIDE.getValue()) ||
                        epreuveBean.getEtat().equals(EtatEpreuve.DEMARRE.getValue()))) {
                    final Stage newStage = new Stage();
                    ConfigureFightView configureFightView = new ConfigureFightView();
                    configureFightView.showView(newStage, competitionBean, epreuveBean);
                }
            }
        }
    }
}
