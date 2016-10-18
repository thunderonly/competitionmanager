package fr.csmb.competition.controller;

import fr.csmb.competition.Helper.CategorieHelper;
import fr.csmb.competition.Helper.EleveHelper;
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

        NetworkSender.getINSTANCE().sendClub(competitionBean, clubBean);
    }

    public void updateEleve(EleveBean eleveBean, String licence, String nom, String prenom, String age, String poids, TypeCategorie sexe, List<String> epreuves) {
        setEleve(eleveBean, licence, nom, prenom, age, poids, sexe, epreuves);
        //Call PresenceListener
        NetworkSender.getINSTANCE().sendClub(competitionBean, clubBean);
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

        EleveHelper.manageEpreuves(competitionBean, clubBean, eleveBean, epreuves);
    }

    public void setCompetitionBean(CompetitionBean competitionBean) {
        this.competitionBean = competitionBean;
    }

    public void setClubBean(ClubBean clubBean) {
        this.clubBean = clubBean;
    }

}
