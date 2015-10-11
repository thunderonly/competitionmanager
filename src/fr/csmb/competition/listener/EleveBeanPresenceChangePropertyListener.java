package fr.csmb.competition.listener;

import fr.csmb.competition.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Created by Administrateur on 10/03/15.
 */
public class EleveBeanPresenceChangePropertyListener implements ChangeListener<Boolean> {

    private CompetitionBean competitionBean;
    private ClubBean clubBean;
    private EleveBean eleveBean;

    @Override
    public void changed(ObservableValue<? extends java.lang.Boolean> observableValue, Boolean o, Boolean t1) {
        if (t1) {
            //Check if participant exist for epreuve of eleve
            CategorieBean categorieBean = competitionBean.getCategorie(eleveBean.getSexe(), eleveBean.getCategorie());
            if (categorieBean != null) {
                for (String epreuve : eleveBean.getEpreuves()) {
                    if (!"".equals(epreuve)) {
                        DisciplineBean disciplineBean = competitionBean.getDiscipline(epreuve);
                        EpreuveBean epreuveBean = competitionBean.getEpreuve(categorieBean, disciplineBean);
                        boolean participantExist = false;
                        for (ParticipantBean participantBean : competitionBean.getParticipantByEpreuve(
                                epreuveBean)) {
                            if (participantBean.getNom().equals(eleveBean.getNom()) &&
                                    participantBean.getPrenom().equals(eleveBean.getPrenom())) {
                                participantExist = true;
                            }
                        }
                        if (!participantExist) {
                            ParticipantBean participantBean = new ParticipantBean(eleveBean.getNom(), eleveBean.getPrenom());
                            participantBean.setEpreuveBean(epreuveBean);
                            if (eleveBean.getPoids() != null && !eleveBean.getPoids().trim().equals("")) {
                                participantBean.setPoids(Integer.parseInt(eleveBean.getPoids()));
                            }
                            participantBean.setClub(clubBean.getIdentifiant());
                            competitionBean.getParticipants().add(participantBean);
                        }
                    }
                }
            }
            for (ParticipantBean participantBean : competitionBean.getParticipantByNomPrenom(
                    eleveBean.getNom(), eleveBean.getPrenom())) {
                participantBean.setParticipe(true);
            }


        } else {
            for (ParticipantBean participantBean : competitionBean.getParticipantByNomPrenom(
                    eleveBean.getNom(), eleveBean.getPrenom())) {
                participantBean.setParticipe(false);
            }
        }
    }

    public void setCompetitionBean(CompetitionBean competitionBean) {
        this.competitionBean = competitionBean;
    }

    public void setClubBean(ClubBean clubBean) {
        this.clubBean = clubBean;
    }

    public void setEleveBean(EleveBean eleveBean) {
        this.eleveBean = eleveBean;
    }
}
