package fr.csmb.competition.Helper;

import fr.csmb.competition.model.*;

/**
 * Created by Administrateur on 25/11/15.
 */
public class ParticipantHelper {

    /**
     * Create all participant from epreuves of eleveBean
     * @param competitionBean
     * @param eleveBean
     * @param clubBean
     */
    public static void createParticipantsFromEleveBean(CompetitionBean competitionBean, EleveBean eleveBean, ClubBean clubBean) {
        //Check if participant exist for epreuve of eleve
        CategorieBean categorieBean = competitionBean.getCategorie(eleveBean.getSexe(), eleveBean.getCategorie());
        if (categorieBean != null) {
            for (String epreuve : eleveBean.getEpreuves()) {
                createParticipantFromEleveBeanCategorie(competitionBean, eleveBean, clubBean, categorieBean, epreuve);
            }
        }
    }

    public static void createParticipantFromEleveBeanCategorie(
            CompetitionBean competitionBean, EleveBean eleveBean, ClubBean clubBean, CategorieBean categorieBean, String epreuve) {
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
                participantBean.setParticipe(true);
            }
        }
    }
}
